package org.jc.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class StoragePlaceO {
    private int storagePlaceID;

    @NotNull(message = "sequenceNumber null error")
    private Integer sequenceNumber;

    @NotNull(message = "sequenceNumber null error")
    @NotBlank(message = "sequenceNumber blank error")
    private String placeName;
}
