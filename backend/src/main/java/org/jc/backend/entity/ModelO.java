package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ModelO {
    private int modelID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "categoryID null error")
    private Integer categoryID;

    private String categoryCode;

    @NotNull(message = "unitID null error")
    private Integer unitID;

    private String unitName;
}
