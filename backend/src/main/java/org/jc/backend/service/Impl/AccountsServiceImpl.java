package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.StatO.AccountsDetailO;
import org.jc.backend.entity.StatO.AccountsLedgerO;
import org.jc.backend.entity.StatO.AccountsSummaryO;
import org.jc.backend.service.AccountsIOEntryStatService;
import org.jc.backend.service.AccountsService;
import org.jc.backend.service.AccountsStatService;
import org.jc.backend.service.CompanyService;
import org.jc.backend.utils.CompanyClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.jc.backend.utils.AcceptanceBillClassification.ACCEPTANCE_PAY;
import static org.jc.backend.utils.AcceptanceBillClassification.ACCEPTANCE_RECV;
import static org.jc.backend.utils.EntryClassification.*;

@Service
public class AccountsServiceImpl implements AccountsService {
    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final CompanyService companyService;

    private final AccountsIOEntryStatService inboundEntryService;
    private final AccountsIOEntryStatService outboundEntryService;

    private final AccountsStatService initialMoneyEntryService;
    private final AccountsStatService checkoutEntryService;
    private final AccountsStatService moneyEntryService;
    private final AccountsStatService shippingCostEntryService;
    private final AccountsStatService acceptanceService;

    public AccountsServiceImpl(
            CompanyService companyService,
            @Qualifier("inboundEntryServiceImpl") AccountsIOEntryStatService inboundEntryService,
            @Qualifier("outboundEntryServiceImpl") AccountsIOEntryStatService outboundEntryService,
            @Lazy @Qualifier("initialMoneyEntryServiceImpl") AccountsStatService initialMoneyEntryService,
            @Lazy @Qualifier("checkoutEntryServiceImpl") AccountsStatService checkoutEntryService,
            @Lazy @Qualifier("moneyEntryServiceImpl") AccountsStatService moneyEntryService,
            @Lazy @Qualifier("shippingCostEntryServiceImpl") AccountsStatService shippingCostEntryService,
            @Lazy @Qualifier("acceptanceServiceImpl") AccountsStatService acceptanceService
    ) {
        this.companyService = companyService;

        this.inboundEntryService = inboundEntryService;
        this.outboundEntryService = outboundEntryService;

        this.initialMoneyEntryService = initialMoneyEntryService;
        this.checkoutEntryService = checkoutEntryService;
        this.moneyEntryService = moneyEntryService;
        this.shippingCostEntryService = shippingCostEntryService;
        this.acceptanceService = acceptanceService;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional(readOnly = true)
    @Override
    public List<AccountsSummaryO> getPayableSummary() {
        try {
            return this.getSummary(true);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsSummaryO> getReceivableSummary() {
        try {
            return this.getSummary(false);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    private List<AccountsSummaryO> getSummary(boolean isPayable) {

        // get payable/receivable summary
        List<AccountsSummaryO> summaries = new ArrayList<>();
        for (var companyID : this.getDistinctCompaniesInAllEntries()) {
            AccountsSummaryO summary = new AccountsSummaryO();

            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            summary.setCompanyID(companyID);
            summary.setCompanyName(company.getAbbreviatedName());

            List<AccountsDetailO> entryList;
            AccountsDetailO lastEntry = null;
            try {
                switch (company.getClassification()) {
                    case CompanyClassification.SUPPLIER:
                    case CompanyClassification.OTHER_PAY:
                        if (!isPayable) continue;
                        entryList = this.getPayableDetail(companyID);
                        lastEntry = entryList.get(entryList.size() - 1);
                        break;
                    case CompanyClassification.CUSTOMER:
                    case CompanyClassification.OTHER_RECV:
                        if (isPayable) continue;
                        entryList = this.getReceivableDetail(companyID);
                        lastEntry = entryList.get(entryList.size() - 1);
                        break;
                }
            } catch (GlobalParamException ignored) {}

            BigDecimal amount;
            assert lastEntry != null;
            if (isPayable) {
                if (lastEntry.getDebitOrCredit().equals("借")) {
                    amount = new BigDecimal(lastEntry.getDebtorAmount());
                    amount = amount.negate();
                }
                else {
                    amount = new BigDecimal(lastEntry.getCreditorAmount());
                }
                summary.setPayableAmount(amount.toPlainString());
            }
            else {
                if (lastEntry.getDebitOrCredit().equals("借")) {
                    amount = new BigDecimal(lastEntry.getDebtorAmount());
                }
                else {
                    amount = new BigDecimal(lastEntry.getCreditorAmount());
                    amount = amount.negate();
                }
                summary.setReceivableAmount(amount.toPlainString());
            }

            // get not checkout summary
            var notCheckoutInboundAmount = new BigDecimal(inboundEntryService.getNotCheckoutEntrySummary(companyID));
            var notCheckoutOutboundAmount = new BigDecimal(outboundEntryService.getNotCheckoutEntrySummary(companyID));
            BigDecimal notCheckoutAmount;
            if (isPayable) {
                notCheckoutAmount = notCheckoutInboundAmount.subtract(notCheckoutOutboundAmount);
            }
            else {
                notCheckoutAmount = notCheckoutOutboundAmount.subtract(notCheckoutInboundAmount);
            }
            summary.setNotCheckoutAmount(notCheckoutAmount.toPlainString());
            summary.setSubtotal(amount.add(notCheckoutAmount).toPlainString());

            summaries.add(summary);
        }

        return summaries;
    }

    private Set<Integer> getDistinctCompaniesInAllEntries() {
        Set<Integer> distinctCompanies = new HashSet<>();

        distinctCompanies.addAll(initialMoneyEntryService.getDistinctCompaniesInvolvedInEntries());
        distinctCompanies.addAll(checkoutEntryService.getDistinctCompaniesInvolvedInEntries());
        distinctCompanies.addAll(moneyEntryService.getDistinctCompaniesInvolvedInEntries());
        distinctCompanies.addAll(shippingCostEntryService.getDistinctCompaniesInvolvedInEntries());
        distinctCompanies.addAll(acceptanceService.getDistinctCompaniesInvolvedInEntries());

        return distinctCompanies;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsDetailO> getReceivableDetail(int companyID) throws GlobalParamException {
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

            return generateEntryList(companyID);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsDetailO> getPayableDetail(int companyID) throws GlobalParamException {
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

            return generateEntryList(companyID);

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
    // 客户：customer
    // 借方 debtor: 出结，付款，付运，承付
    // 贷方 creditor: 入结，收款，收运，承收

    @Transactional
    @Override
    public void calculateBalance(int partnerCompanyID) {
        var allEntries = generateEntryList(partnerCompanyID);

        String lastBalance = "";
        boolean isLastDebit = true;
        for (var entry : allEntries) {
            doCalculation(entry, lastBalance, isLastDebit);
            updateEntryDetail(entry);

            lastBalance = entry.getBalance();
            isLastDebit = entry.getDebitOrCredit().equals("借");
        }

    }

    private void doCalculation(AccountsDetailO entry, String lastBalance, boolean isLastDebit) {
        BigDecimal balance = new BigDecimal(lastBalance);

        switch (entry.getEntryID().substring(0,2)) {
            case INITIAL_PAYABLE:
            case INITIAL_RECEIVABLE:
                return; // todo
            case OUTBOUND_CHECKOUT:
            case INBOUND_PAYABLE:
            case SHIPPING_COST_PAY:
            case ACCEPTANCE_PAY: // debtor
                if (isLastDebit) {
                    balance = balance.subtract(new BigDecimal(entry.getDebtorAmount()));
                }
                else {
                    balance = balance.add(new BigDecimal(entry.getDebtorAmount()));
                }
                break;
            case INBOUND_CHECKOUT:
            case OUTBOUND_RECEIVABLE:
            case SHIPPING_COST_RECV:
            case ACCEPTANCE_RECV: // creditor
                if (isLastDebit) {
                    balance = balance.subtract(new BigDecimal(entry.getCreditorAmount()));
                }
                else {
                    balance = balance.add(new BigDecimal(entry.getCreditorAmount()));
                }
                break;
        }
        int r = balance.compareTo(BigDecimal.ZERO);
        if (r > 0) {
            entry.setDebitOrCredit("借");
            entry.setBalance(balance.toPlainString());
        }
        else if (r < 0) {
            entry.setDebitOrCredit("贷");
            balance = balance.negate();
            entry.setBalance(balance.toPlainString());
        }
        else {
            entry.setDebitOrCredit("");
            entry.setBalance("0");
        }
    }

    private void updateEntryDetail(AccountsDetailO entry) {
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

    private List<AccountsDetailO> generateEntryList(int companyID) {
        var initialEntries = initialMoneyEntryService.getEntryDetails(companyID, true);

        var inboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, true);
        var outboundCheckoutEntries = checkoutEntryService.getEntryDetails(companyID, false);

        var inboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, true);
        var outboundMoneyEntries = moneyEntryService.getEntryDetails(companyID, false);

        var inboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, true);
        var outboundShippingCostEntries = shippingCostEntryService.getEntryDetails(companyID, false);

        var inboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, true);
        var outboundAcceptanceEntries = acceptanceService.getEntryDetails(companyID, false);

        var entryMap = new TreeMap<String, List<AccountsDetailO>>();

        if (initialEntries.size() != 0) {
            var list = new ArrayList<AccountsDetailO>();
            list.add(initialEntries.get(0));
            entryMap.put(initialEntries.get(0).getEntryID(), list);
        }

        mergeMapsAndSort(entryMap, transformIntoMap(inboundCheckoutEntries));
        mergeMapsAndSort(entryMap, transformIntoMap(outboundCheckoutEntries));

        mergeMapsAndSort(entryMap, transformIntoMap(inboundMoneyEntries));
        mergeMapsAndSort(entryMap, transformIntoMap(outboundMoneyEntries));

        mergeMapsAndSort(entryMap, transformIntoMap(inboundShippingCostEntries));
        mergeMapsAndSort(entryMap, transformIntoMap(outboundShippingCostEntries));

        mergeMapsAndSort(entryMap, transformIntoMap(inboundAcceptanceEntries));
        mergeMapsAndSort(entryMap, transformIntoMap(outboundAcceptanceEntries));

        var list = new ArrayList<AccountsDetailO>(entryMap.size());
        for (var entry : entryMap.entrySet()) {
            list.addAll(entry.getValue());
        }
        return list;
    }

    private TreeMap<String, List<AccountsDetailO>> transformIntoMap(List<AccountsDetailO> list) {
        return list.parallelStream().collect(
                Collectors.groupingBy(AccountsDetailO::getEntryDate, TreeMap::new, Collectors.toList()));
    }

    private void mergeMapsAndSort(TreeMap<String, List<AccountsDetailO>> parentMap,
                                  TreeMap<String, List<AccountsDetailO>> subMap) {
        for (var entry : subMap.entrySet()) {
            String key = entry.getKey();
            var value = entry.getValue();
            value.sort(Comparator.comparing(AccountsDetailO::getEntryID));
            value.sort(Comparator.comparing(AccountsDetailO::getEntryDate));
            if (parentMap.containsKey(key)) {
                parentMap.get(key).addAll(value);
            }
            else {
                parentMap.put(key, value);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsLedgerO> getPayableLedger() {
        try {
            return this.getLedger(true);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountsLedgerO> getReceivableLedger() {
        try {
            return this.getLedger(false);

        } catch (PersistenceException e) {
            if (logger.isDebugEnabled()) e.printStackTrace();
            logger.error("Query failed");
            throw e;
        }
    }

    private List<AccountsLedgerO> getLedger(boolean isPayable) {
        List<AccountsLedgerO> allLedgers = new ArrayList<>();

        for (var companyID : this.getDistinctCompaniesInAllEntries()) {
            AccountsLedgerO ledger = new AccountsLedgerO();

            var company = companyService.getCompanyByID(companyID);
            assert company != null;

            ledger.setCompanyID(companyID);
            ledger.setCompanyAbbreviatedName(company.getAbbreviatedName());
            ledger.setCompanyFullName(company.getFullName());

            switch (company.getClassification()) {
                case CompanyClassification.SUPPLIER:
                case CompanyClassification.OTHER_PAY:
                    if (!isPayable) continue;
                    // todo
                    break;
                case CompanyClassification.CUSTOMER:
                case CompanyClassification.OTHER_RECV:
                    if (isPayable) continue;
                    // todo
                    break;
            }

            allLedgers.add(ledger);
        }

        return allLedgers;
    }
}
