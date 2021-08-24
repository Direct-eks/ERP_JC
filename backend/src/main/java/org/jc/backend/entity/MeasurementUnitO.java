package org.jc.backend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MeasurementUnitO implements ObjectComparison<MeasurementUnitO> {

    @NotNull(message = "unitID null error")
    private Integer unitID;

    @NotNull(message = "unitName null error")
    @NotBlank(message = "unitName blank error")
    private String unitName;

    @NotNull(message = "remark null error")
    private String remark;

    @Override
    public MeasurementUnitO getOldObject(List<MeasurementUnitO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.unitID.equals(item.getUnitID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(MeasurementUnitO oldUnitO, StringBuilder record) {
        if (oldUnitO == this) return true;
        boolean bool = false;

        if (!this.unitName.equals(oldUnitO.getUnitName())) {
            bool = true;
            record.append(String.format("名称：%s -> %s; ", oldUnitO.getUnitName(), this.unitName));
        }
        if (!this.remark.equals(oldUnitO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldUnitO.getRemark(), this.remark));
        }

        return bool;
    }
}
