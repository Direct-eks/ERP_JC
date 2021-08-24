package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class RelevantCompanyO implements ObjectComparison<RelevantCompanyO> {
    @NotNull(message = "companyID null error")
    private Integer companyID;

    @NotNull(message = "abbreviatedName null error")
    @NotNull(message = "abbreviatedName blank error")
    private String abbreviatedName;

    @NotNull(message = "fullName null error")
    private String fullName;

    @NotNull(message = "phone null error")
    private String phone;

    @NotNull(message = "contactPerson null error")
    private String contactPerson;

    @NotNull(message = "contactNumber null error")
    private String contactNumber;

    @NotNull(message = "address null error")
    private String address;

    @NotNull(message = "fax null error")
    private String fax;

    @NotNull(message = "zipcode null error")
    private String zipcode;

    @NotNull(message = "email null error")
    private String email;

    @NotNull(message = "website null error")
    private String website;

    @NotNull(message = "categoryID null error")
    private Integer categoryID;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isActive null error")
    private Integer isActive;

    @Override
    public RelevantCompanyO getOldObject(List<RelevantCompanyO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.companyID.equals(item.getCompanyID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(RelevantCompanyO oldCompanyO, StringBuilder record) {
        if (oldCompanyO == this) return true;
        boolean bool = false;

        if (!this.fullName.equals(oldCompanyO.getFullName())) {
            bool = true;
            record.append(String.format("全称：%s -> %s; ", oldCompanyO.getFullName(), this.fullName));
        }
        if (!this.phone.equals(oldCompanyO.getPhone())) {
            bool = true;
            record.append(String.format("电话：%s -> %s; ", oldCompanyO.getPhone(), this.phone));
        }
        if (!this.contactPerson.equals(oldCompanyO.getContactPerson())) {
            bool = true;
            record.append(String.format("联系人：%s -> %s; ", oldCompanyO.getContactPerson(), this.contactPerson));
        }
        if (!this.contactNumber.equals(oldCompanyO.getContactNumber())) {
            bool = true;
            record.append(String.format("联系电话：%s -> %s; ", oldCompanyO.getContactNumber(), this.contactNumber));
        }
        if (!this.address.equals(oldCompanyO.getAddress())) {
            bool = true;
            record.append(String.format("地址：%s -> %s; ", oldCompanyO.getAddress(), this.address));
        }
        if (!this.fax.equals(oldCompanyO.getFax())) {
            bool = true;
            record.append(String.format("传真：%s -> %s; ", oldCompanyO.getFax(), this.fax));
        }
        if (!this.zipcode.equals(oldCompanyO.getZipcode())) {
            bool = true;
            record.append(String.format("邮编：%s -> %s; ", oldCompanyO.getZipcode(), this.zipcode));
        }
        if (!this.email.equals(oldCompanyO.getEmail())) {
            bool = true;
            record.append(String.format("邮箱：%s -> %s; ", oldCompanyO.getEmail(), this.email));
        }
        if (!this.website.equals(oldCompanyO.getWebsite())) {
            bool = true;
            record.append(String.format("网址：%s -> %s; ", oldCompanyO.getWebsite(), this.website));
        }
        if (!this.categoryID.equals(oldCompanyO.getCategoryID())) {
            bool = true;
            record.append(String.format("分类：%s -> %s; ", oldCompanyO.getCategoryID(), this.categoryID));
        }
        if (!this.remark.equals(oldCompanyO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldCompanyO.getRemark(), this.remark));
        }
        if (!this.isActive.equals(oldCompanyO.getIsActive())) {
            bool = true;
            record.append(String.format("状态：%s -> %s; ", oldCompanyO.getIsActive(), this.isActive));
        }

        return bool;
    }
}
