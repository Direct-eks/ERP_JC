package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SkuO {
    private Integer skuID;

    @NotNull(message = "factoryBrandID null error")
    private Integer factoryBrandID;
    // from w_factory_brand
    private String factoryCode;

    @NotNull(message = "modelID null error")
    private Integer modelID;
    // from w_model
    private String code;

    @NotNull(message = "unitID null error")
    private Integer unitID;
    // from w_measurement_unit
    private String unitName;
}
