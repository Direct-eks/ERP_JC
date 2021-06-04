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
public class DepartmentO {
    private int departmentID;

    @NotNull(message = "name null error")
    @NotBlank(message = "name blank error")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isDefault option null error")
    @Min(value = 0, message = "min value 0 violation")
    @Max(value = 1, message = "max value 1 violation")
    private int isDefault;
}
