package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class RelevantCompanyCategoryO implements ObjectComparison<RelevantCompanyCategoryO> {
    private Integer categoryID;

    @NotNull(message = "name null error")
    @NotBlank(message = "区划名称不能为空白")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @Override
    public RelevantCompanyCategoryO getOldObject(List<RelevantCompanyCategoryO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.categoryID.equals(item.getCategoryID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(RelevantCompanyCategoryO oldCategory, StringBuilder record) {
        if (oldCategory == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldCategory.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldCategory.getName(), this.name));
        }
        if (!this.remark.equals(oldCategory.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldCategory.getRemark(), this.remark));
        }

        return bool;
    }
}
