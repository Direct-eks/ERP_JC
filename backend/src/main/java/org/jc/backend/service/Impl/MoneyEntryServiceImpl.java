package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.MoneyEntryMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.MoneyEntryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.MoneyEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoneyEntryServiceImpl implements MoneyEntryService {
    private static final Logger logger = LoggerFactory.getLogger(MoneyEntryServiceImpl.class);

    private final MoneyEntryMapper moneyEntryMapper;

    public MoneyEntryServiceImpl(MoneyEntryMapper moneyEntryMapper) {
        this.moneyEntryMapper = moneyEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public String createEntryForCheckout(String checkoutSerial, CheckoutEntryDO checkoutEntry) {

        int count = moneyEntryMapper.countNumberOfEntriesOfToday("付款");
        String newMoneySerial = MyUtils.formNewSerial("付款", count);

        MoneyEntryO moneyEntryO = new MoneyEntryO();

        moneyEntryO.setCheckoutSerial(checkoutSerial);
        moneyEntryO.setMoneyEntrySerial(newMoneySerial);

        moneyEntryO.setPartnerCompanyID(checkoutEntry.getPartnerCompanyID());
        moneyEntryO.setInvoiceIndication("正常");
        moneyEntryO.setPaymentMethod(checkoutEntry.getPaymentMethod());
        moneyEntryO.setPaymentNumber(checkoutEntry.getPaymentNumber());
        moneyEntryO.setPaymentAmount(checkoutEntry.getPaymentAmount());
        moneyEntryO.setBankAccountID(checkoutEntry.getBankAccountID());
        moneyEntryO.setDrawer(checkoutEntry.getDrawer());
        moneyEntryO.setPaymentDate(checkoutEntry.getCheckoutDate());
        moneyEntryO.setCheckoutSerial(checkoutEntry.getCheckoutEntrySerial());
        moneyEntryO.setDepartmentID(checkoutEntry.getDepartmentID());

        try {
            moneyEntryMapper.insertEntry(moneyEntryO);
        }
        catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert error");
            throw e;
        }

        return newMoneySerial;
    }
}
