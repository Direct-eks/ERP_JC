package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class WarehouseO {
    private int warehouseID;

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
}
