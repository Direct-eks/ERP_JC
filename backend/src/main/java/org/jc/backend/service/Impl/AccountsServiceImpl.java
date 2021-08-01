package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.MoneyEntryDetailO;
import org.jc.backend.service.AccountsService;
import org.jc.backend.service.AccountsStatService;
import org.jc.backend.service.CompanyService;
import org.jc.backend.utils.CompanyClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService {
    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final CompanyService companyService;
    private final AccountsStatService initialMoneyEntryService;
    private final AccountsStatService checkoutEntryService;
    private final AccountsStatService moneyEntryService;
    private final AccountsStatService shippingCostEntryService;
    private final AccountsStatService acceptanceService;

    public AccountsServiceImpl(
            CompanyService companyService,
            @Qualifier("initialMoneyEntryServiceImpl") AccountsStatService initialMoneyEntryService,
            @Qualifier("checkoutEntryServiceImpl") AccountsStatService checkoutEntryService,
            @Qualifier("moneyEntryServiceImpl") AccountsStatService moneyEntryService,
            @Qualifier("shippingCostEntryServiceImpl") AccountsStatService shippingCostEntryService,
            @Qualifier("acceptanceServiceImpl") AccountsStatService acceptanceService
    ) {
        this.companyService = companyService;
        this.initialMoneyEntryService = initialMoneyEntryService;
        this.checkoutEntryService = checkoutEntryService;
        this.moneyEntryService = moneyEntryService;
        this.shippingCostEntryService = shippingCostEntryService;
        this.acceptanceService = acceptanceService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getReceivableSummary(int companyID) throws GlobalParamException {
        try {
            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            switch (company.getClassification()) {
                case CompanyClassification.CUSTOMER:
                case CompanyClassification.OTHER_RECV:
                    break;
                default:
                    throw new GlobalParamException("此公司为供应商/其他应付");
            }

            return null; // todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoneyEntryDetailO> getPayableSummary(int companyID) throws GlobalParamException {
        try {
            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            switch (company.getClassification()) {
                case CompanyClassification.SUPPLIER:
                case CompanyClassification.OTHER_PAY:
                    break;
                default:
                    throw new GlobalParamException("此公司为客户/其他应收");
            }

            return null; // todo

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }


}
