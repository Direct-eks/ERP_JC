package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkuWithFactoryBrandO {
    private int skuID;
    private int factoryBrandID;
    // from w_factory_brand
    private String factoryCode;
    private int modelID;
    private int stockQuantity;
    private String remark;
    private int supplierID;
}
