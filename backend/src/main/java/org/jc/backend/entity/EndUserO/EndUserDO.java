package org.jc.backend.entity.EndUserO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndUserDO {
    private int userID;
    private String username;
    private String password;
    private String remark;
    private String permittedRoundingAmount;
}
