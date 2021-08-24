package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SupplierO implements ObjectComparison<SupplierO> {

    @NotNull(message = "supplierID null error")
    private Integer supplierID;
    // from c_partner_company
    private String supplierAbbreviatedName;
    private String supplierFullName;
    private String supplierPhone;

    private String remark;

    @Override
    public SupplierO getOldObject(List<SupplierO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.supplierID.equals(item.getSupplierID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean formModificationRecord(SupplierO oldSupplierO, StringBuilder record) {
        if (oldSupplierO == this) return true;
        boolean bool = false;

        if (!this.remark.equals(oldSupplierO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldSupplierO.getRemark(), this.remark));
        }

        return bool;
    }
}
