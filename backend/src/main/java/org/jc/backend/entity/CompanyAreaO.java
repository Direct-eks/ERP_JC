package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CompanyAreaO {
    @NotNull(message = "areaID null error")
    private Integer areaID;

    @NotNull(message = "name null error")
    @NotBlank(message = "name blank error")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "treeLevel null error")
    @NotBlank(message = "treeLevel blank error")
    private String treeLevel;
}
