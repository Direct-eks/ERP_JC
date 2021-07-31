package org.jc.backend.utils;

public class AcceptanceBillClassification {
    // recv classification
    public final static String HOLD = "持有";
    public final static String TRANSFERRED = "已付";
    public final static String SOLUTION_PAY = "解汇";
    // pay classification
    public final static String SELF_PAY = "本付";
    public final static String TRANSFER_PAY = "外付";
    // solution pay / self pay classification
    public final static String NORMAL = "正常";
    // entry
    public final static String ACCEPTANCE_RECV = "承收";
    public final static String ACCEPTANCE_PAY = "承付";
    public final static String ACCEPTANCE_SOLUTION = "承解";
    public final static String ACCEPTANCE_PROMISSORY = "承款";
}
