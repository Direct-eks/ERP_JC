package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class FeeCategoryO {
    @NotNull(message = "feeCategoryID null error")
    private Integer feeCategoryID;

    @NotNull(message = "name null error")
    @NotBlank(message = "name blank error")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "classification null error")
    @Pattern(regexp = "^(收入|支出)$",
            message = "classification value error")
    private String classification;

    @NotNull(message = "treeLevel null error")
    @NotBlank(message = "treeLevel blank error")
    private String treeLevel;
}
