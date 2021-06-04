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
    @NotBlank(message = "location blank error")
    private String location;

    @NotNull(message = "isDefault option null error")
    @Min(value = 0, message = "min value 0 violation")
    @Max(value = 1, message = "max value 1 violation")
    private Integer isDefault;
}
