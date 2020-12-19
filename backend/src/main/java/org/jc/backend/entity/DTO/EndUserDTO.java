package org.jc.backend.entity.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EndUserDTO {

    @NotNull(message = "username null error")
    @NotBlank(message = "username blank error")
    private String username;

    @NotNull(message = "password null error")
    @NotBlank(message = "password blank error")
    private String password;
}
