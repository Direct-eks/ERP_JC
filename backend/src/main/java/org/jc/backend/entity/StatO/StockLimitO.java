package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockLimitO {
    private int skuID;
    private String categoryCode;
    private String code;
    private String unitName;
    private int stockQuantity;
    private int lowerLimit;
    private int upperLimit;
    private int diff;
}
