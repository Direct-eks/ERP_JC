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
public class WarehouseProductO {
    private Integer warehouseProductID;
    private String warehouseEntryID;

    @NotNull(message = "skuID null error")
    private Integer skuID;
    // from sku_full
    private String code;
    private String unitName;
    private String factoryCode;

    @NotNull(message = "quantity null error")
    @Min(value = 0, message = "商品数量必须大于0")
    private Integer quantity;
    private Integer stockQuantity;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "warehouseStockID null error")
    private Integer warehouseStockID;

    @NotNull(message = "warehouseID null error")
    @Min(value = 0, message = "商品仓库错误")
    private Integer warehouseID;

    @NotNull(message = "unitPrice null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "unitPrice value error")
    private String unitPrice;

    private String stockUnitPrice;
}
