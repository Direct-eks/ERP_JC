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

    // brand
    private String factoryBrand;

    // company
    private String abbreviatedName;

    // company by month
}
