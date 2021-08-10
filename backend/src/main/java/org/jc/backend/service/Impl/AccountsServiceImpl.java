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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.jc.backend.utils.AcceptanceBillClassification.ACCEPTANCE_PAY;
import static org.jc.backend.utils.AcceptanceBillClassification.ACCEPTANCE_RECV;
import static org.jc.backend.utils.EntryClassification.*;

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

            return generateEntryList(companyID, true);

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

            return generateEntryList(companyID, false);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    // balance and debitOrCredit indicator are only calculated when needed, e.g. here

    // 入结 出结
    // 付款 收款
    // 付运 收运
    // 承付 承收
    // 初收 初付

    // 把代表资金去向的叫做「借」一族。资产和费用和成本
    // 把代表资金来源的叫做「贷」一族。负债，股本（狭义的所有者权益）和收入
    //「借」一族增加就是记借方，减少就是记贷方。
    //「贷」一族增加就是记贷方，减少就是记借方。

    // 供应商：supplier
    // 借方 debtor: 出结，付款，付运，承付
    // 贷方 creditor: 入结，收款，收运，承收

    // 客户：customer
    // 借方 debtor: 出结，付款，付运，承付
    // 贷方 creditor: 入结，收款，收运，承收

    private void calculateBalance(int partnerCompanyID, boolean isCustomer) {
        var allEntries = generateEntryList(partnerCompanyID, isCustomer);

        String lastBalance = "";
        boolean isLastDebit = true;
        for (var entry : allEntries) {
            doCalculation(entry, lastBalance, isLastDebit);
            updateEntryDetail(entry);

            lastBalance = entry.getBalance();
            isLastDebit = entry.getDebitOrCredit().equals("借");
        }

    }

    private void updateEntryDetail(MoneyEntryDetailO entry) {
        switch (entry.getEntryID().substring(0,2)) {
            case INITIAL_PAYABLE:
            case INITIAL_RECEIVABLE:
                break;
            case INBOUND_CHECKOUT:
            case OUTBOUND_CHECKOUT:
                checkoutEntryService.updateEntryDetail(entry);
                break;
            case INBOUND_PAYABLE:
            case OUTBOUND_RECEIVABLE:
                moneyEntryService.updateEntryDetail(entry);
                break;
            case SHIPPING_COST_PAY:
            case SHIPPING_COST_RECV:
                shippingCostEntryService.updateEntryDetail(entry);
                break;
            case ACCEPTANCE_RECV:
            case ACCEPTANCE_PAY:
                acceptanceService.updateEntryDetail(entry);
                break;
        }
    }

    private void doCalculation(MoneyEntryDetailO entry, String lastBalance, boolean isDebit) {
        switch (entry.getEntryID().substring(0,2)) {
            case INITIAL_PAYABLE:
            case INITIAL_RECEIVABLE:
                break;
            case INBOUND_CHECKOUT:
            case OUTBOUND_RECEIVABLE:
            case SHIPPING_COST_RECV:
            case ACCEPTANCE_RECV: // todo, draft, not verified
                entry.setDebtorAmount("");
                BigDecimal balance = new BigDecimal(lastBalance);
                if (isDebit) {
                    balance = balance.subtract(new BigDecimal(""));
                }
                else {
                    balance = balance.add(new BigDecimal(""));
                }
                entry.setBalance(balance.toPlainString());
                int r = balance.compareTo(new BigDecimal(0));
                if (r > 0) {
                    entry.setDebitOrCredit("借");
                }
                else if (r < 0) {
                    entry.setDebitOrCredit("贷");
                }
                else {
                    entry.setDebitOrCredit("");
                }
                break;
            case OUTBOUND_CHECKOUT:
            case INBOUND_PAYABLE:
            case SHIPPING_COST_PAY:
            case ACCEPTANCE_PAY:
                break;
        }
    }

    private List<MoneyEntryDetailO> generateEntryList(int companyID, boolean isCustomer) {
        var initialEntries = initialMoneyEntryService.getEntryDetails(companyID, true, isCustomer);

        var inboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, true, isCustomer);
        var outboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, false, isCustomer);

        var inboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, true, isCustomer);
        var outboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, false, isCustomer);

        var inboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, true, isCustomer);
        var outboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, false, isCustomer);

        var inboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, true, isCustomer);
        var outboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, false, isCustomer);

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

        mergeMaps(inboundMap, outboundMap);

        var list = new ArrayList<MoneyEntryDetailO>(inboundMap.size());
        for (var entry : inboundMap.entrySet()) {
            list.addAll(entry.getValue());
        }
        return list;
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
