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
public class CompanyAreaO implements ObjectComparison<CompanyAreaO> {
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

    @Override
    public CompanyAreaO getOldObject(List<CompanyAreaO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.areaID.equals(item.getAreaID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(final CompanyAreaO oldAreaO, final StringBuilder record) {
        if (oldAreaO == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldAreaO.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldAreaO.getName(), this.name));
        }
        if (!this.remark.equals(oldAreaO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldAreaO.getRemark(), this.remark));
        }
        if (!this.treeLevel.equals(oldAreaO.getTreeLevel())) {
            bool = true;
            record.append(String.format("层级：%s -> %s; ", oldAreaO.getTreeLevel(), this.treeLevel));
        }

        return bool;
    }
}
