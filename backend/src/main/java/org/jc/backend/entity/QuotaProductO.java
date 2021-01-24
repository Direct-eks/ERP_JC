package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class QuotaProductO {
    private int quotaProductID;
    private String quotaEntryID;

    private int skuID;
    // from w_model, w_measurement_unit, w_factory_brand
    private String newCode;
    private String oldCode;
    private String unitName;
    private String factoryCode;

    @Min(value = 1, message = "quantity smaller than one error")
    private int quantity;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "taxRate null error")
    @NotBlank(message = "taxRate blank error")
    @Pattern(regexp = "^[\\d]*$", message = "unitPriceWithoutTax value error")
    private String taxRate;

    @NotNull(message = "unitPriceWithoutTax null error")
    @NotBlank(message = "unitPriceWithoutTax blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "unitPriceWithoutTax value error")
    private String unitPriceWithoutTax;

    @NotNull(message = "unitPriceWithTax null error")
    @NotBlank(message = "unitPriceWithTax blank error")
    @Pattern(regexp = "^[\\d]*?\\.?[\\d]*?$", message = "unitPriceWithTax value error")
    private String unitPriceWithTax;
}
