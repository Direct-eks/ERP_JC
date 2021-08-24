package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class ModelO implements ObjectComparison<ModelO> {

    @NotNull(message = "modelID null error")
    private Integer modelID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "categoryID null error")
    @Min(value = 0, message = "categoryID value error")
    private Integer categoryID;
    private String categoryCode;

    @NotNull(message = "unitID null error")
    @Min(value = 0, message = "unitID value error")
    private Integer unitID;
    private String unitName;

    @Override
    public ModelO getOldObject(List<ModelO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.modelID.equals(item.getModelID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(ModelO oldModelO, StringBuilder record) {
        if (oldModelO == this) return true;
        boolean bool = false;

        if (!this.sequenceNumber.equals(oldModelO.getSequenceNumber())) {
            bool = true;
            record.append(String.format("序号：%s -> %s; ", oldModelO.getSequenceNumber(), this.sequenceNumber));
        }
        if (!this.code.equals(oldModelO.getCode())) {
            bool = true;
            record.append(String.format("型号：%s -> %s; ", oldModelO.getCode(), this.code));
        }
        if (!this.categoryID.equals(oldModelO.getCategoryID())) {
            bool = true;
            record.append(String.format("分类：%s -> %s; ", oldModelO.getCategoryID(), this.categoryID));
        }
        if (!this.unitID.equals(oldModelO.getUnitID())) {
            bool = true;
            record.append(String.format("单位：%s -> %s; ", oldModelO.getUnitID(), this.unitID));
        }

        return bool;
    }
}
