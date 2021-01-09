package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.ShippingCostEntryMapper;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.VO.ShippingCostEntryVO;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.service.ShippingCostEntryService;
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
public class ShippingCostEntryServiceImpl implements ShippingCostEntryService {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryServiceImpl.class);

    private final ShippingCostEntryMapper shippingCostEntryMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;
    private final ModificationMapper modificationMapper;

    public ShippingCostEntryServiceImpl(ShippingCostEntryMapper shippingCostEntryMapper,
                                        InboundEntryService inboundEntryService,
                                        OutboundEntryService outboundEntryService,
                                        ModificationMapper modificationMapper) {
        this.shippingCostEntryMapper = shippingCostEntryMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(ShippingCostEntryVO shippingCostEntryVO, boolean isInbound) {

        ShippingCostEntryDO shippingCostEntryDO = new ShippingCostEntryDO();
        BeanUtils.copyProperties(shippingCostEntryVO, shippingCostEntryDO);

        List<InboundEntryDO> inboundEntries = shippingCostEntryVO.getInboundEntries();
        List<OutboundEntryDO> outboundEntries = shippingCostEntryVO.getOutboundEntries();

        try {
            String prefix = isInbound ? "付运" : "收运";
            int count = shippingCostEntryMapper.countNumberOfEntriesOfToday(prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, shippingCostEntryDO.getCheckoutDate());

            shippingCostEntryDO.setShippingCostEntrySerial(newSerial);
            shippingCostEntryMapper.insertEntry(shippingCostEntryDO);

            inboundEntries.forEach(entry -> {
                entry.setShippingCostSerial(newSerial);
                inboundEntryService.updateEntryWithShippingCostSerial(entry);
            });

            outboundEntries.forEach(entry -> {
                entry.setShippingCostSerial(newSerial);
                outboundEntryService.updateEntryWithShippingCostSerial(entry);
            });

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<ShippingCostEntryVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                           int companyID, boolean isInbound) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<ShippingCostEntryVO> entries = new ArrayList<>();
        try {
            String prefix = isInbound ? "付运" : "收运";

            List<ShippingCostEntryDO> entriesFromDatabase = shippingCostEntryMapper.getEntriesInDateRangeAndParams(
                    dateFormat.format(startDate), dateFormat.format(endDate), companyID, prefix);

            for (var entryFromDatabase : entriesFromDatabase) {
                ShippingCostEntryVO tempEntry = new ShippingCostEntryVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                String serial = tempEntry.getShippingCostEntrySerial();

                List<InboundEntryDO> inboundEntries = inboundEntryService.getEntriesWithShippingCostSerial(serial);
                tempEntry.setInboundEntries(inboundEntries);

                List<OutboundEntryDO> outboundEntries = outboundEntryService.getEntriesWithShippingCostSerial(serial);
                tempEntry.setOutboundEntries(outboundEntries);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("query failed");
            throw e;
        }

        return entries;
    }

    @Transactional
    public void modifyEntry(ShippingCostEntryVO shippingCostEntryVO) {

        ShippingCostEntryDO modifiedEntry = new ShippingCostEntryDO();
        BeanUtils.copyProperties(shippingCostEntryVO, modifiedEntry);

        List<InboundEntryDO> modifiedInboundEntries = shippingCostEntryVO.getInboundEntries();
        List<OutboundEntryDO> modifiedOutboundEntries = shippingCostEntryVO.getOutboundEntries();

        String serial = modifiedEntry.getShippingCostEntrySerial();
        try {
            ShippingCostEntryDO originEntry = shippingCostEntryMapper.selectEntryBySerialForCompare(serial);

            List<InboundEntryDO> originInboundEntries = inboundEntryService.getEntriesWithShippingCostSerial(serial);
            List<OutboundEntryDO> originOutboundEntries = outboundEntryService.getEntriesWithShippingCostSerial(serial);

            StringBuilder record = new StringBuilder("修改者: " + modifiedEntry.getDrawer() + "; ");
            boolean bool1 = compareEntryAndFormModificationRecord(record, modifiedEntry, originEntry);

            boolean bool2 = false;
            // check for added inbound entry
            for (var modifiedInboundEntry : modifiedInboundEntries) {
                boolean found = false;
                for (var originInboundEntry : originInboundEntries) {
                    if (originInboundEntry.getInboundEntryID().equals(modifiedInboundEntry.getInboundEntryID())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bool2 = true;
                    record.append(String.format("新增: %s; ", modifiedInboundEntry.getInboundEntryID()));
                    modifiedInboundEntry.setShippingCostSerial(serial);
                    inboundEntryService.updateEntryWithShippingCostSerial(modifiedInboundEntry);
                }
            }
            // check for removed inbound entry
            for (var originInboundEntry : originInboundEntries) {
                boolean found = false;
                for (var modifiedInboundEntry : modifiedInboundEntries) {
                    if (modifiedInboundEntry.getInboundEntryID().equals(originInboundEntry.getInboundEntryID())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bool2 = true;
                    record.append(String.format("移除: %s; ", originInboundEntry.getInboundEntryID()));
                    originInboundEntry.setShippingCostSerial("");
                    inboundEntryService.updateEntryWithShippingCostSerial(originInboundEntry);
                }
            }

            // check for added outbound entry
            for (var modifiedOutboundEntry : modifiedOutboundEntries) {
                boolean found = false;
                for (var originOutboundEntry : originOutboundEntries) {
                    if (originOutboundEntry.getOutboundEntryID().equals(modifiedOutboundEntry.getOutboundEntryID())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bool2 = true;
                    record.append(String.format("增加: %s; ", modifiedOutboundEntry.getOutboundEntryID()));
                    modifiedOutboundEntry.setShippingCostSerial(serial);
                    outboundEntryService.updateEntryWithShippingCostSerial(modifiedOutboundEntry);
                }
            }
            // check for removed outbound entry
            for (var originOutboundEntry : originOutboundEntries) {
                boolean found = false;
                for (var modifiedOutboundEntry : modifiedOutboundEntries) {
                    if (modifiedOutboundEntry.getOutboundEntryID().equals(originOutboundEntry.getOutboundEntryID())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bool2 = true;
                    record.append(String.format("移除: %s; ", originOutboundEntry.getOutboundEntryID()));
                    originOutboundEntry.setShippingCostSerial("");
                    outboundEntryService.updateEntryWithShippingCostSerial(originOutboundEntry);
                }
            }

            if (bool1 || bool2) {
                shippingCostEntryMapper.modifyEntry(modifiedEntry);

                modificationMapper.insertModificationRecord(new ModificationO(
                        modifiedEntry.getShippingCostEntrySerial(), record.toString()));
            }
            else {
                logger.warn("nothing changed");
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("update failed");
            throw e;
        }

    }

    private boolean compareEntryAndFormModificationRecord(StringBuilder record, ShippingCostEntryDO modifiedEntry,
                                                          ShippingCostEntryDO originEntry) {
        boolean bool = false;
        if (modifiedEntry.getPartnerCompanyID() != originEntry.getPartnerCompanyID()) {
            bool = true;
            record.append(String.format("单位: %s -> %s; ", originEntry.getCompanyAbbreviatedName(),
                    modifiedEntry.getCompanyAbbreviatedName()));
        }
        if (modifiedEntry.getIsTaxDeduction() != originEntry.getIsTaxDeduction()) {
            bool = true;
            record.append(String.format("抵税标志: %d -> %d; ", originEntry.getIsTaxDeduction(),
                    modifiedEntry.getIsTaxDeduction()));
        }
        if (modifiedEntry.getTotalAmount() != originEntry.getTotalAmount()) {
            bool = true;
            record.append(String.format("总运费金额: %f -> %f; ", originEntry.getTotalAmount(),
                    modifiedEntry.getTotalAmount()));
        }
        if (!modifiedEntry.getInvoiceNumber().equals(originEntry.getInvoiceNumber())) {
            bool = true;
            record.append(String.format("发票号码: %s -> %s; ", originEntry.getInvoiceNumber(),
                    modifiedEntry.getInvoiceNumber()));
        }
        if (modifiedEntry.getInvoiceAmount() != originEntry.getInvoiceAmount()) {
            bool = true;
            record.append(String.format("开票金额: %f -> %f; ", originEntry.getInvoiceAmount(),
                    modifiedEntry.getInvoiceAmount()));
        }
        if (!modifiedEntry.getInvoiceDate().equals(originEntry.getInvoiceDate())) {
            bool = true;
            record.append(String.format("开票日期: %s -> %s; ", originEntry.getInvoiceDate(),
                    modifiedEntry.getInvoiceDate()));
        }
        if (!modifiedEntry.getShippingPaymentType().equals(originEntry.getShippingPaymentType())) {
            bool = true;
            record.append(String.format("运费标志: %s -> %s; ", originEntry.getShippingPaymentType(),
                    modifiedEntry.getShippingPaymentType()));
        }
        if (!modifiedEntry.getRemark().equals(originEntry.getRemark())) {
            bool = true;
            record.append(String.format("备注: %s -> %s;", originEntry.getRemark(), modifiedEntry.getRemark()));
        }

        return bool;
    }

}
