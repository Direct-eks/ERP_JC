package org.jc.backend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FactoryBrandO {
    private Integer factoryBrandID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    @NotBlank(message = "code blank error")
    private String code;

    @NotNull(message = "remark null error")
    private String remark;
}
