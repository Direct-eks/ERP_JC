package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RelevantCompanyO {
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
}
