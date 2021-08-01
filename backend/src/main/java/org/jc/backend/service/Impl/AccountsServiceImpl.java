package org.jc.backend.service.Impl;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.MoneyEntryO;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    private void calculateBalance(CheckoutEntryDO entry, boolean isCustomer) {
        var triple = generateEntryMaps(entry.getPartnerCompanyID(), isCustomer);
        var inboundMap = triple.getLeft();
        var outboundMap = triple.getMiddle();
        var entryMap = triple.getRight();

        MoneyEntryDetailO lastEntry;
        List<MoneyEntryDetailO> affectedEntries = new ArrayList<>();
    }

    private boolean isDebit(String debitOrCredit) {
        return debitOrCredit.equals("借");
    }

    private Triple<TreeMap<String, List<MoneyEntryDetailO>>,
            TreeMap<String, List<MoneyEntryDetailO>>,
            TreeMap<String, List<MoneyEntryDetailO>>> generateEntryMaps(int companyID, boolean isCustomer) {
        var initialEntries = initialMoneyEntryService.getEntryDetails(companyID, true);

        var inboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, true);
        var outboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, false);

        var inboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, true);
        var outboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, false);

        var inboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, true);
        var outboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, false);

        var inboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, true);
        var outboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, false);

        var entryMap = new TreeMap<String, List<MoneyEntryDetailO>>();
        var inboundMap = new TreeMap<String, List<MoneyEntryDetailO>>();
        var outboundMap = new TreeMap<String, List<MoneyEntryDetailO>>();

        if (initialEntries.size() != 0) {
            var list = new ArrayList<MoneyEntryDetailO>();
            list.add(initialEntries.get(0));
            if (isCustomer) {
                inboundMap.put(initialEntries.get(0).getEntryID(), list);
            }
            else {
                outboundMap.put(initialEntries.get(0).getEntryID(), list);
            }
        }

        mergeMaps(inboundMap, transformIntoMapAndSort(inboundCheckoutEntries));
        mergeMaps(outboundMap, transformIntoMapAndSort(outboundCheckoutEntries));

        mergeMaps(inboundMap, transformIntoMapAndSort(inboundMoneyEntries));
        mergeMaps(outboundMap, transformIntoMapAndSort(outboundMoneyEntries));

        mergeMaps(inboundMap, transformIntoMapAndSort(inboundShippingCostEntries));
        mergeMaps(outboundMap, transformIntoMapAndSort(outboundShippingCostEntries));

        mergeMaps(inboundMap, transformIntoMapAndSort(inboundAcceptanceEntries));
        mergeMaps(outboundMap, transformIntoMapAndSort(outboundAcceptanceEntries));

        mergeMaps(entryMap, inboundMap);
        mergeMaps(entryMap, outboundMap);

        return Triple.of(inboundMap, outboundMap, entryMap);
    }

    private TreeMap<String, List<MoneyEntryDetailO>> transformIntoMapAndSort(List<MoneyEntryDetailO> list) {
        return list.parallelStream().collect(
                Collectors.groupingBy(MoneyEntryDetailO::getEntryID, TreeMap::new, Collectors.toList()));
    }

    private void mergeMaps(TreeMap<String, List<MoneyEntryDetailO>> parentMap,
                           TreeMap<String, List<MoneyEntryDetailO>> subMap) {
        for (var entry : subMap.entrySet()) {
            entry.getValue().sort(Comparator.comparing(MoneyEntryDetailO::getEntryID));
            String key = entry.getKey();
            var value = entry.getValue();
            if (parentMap.containsKey(key)) {
                parentMap.get(key).addAll(value);
            }
            else {
                parentMap.put(key, value);
            }
        }
    }


}
