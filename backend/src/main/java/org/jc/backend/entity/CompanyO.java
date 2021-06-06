package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CompanyO {
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
}
