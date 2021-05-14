package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class FactoryBrandO {
    private Integer factoryBrandID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "code null error")
    private String code;

    @NotNull(message = "remark null error")
    private String remark;
}
