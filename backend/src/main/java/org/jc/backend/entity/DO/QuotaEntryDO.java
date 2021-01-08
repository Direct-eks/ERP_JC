package org.jc.backend.entity.DO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotaEntryDO {
    private String quotaEntryID;
    private String creationDate;
    private double totalAmount;
    private String invoiceType;
    private String drawer;

    private int partnerCompanyID;
    // from c_partner_company
    private String companyAbbreviatedName;
    private String companyFullName;
    private String companyPhone;

    private String remark;
    private int isModified;
}
