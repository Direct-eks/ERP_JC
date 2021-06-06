package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ModelO {
    @NotNull(message = "modelID null error")
    private Integer modelID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "categoryID null error")
    @Min(value = 0, message = "categoryID value error")
    private Integer categoryID;

    private String categoryCode;

    @NotNull(message = "unitID null error")
    @Min(value = 0, message = "unitID value error")
    private Integer unitID;

    private String unitName;
}
