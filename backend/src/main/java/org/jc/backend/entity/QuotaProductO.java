package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class QuotaProductO {
    private int quotaProductID;
    private String quotaEntryID;

    @NotNull(message = "skuID null error")
    private Integer skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String code;
    private String unitName;
    private String factoryCode;

    @NotNull(message = "quantity null error")
    @Min(value = 1, message = "报价单商品数量必须大于0")
    private Integer quantity;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "taxRate null error")
    @Min(value = 0, message = "报价单商品税率错误")
    private Integer taxRate;

    @NotNull(message = "unitPriceWithoutTax null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "unitPriceWithoutTax value error")
    private String unitPriceWithoutTax;

    @NotNull(message = "unitPriceWithTax null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "unitPriceWithTax value error")
    private String unitPriceWithTax;
}
