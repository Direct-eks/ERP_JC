package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RelevantCompanyCategoryO {
    private Integer categoryID;

    @NotNull(message = "name null error")
    @NotBlank(message = "区划名称不能为空白")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;
}
