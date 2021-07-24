package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WarehouseStockO {

    @NotNull(message = "warehouseStockID null error")
    @Min(value = 0, message = "warehouseStockID value error")
    private Integer warehouseStockID;
    private int skuID;
    private int warehouseID;
    //from w_warehouse
    private String warehouseName;

    private int stockQuantity;
    private String stockUnitPriceWithoutTax;
    private String remark;
    private int storagePlaceID;

    @NotNull(message = "initialStockQuantity null error")
    @Min(value = 0, message = "期初库存数量错误")
    private Integer initialStockQuantity;

    @NotNull(message = "initialStockUnitPrice null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_8,
            message = "期初库存成本价格式错误")
    private String initialStockUnitPrice;
}
