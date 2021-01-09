package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.ShippingCostEntryMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.DO.ShippingCostEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.OutboundProductO;
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

import java.util.Date;
import java.util.List;

@Service
public class ShippingCostEntryServiceImpl implements ShippingCostEntryService {
    private static final Logger logger = LoggerFactory.getLogger(ShippingCostEntryServiceImpl.class);

    private final ShippingCostEntryMapper shippingCostEntryMapper;
    private final InboundEntryService inboundEntryService;
    private final OutboundEntryService outboundEntryService;

    public ShippingCostEntryServiceImpl(ShippingCostEntryMapper shippingCostEntryMapper,
                                        InboundEntryService inboundEntryService,
                                        OutboundEntryService outboundEntryService) {
        this.shippingCostEntryMapper = shippingCostEntryMapper;
        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(ShippingCostEntryVO shippingCostEntryVO, boolean isInbound) {

        ShippingCostEntryDO shippingCostEntryDO = new ShippingCostEntryDO();
        BeanUtils.copyProperties(shippingCostEntryVO, shippingCostEntryDO);

        List<InboundEntryDO> inboundEntries = null;
        List<OutboundEntryDO> outboundEntries = null;
        if (isInbound) {
            inboundEntries = shippingCostEntryVO.getInboundEntries();
        }
        else {
            outboundEntries = shippingCostEntryVO.getOutboundEntries();
        }

        try {
            String prefix = isInbound ? "付运" : "收运";
            int count = shippingCostEntryMapper.countNumberOfEntriesOfToday(prefix);
            String newSerial = MyUtils.formNewSerial(prefix, count, shippingCostEntryDO.getCheckoutDate());

            shippingCostEntryDO.setShippingCostEntrySerial(newSerial);
            shippingCostEntryMapper.insertEntry(shippingCostEntryDO);

            if (isInbound) {
                inboundEntries.forEach(entry -> {
                    entry.setShippingCostSerial(newSerial);
                    inboundEntryService.updateEntryWithShippingCostSerial(entry);
                });
            }
            else {
                outboundEntries.forEach(entry -> {
                    entry.setShippingCostSerial(newSerial);
                    outboundEntryService.updateEntryWithShippingCostSerial(entry);
                });
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<ShippingCostEntryVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                           int companyID, boolean isInbound) {

    }

    @Transactional
    public void modifyEntry(ShippingCostEntryVO shippingCostEntryVO) {

    }
}
