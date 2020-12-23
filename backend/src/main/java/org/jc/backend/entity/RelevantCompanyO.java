package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelevantCompanyO {
    private int companyID;
    private String abbreviatedName;
    private String fullName;
    private String phone;
    private String contactPerson;
    private String contactNumber;
    private String address;
    private String fax;
    private String zipcode;
    private String email;
    private String website;
    private int categoryID;
    private String remark;
    private int isActive;
}
