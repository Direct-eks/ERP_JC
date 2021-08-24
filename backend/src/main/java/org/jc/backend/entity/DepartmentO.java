package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class DepartmentO implements ObjectComparison<DepartmentO> {

    @NotNull(message = "departmentID null error")
    private Integer departmentID;

    @NotNull(message = "name null error")
    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isDefault option null error")
    @Min(value = 0, message = "isDefault min value error")
    @Max(value = 1, message = "isDefault max value error")
    private Integer isDefault;

    @Override
    public DepartmentO getOldObject(List<DepartmentO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.departmentID.equals(item.getDepartmentID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(DepartmentO oldDepartmentO, StringBuilder record) {
        if (oldDepartmentO == this) return true;
        boolean bool = false;

        if (!this.name.equals(oldDepartmentO.getName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldDepartmentO.getName(), this.name));
        }
        if (!this.remark.equals(oldDepartmentO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldDepartmentO.getRemark(), this.remark));
        }
        if (!this.isDefault.equals(oldDepartmentO.getIsDefault())) {
            bool = true;
            record.append(String.format("默认：%s -> %s; ", oldDepartmentO.getIsDefault(), this.isDefault));
        }

        return bool;
    }
}
