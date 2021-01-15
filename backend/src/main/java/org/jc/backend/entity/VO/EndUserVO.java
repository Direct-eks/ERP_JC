package org.jc.backend.entity.VO;

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

    @NotNull(message = "role null error")
    @NotBlank(message = "role blank error")
    private String role;

    @NotNull(message = "permission null error")
    private List<String> permissions;

    private String sessionID;
}
