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
public class FactoryBrandO implements ObjectComparison<FactoryBrandO> {

    @NotNull(message = "factoryBrandID null error")
    private Integer factoryBrandID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "remark null error")
    private String remark;

    @Override
    public FactoryBrandO getOldObject(List<FactoryBrandO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.factoryBrandID.equals(item.getFactoryBrandID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(FactoryBrandO oldBrandO, StringBuilder record) {
        if (oldBrandO == this) return true;
        boolean bool = false;

        if (!this.sequenceNumber.equals(oldBrandO.getSequenceNumber())) {
            bool = true;
            record.append(String.format("序号：%s -> %s; ", oldBrandO.getSequenceNumber(), this.sequenceNumber));
        }
        if (!this.code.equals(oldBrandO.getCode())) {
            bool = true;
            record.append(String.format("厂牌：%s -> %s; ", oldBrandO.getCode(), this.code));
        }
        if (!this.remark.equals(oldBrandO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldBrandO.getRemark(), this.remark));
        }

        return bool;
    }
}
