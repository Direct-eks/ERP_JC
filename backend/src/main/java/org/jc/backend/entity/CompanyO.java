package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CompanyO {
    private int companyID;
    private int sequenceNumber;
    private String brandCode;

    @NotNull(message = "abbreviatedName null error")
    private String abbreviatedName;

    private int areaID;

    @NotNull(message = "phone null error")
    private String phone;
    private String address;
    private String fax;
    private String classification;
    private String fullName;
    private String contactPerson;
    private String contactNumber;
    private String remark;
    private int isActive;
    private String zipcode;
    private String email;
    private String website;
}
