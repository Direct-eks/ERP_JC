package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class SkuO implements ObjectComparison<SkuO> {
    private Integer skuID;

    @NotNull(message = "factoryBrandID null error")
    private Integer factoryBrandID;
    // from w_factory_brand
    private String factoryCode;

    @NotNull(message = "modelID null error")
    private Integer modelID;
    // from w_model
    private String code;

    @NotNull(message = "unitID null error")
    private Integer unitID;
    // from w_measurement_unit
    private String unitName;

    @Override
    public SkuO getOldObject(List<SkuO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.skuID.equals(item.getSkuID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(SkuO oldSkuO, StringBuilder record) {
        if (oldSkuO == this) return true;
        boolean bool = false;

        if (!this.factoryBrandID.equals(oldSkuO.getFactoryBrandID())) {
            bool = true;
            record.append(String.format("厂牌：%s -> %s; ", oldSkuO.getFactoryBrandID(), this.factoryBrandID));
        }

        return bool;
    }
}
