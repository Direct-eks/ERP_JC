package org.jc.backend.entity.EndUserO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class EndUserVO {

    private int userID;

    @NotNull(message = "username null error")
    @NotBlank(message = "username blank error")
    private String username;

    @NotNull(message = "password null error")
    @NotBlank(message = "password blank error")
    private String password;

    @NotNull(message = "remark null error")
    private String remark;

    @NotNull(message = "role null error")
    @NotBlank(message = "role blank error")
    private UserRole role;

    @NotNull(message = "permission null error")
    private List<UserPermission> permissions;

    private String sessionID;
}
