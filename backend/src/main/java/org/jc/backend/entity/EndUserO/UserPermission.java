package org.jc.backend.entity.EndUserO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserPermission {
    private int permissionID;

    @NotNull(message = "permission null error")
    @NotBlank(message = "permission blank error")
    private String permission;
}
