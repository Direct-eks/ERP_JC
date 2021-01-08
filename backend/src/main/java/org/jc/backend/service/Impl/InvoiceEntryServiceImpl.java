package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.InvoiceEntryMapper;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.entity.VO.InvoiceEntryStandAloneVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceEntryServiceImpl implements InvoiceEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryServiceImpl.class);

    private final InvoiceEntryMapper invoiceEntryMapper;
    private final ModificationMapper modificationMapper;
    private final InboundEntryService inboundEntryService;

    public InvoiceEntryServiceImpl(InvoiceEntryMapper invoiceEntryMapper,
                                   ModificationMapper modificationMapper,
                                   InboundEntryService inboundEntryService) {
        this.invoiceEntryMapper = invoiceEntryMapper;
        this.modificationMapper = modificationMapper;
        this.inboundEntryService = inboundEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(InvoiceEntryStandAloneVO invoiceEntryStandAloneVO, boolean isInbound) {

        InvoiceEntryO invoiceEntryO = new InvoiceEntryO();
        BeanUtils.copyProperties(invoiceEntryStandAloneVO, invoiceEntryO);

        try {
            String prefix = isInbound ?
                    (invoiceEntryO.getInvoiceType().equals("增值税票") ? "入增" : "入普") :
                    (invoiceEntryO.getInvoiceType().equals("增值税票") ? "出增" : "出普");

            int count = invoiceEntryMapper.countNumberOfEntriesOfToday(prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, invoiceEntryO.getInvoiceDate());

            invoiceEntryO.setInvoiceEntrySerial(newSerial);
            invoiceEntryMapper.insertEntry(invoiceEntryO);

            inboundEntryService.updateProductsWithInvoiceSerial(
                    invoiceEntryStandAloneVO.getInvoiceProducts(), newSerial);
        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }

    @Transactional
    public List<InvoiceEntryStandAloneVO> getEntriesInDateRange(Date startDate, Date endDate, Date invoiceNumberDate,
                                                                int companyID, int isFollowUpIndication,
                                                                String invoiceNumber, String invoiceType, boolean isInbound) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<InvoiceEntryStandAloneVO> entries = new ArrayList<>();
        try {
            String prefix1, prefix2;
            if (invoiceType.equals("")) {
                prefix1 = isInbound ? "入增" : "出增";
                prefix2 = isInbound ? "入普" : "出普";
            }
            else {
                prefix1 = isInbound ?
                        (invoiceType.equals("增值税票") ? "入增" : "入普") :
                        (invoiceType.equals("增值税票") ? "出增" : "出普");
                prefix2 = "x";
            }

            List<InvoiceEntryO> entriesFromDatabase = invoiceEntryMapper.getEntriesInDateRangeAndParams(
                    dateFormat.format(startDate), dateFormat.format(endDate),
                    invoiceNumberDate == null ? null : dateFormat.format(invoiceNumberDate),
                    companyID, isFollowUpIndication, invoiceNumber, invoiceType, prefix1, prefix2);

            for (var entryFromDatabase : entriesFromDatabase) {
                InvoiceEntryStandAloneVO entry = new InvoiceEntryStandAloneVO();
                BeanUtils.copyProperties(entryFromDatabase, entry);

                List<InboundProductO> products = inboundEntryService.getProductsWithInvoiceSerial(
                        entryFromDatabase.getInvoiceEntrySerial());
                entry.setInvoiceProducts(products);
                entries.add(entry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("query failed");
            throw e;
        }

        return entries;
    }

    @Transactional
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
            e.printStackTrace(); // todo remove in production
            logger.error("update failed");
            throw e;
        }
    }

    private boolean compareEntryAndFormModificationRecord(StringBuilder record, InvoiceEntryO originEntry,
                                                          InvoiceEntryO modifiedEntry) {
        boolean bool = false;
        if (!originEntry.getInvoiceNumber().equals(modifiedEntry.getInvoiceNumber())) {
            bool = true;
            record.append(String.format("发票号码: %s -> %s; ", originEntry.getInvoiceNumber(),
                    modifiedEntry.getInvoiceNumber()));
        }
        if (originEntry.getInvoiceAmount() != modifiedEntry.getInvoiceAmount()) {
            bool = true;
            record.append(String.format("开票金额: %f -> %f; ", originEntry.getInvoiceAmount(),
                    modifiedEntry.getInvoiceAmount()));
        }
        if (originEntry.getPartnerCompanyID() != modifiedEntry.getPartnerCompanyID()) {
            bool = true;
            record.append(String.format("单位: %s -> %s; ", originEntry.getCompanyFullName(),
                    modifiedEntry.getCompanyFullName()));
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
    public String createEntryForCheckout(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO, boolean isInbound) {

        InvoiceEntryO invoiceEntry = new InvoiceEntryO();
        BeanUtils.copyProperties(checkoutEntryWithProductsVO.getInvoiceEntry(), invoiceEntry);
        String newSerial;

        try {
            String prefix = isInbound ?
                    (checkoutEntryWithProductsVO.getInvoiceType().equals("增值税票") ? "入增" : "入普") :
                    (checkoutEntryWithProductsVO.getInvoiceType().equals("增值税票") ? "出增" : "出普");

            int count = invoiceEntryMapper.countNumberOfEntriesOfToday(prefix);
            newSerial = MyUtils.formNewSerial(prefix, count, invoiceEntry.getInvoiceDate());

            invoiceEntry.setInvoiceEntrySerial(newSerial);
            invoiceEntry.setCheckoutDate(checkoutEntryWithProductsVO.getCheckoutDate());

            invoiceEntryMapper.insertEntry(invoiceEntry);

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("insert failed");
            throw e;
        }

        return newSerial;
    }
}
