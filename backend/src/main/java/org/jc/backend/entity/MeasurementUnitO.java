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
public class MeasurementUnitO {

    private int unitID;

    @NotNull(message = "unitName null error")
    @NotBlank(message = "unitName blank error")
    private String unitName;

    @NotNull(message = "remark null error")
    private String remark;
}
