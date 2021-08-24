package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class FeeCategoryO implements ObjectComparison<FeeCategoryO> {

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

    @Override
    public FeeCategoryO getOldObject(List<FeeCategoryO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.feeCategoryID.equals(item.getFeeCategoryID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(FeeCategoryO oldCategoryO, StringBuilder record) {
        if (oldCategoryO == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldCategoryO.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldCategoryO.getName(), this.name));
        }
        if (!this.remark.equals(oldCategoryO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldCategoryO.getRemark(), this.remark));
        }
        if (!this.classification.equals(oldCategoryO.getClassification())) {
            bool = true;
            record.append(String.format("类别：%s -> %s; ", oldCategoryO.getClassification(), this.classification));
        }
        if (!this.treeLevel.equals(oldCategoryO.getTreeLevel())) {
            bool = true;
            record.append(String.format("层级：%s -> %s; ", oldCategoryO.getTreeLevel(), this.treeLevel));
        }

        return bool;
    }
}
