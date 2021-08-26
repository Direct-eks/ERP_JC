package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
public class CompanyO implements ObjectComparison<CompanyO> {
    @NotNull(message = "companyID null error")
    private Integer companyID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    private String brandCode;

    @NotNull(message = "abbreviatedName null error")
    @NotBlank(message = "abbreviatedName blank error")
    private String abbreviatedName;

    private Integer areaID;

    @NotNull(message = "phone null error")
    private String phone;

    @NotNull(message = "address null error")
    private String address;

    @NotNull(message = "fax null error")
    private String fax;

    @NotNull(message = "classification null error")
    @Pattern(regexp = "^(客户|供应商|其它应收|其它应付)$",
            message = "客户类别输入错误")
    private String classification;

    @NotNull(message = "fullName null error")
    private String fullName;

    @NotNull(message = "contactPerson null error")
    private String contactPerson;

    @NotNull(message = "contactNumber null error")
    private String contactNumber;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "isActive null error")
    @Min(value = 0, message = "isActive min value violation")
    @Max(value = 1, message = "isActive max value violation")
    private Integer isActive;

    @NotNull(message = "zipcode null error")
    private String zipcode;

    @NotNull(message = "email null error")
    private String email;

    @NotNull(message = "website null error")
    private String website;

    @Override
    public CompanyO getOldObject(List<CompanyO> oldObjectList) {
        for (var item : oldObjectList) {
            if (this.companyID.equals(item.getCompanyID())) {
                return item;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public boolean formModificationRecord(final CompanyO oldCompanyO, final StringBuilder record) {
        if (oldCompanyO == this) return true;
        boolean bool = false;

        if (!this.sequenceNumber.equals(oldCompanyO.getSequenceNumber())) {
            bool = true;
            record.append(String.format("序号：%s -> %s; ", oldCompanyO.getSequenceNumber(), this.sequenceNumber));
        }
        if (!this.brandCode.equals(oldCompanyO.getBrandCode())) {
            bool = true;
            record.append(String.format("厂牌：%s -> %s; ", oldCompanyO.getRemark(), this.remark));
        }
        if (!this.areaID.equals(oldCompanyO.getAreaID())) {
            bool = true;
            record.append(String.format("分类：%s -> %s; ", oldCompanyO.getAreaID(), this.areaID));
        }
        if (!this.phone.equals(oldCompanyO.getPhone())) {
            bool = true;
            record.append(String.format("电话：%s -> %s; ", oldCompanyO.getPhone(), this.phone));
        }
        if (!this.address.equals(oldCompanyO.getAddress())) {
            bool = true;
            record.append(String.format("地址：%s -> %s; ", oldCompanyO.getAddress(), this.address));
        }
        if (!this.fax.equals(oldCompanyO.getFax())) {
            bool = true;
            record.append(String.format("传真：%s -> %s; ", oldCompanyO.getFax(), this.fax));
        }
        if (!this.classification.equals(oldCompanyO.getClassification())) {
            bool = true;
            record.append(String.format("类别：%s -> %s; ", oldCompanyO.getClassification(), this.classification));
        }
        if (!this.fullName.equals(oldCompanyO.getFullName())) {
            bool = true;
            record.append(String.format("全称：%s -> %s; ", oldCompanyO.getFullName(), this.fullName));
        }
        if (!this.contactPerson.equals(oldCompanyO.getContactPerson())) {
            bool = true;
            record.append(String.format("联系人：%s -> %s; ", oldCompanyO.getContactPerson(), this.contactPerson));
        }
        if (!this.contactNumber.equals(oldCompanyO.getContactNumber())) {
            bool = true;
            record.append(String.format("联系电话：%s -> %s; ", oldCompanyO.getContactNumber(), this.contactNumber));
        }
        if (!this.remark.equals(oldCompanyO.getRemark())) {
            bool = true;
            record.append(String.format("说明：%s -> %s; ", oldCompanyO.getRemark(), this.remark));
        }
        if (!this.isActive.equals(oldCompanyO.getIsActive())) {
            bool = true;
            record.append(String.format("状态：%s -> %s; ", oldCompanyO.getIsActive(), this.isActive));
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

        return bool;
    }
}
