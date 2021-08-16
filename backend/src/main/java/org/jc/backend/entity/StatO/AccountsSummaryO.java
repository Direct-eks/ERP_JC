package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountsSummaryO {
    private int companyID;
    private String companyName;

    private String receivableAmount;
    private String payableAmount;

    private String notCheckoutAmount;

    private String subtotal;
}
