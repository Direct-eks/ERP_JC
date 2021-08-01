package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.InvoiceEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.entity.VO.InvoiceEntryStandAloneVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.jc.backend.utils.EntryClassification.*;

@Service
public class InvoiceEntryServiceImpl implements InvoiceEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryServiceImpl.class);

    private final InvoiceEntryMapper invoiceEntryMapper;
    private final ModificationMapper modificationMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;

    public InvoiceEntryServiceImpl(InvoiceEntryMapper invoiceEntryMapper,
                                   ModificationMapper modificationMapper,
                                   InboundEntryService inboundEntryService,
                                   OutboundEntryService outboundEntryService) {
        this.invoiceEntryMapper = invoiceEntryMapper;
        this.modificationMapper = modificationMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    private static String getEntryPrefix(boolean isInbound, String invoiceType) {
        return isInbound ?
                (invoiceType.equals("增值税票") ? INBOUND_INVOICE_VAT : INBOUND_INVOICE_NOR) :
                (invoiceType.equals("增值税票") ? OUTBOUND_INVOICE_VAT : OUTBOUND_INVOICE_NOR);
    }

    @Transactional
    @Override
    public void createEntry(InvoiceEntryStandAloneVO invoiceEntryStandAloneVO, boolean isInbound) {

        InvoiceEntryO invoiceEntryO = new InvoiceEntryO();
        BeanUtils.copyProperties(invoiceEntryStandAloneVO, invoiceEntryO);

        try {
            String prefix = getEntryPrefix(isInbound, invoiceEntryO.getInvoiceType());

            int count = invoiceEntryMapper.countNumberOfEntriesOfToday(invoiceEntryO.getInvoiceDate(), prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, invoiceEntryO.getInvoiceDate());

            invoiceEntryO.setInvoiceEntrySerial(newSerial);
            invoiceEntryMapper.insertEntry(invoiceEntryO);

            if (isInbound) {
                inboundEntryService.updateProductsWithInvoiceSerial(
                        invoiceEntryStandAloneVO.getInboundInvoiceProducts(), newSerial);
            }
            else {
                outboundEntryService.updateProductsWithInvoiceSerial(
                        invoiceEntryStandAloneVO.getOutboundInvoiceProducts(), newSerial);
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public List<InvoiceEntryStandAloneVO> getEntriesInDateRange(Date startDate, Date endDate, Date invoiceNumberDate,
                                                                int companyID, int isFollowUpIndication,
                                                                String invoiceNumber, String invoiceType,
                                                                boolean isInbound) {
        try {
            String prefix1, prefix2;
            if (invoiceType.equals("")) {
                prefix1 = isInbound ? INBOUND_INVOICE_VAT : OUTBOUND_INVOICE_VAT;
                prefix2 = isInbound ? INBOUND_INVOICE_NOR : OUTBOUND_INVOICE_NOR;
            }
            else {
                prefix1 = getEntryPrefix(isInbound, invoiceType);
                prefix2 = "x";
            }

            List<InvoiceEntryO> entriesFromDatabase = invoiceEntryMapper.getEntriesInDateRangeAndParams(
                    MyUtils.dateFormat.format(startDate), MyUtils.dateFormat.format(endDate),
                    invoiceNumberDate == null ? null : MyUtils.dateFormat.format(invoiceNumberDate),
                    companyID, isFollowUpIndication, invoiceNumber, invoiceType, prefix1, prefix2);

            List<InvoiceEntryStandAloneVO> entries = new ArrayList<>();
            for (var entryFromDatabase : entriesFromDatabase) {
                InvoiceEntryStandAloneVO entry = new InvoiceEntryStandAloneVO();
                BeanUtils.copyProperties(entryFromDatabase, entry);

                if (isInbound) {
                    List<InboundProductO> products = inboundEntryService.getProductsWithInvoiceSerial(
                            entryFromDatabase.getInvoiceEntrySerial());
                    entry.setInboundInvoiceProducts(products);
                }
                else {
                    List<OutboundProductO> products = outboundEntryService.getProductsWithInvoiceSerial(
                            entryFromDatabase.getInvoiceEntrySerial());
                    entry.setOutboundInvoiceProducts(products);
                }

                entries.add(entry);
            }
            return entries;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("query failed");
            throw e;
        }
    }

    @Transactional
    @Override
    public void modifyEntry(InvoiceEntryStandAloneVO invoiceEntryStandAloneVO) {

        InvoiceEntryO modifiedEntry = new InvoiceEntryO();
        BeanUtils.copyProperties(invoiceEntryStandAloneVO, modifiedEntry);

        try {
            InvoiceEntryO originEntry = invoiceEntryMapper.selectEntryBySerialForCompare(
                    modifiedEntry.getInvoiceEntrySerial());

            StringBuilder record = new StringBuilder("修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean bool = compareEntryAndFormModificationRecord(record, originEntry, modifiedEntry);

            if (bool) {
                invoiceEntryMapper.updateEntry(modifiedEntry);

                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getInvoiceEntrySerial(), record.toString()));
            }
            else {
                logger.warn("nothing modified");
            }

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("update failed");
            throw e;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private static boolean compareEntryAndFormModificationRecord(StringBuilder record, InvoiceEntryO originEntry,
                                                          InvoiceEntryO modifiedEntry) {
        boolean bool = false;
        if (!originEntry.getInvoiceNumber().equals(modifiedEntry.getInvoiceNumber())) {
            bool = true;
            record.append(String.format("发票号码: %s -> %s; ", originEntry.getInvoiceNumber(),
                    modifiedEntry.getInvoiceNumber()));
        }
        if (new BigDecimal(originEntry.getInvoiceAmount())
                .compareTo(new BigDecimal(modifiedEntry.getInvoiceAmount())) != 0) {
            bool = true;
            record.append(String.format("开票金额: %s -> %s; ", originEntry.getInvoiceAmount(),
                    modifiedEntry.getInvoiceAmount()));
        }
        if (new BigDecimal(originEntry.getPartnerCompanyID())
                .compareTo(new BigDecimal(modifiedEntry.getPartnerCompanyID())) != 0) {
            bool = true;
            record.append(String.format("单位: %s -> %s; ", originEntry.getCompanyAbbreviatedName(),
                    modifiedEntry.getCompanyAbbreviatedName()));
        }
        if (!originEntry.getInvoiceNumberDate().equals(modifiedEntry.getInvoiceNumberDate())) {
            bool = true;
            record.append(String.format("发票开具日期: %s -> %s; ", originEntry.getInvoiceNumberDate(),
                    modifiedEntry.getInvoiceNumberDate()));
        }
        if (!originEntry.getRemark().equals(modifiedEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }


    @Transactional
    @Override
    public String createEntryForCheckout(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO, boolean isInbound) {

        InvoiceEntryO invoiceEntry = new InvoiceEntryO();
        BeanUtils.copyProperties(checkoutEntryWithProductsVO.getInvoiceEntry(), invoiceEntry);

        try {
            String prefix = getEntryPrefix(isInbound, checkoutEntryWithProductsVO.getInvoiceType());

            int count = invoiceEntryMapper.countNumberOfEntriesOfToday(invoiceEntry.getInvoiceDate(), prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, invoiceEntry.getInvoiceDate());

            invoiceEntry.setInvoiceEntrySerial(newSerial);
            invoiceEntry.setCheckoutDate(checkoutEntryWithProductsVO.getCheckoutDate());

            invoiceEntryMapper.insertEntry(invoiceEntry);

            return newSerial;

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("insert failed");
            throw e;
        }
    }
}
