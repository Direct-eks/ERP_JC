package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class WarehouseO implements ObjectComparison<WarehouseO> {

    @NotNull(message = "warehouseID null error")
    private Integer warehouseID;

    @NotNull(message = "name null error")
    @NotBlank(message = "name blank error")
    private String name;

    @NotNull(message = "location null error")
    private String location;

    @NotNull(message = "isInDefault option null error")
    @Min(value = 0, message = "isInDefault min value 0 violation")
    @Max(value = 1, message = "isInDefault max value 1 violation")
    private Integer isInDefault;

    @NotNull(message = "isOutDefault option null error")
    @Min(value = 0, message = "isOutDefault min value 0 violation")
    @Max(value = 1, message = "isOutDefault max value 1 violation")
    private Integer isOutDefault;

    @NotNull(message = "permittedCategory null error")
    private Integer permittedCategory;

    @Override
    public WarehouseO getOldObject(List<WarehouseO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.warehouseID.equals(item.getWarehouseID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(WarehouseO oldWarehouseO, StringBuilder record) {
        if (oldWarehouseO == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldWarehouseO.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldWarehouseO.getName(), this.name));
        }
        if (!this.location.equals(oldWarehouseO.getLocation())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldWarehouseO.getLocation(), this.location));
        }
        if (!this.isInDefault.equals(oldWarehouseO.getIsInDefault())) {
            bool = true;
            record.append(String.format("入库默认：%s -> %s; ", oldWarehouseO.getIsInDefault(), this.isInDefault));
        }
        if (!this.isOutDefault.equals(oldWarehouseO.getIsOutDefault())) {
            bool = true;
            record.append(String.format("出库默认：%s -> %s; ", oldWarehouseO.getIsOutDefault(), this.isOutDefault));
        }

        return bool;
    }
}
