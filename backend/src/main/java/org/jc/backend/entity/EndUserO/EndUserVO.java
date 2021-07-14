package org.jc.backend.entity.EndUserO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jc.backend.config.validation.DecimalValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class EndUserVO {

    private int userID;

    @NotNull(message = "username null error")
    @NotBlank(message = "username blank error")
    private String username;

    // not checked here
    private String password;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "role null error")
    @NotBlank(message = "role blank error")
    private String role;

    @NotNull(message = "permission null error")
    @NotEmpty(message = "permissions empty error")
    private List<String> permissions;

    @NotNull(message = "permittedRoundingAmount null error")
    @DecimalValidation(type = DecimalValidation.ValidationTypeEnum.DECIMAL_2,
            message = "抹零值错误")
    private String permittedRoundingAmount;

    private String sessionID;
}
