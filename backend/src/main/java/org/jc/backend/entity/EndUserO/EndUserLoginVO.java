package org.jc.backend.entity.EndUserO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EndUserLoginVO {
    @NotNull(message = "username null error")
    @NotBlank(message = "username blank error")
    private String username;

    @NotNull(message = "password null error")
    @NotBlank(message = "password blank error")
    private String password;
}
