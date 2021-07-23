package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutboundSpecialSummaryO {
    private String totalPrice;
    private String percentage;

    // by category
    private String categoryCode;
    private String categoryName;

    // brand
    private String factoryBrand;

    // company
    private String abbreviatedName;

    // company by month
    private int quantity;
    private String jan;
    private String feb;
    private String mar;
    private String apr;
    private String may;
    private String jun;
    private String jul;
    private String aug;
    private String sep;
    private String oct;
    private String nov;
    private String dec;
}
