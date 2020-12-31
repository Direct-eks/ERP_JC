package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.CheckoutEntryMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.InboundProductO;
import org.jc.backend.entity.MoneyEntryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.CheckoutEntryService;
import org.jc.backend.service.InboundEntryService;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.service.MoneyEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckoutEntryServiceImpl implements CheckoutEntryService {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutEntryServiceImpl.class);

    private final CheckoutEntryMapper checkoutEntryMapper;
    private final MoneyEntryService moneyEntryService;
    private final InboundEntryService inboundEntryService;
    private final InvoiceEntryService invoiceEntryService;

    public CheckoutEntryServiceImpl(CheckoutEntryMapper checkoutEntryMapper,
                                    MoneyEntryService moneyEntryService,
                                    InboundEntryService inboundEntryService,
                                    InvoiceEntryService invoiceEntryService) {
        this.checkoutEntryMapper = checkoutEntryMapper;
        this.moneyEntryService = moneyEntryService;
        this.inboundEntryService = inboundEntryService;
        this.invoiceEntryService = invoiceEntryService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO) {

        try {
            CheckoutEntryDO checkoutEntry = new CheckoutEntryDO();
            BeanUtils.copyProperties(checkoutEntryWithProductsVO, checkoutEntry);

            int count = checkoutEntryMapper.countNumberOfEntriesOfToday("入结");
            String newCheckoutSerial = MyUtils.formNewSerial("入结", count);
            checkoutEntry.setCheckoutEntrySerial(newCheckoutSerial);

            //first create a new moneyEntry
            String newMoneySerial = moneyEntryService.createEntryForCheckout(newCheckoutSerial, checkoutEntry);

            //then create a new checkoutEntry
            checkoutEntry.setMoneyEntrySerial(newMoneySerial);
            checkoutEntryMapper.insertEntry(checkoutEntry);

            //update product checkoutSerial
            inboundEntryService.updateProductsWithCheckoutSerial(
                    checkoutEntryWithProductsVO.getCheckoutProducts(), newCheckoutSerial);

            //check if it is needed to create invoiceEntry
            if (checkoutEntryWithProductsVO.getInvoiceEntry() != null) {
                invoiceEntryService.createEntryForCheckout(checkoutEntryWithProductsVO, true);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }
}
