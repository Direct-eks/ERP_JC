package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class ModelCategoryO implements ObjectComparison<ModelCategoryO> {

    @NotNull(message = "modelCategoryID null error")
    private Integer modelCategoryID;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "name null error")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "treeLevel null error")
    @NotBlank(message = "treeLevel blank error")
    private String treeLevel;

    @Override
    public ModelCategoryO getOldObject(List<ModelCategoryO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.modelCategoryID.equals(item.getModelCategoryID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(ModelCategoryO oldCategory, StringBuilder record) {
        if (oldCategory == this) return true;
        boolean bool = false;

        if (!this.code.equals(oldCategory.getCode())) {
            bool = true;
            record.append(String.format("型号：%s -> %s; ", oldCategory.getCode(), this.code));
        }
        if (!this.name.equals(oldCategory.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldCategory.getName(), this.name));
        }
        if (!this.remark.equals(oldCategory.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldCategory.getRemark(), this.remark));
        }
        if (!this.treeLevel.equals(oldCategory.getTreeLevel())) {
            bool = true;
            record.append(String.format("层级：%s -> %s; ", oldCategory.getTreeLevel(), this.treeLevel));
        }

        return bool;
    }
}
