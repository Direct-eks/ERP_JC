package org.jc.backend.entity.VO;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ListUpdateVO<T> {
    @Valid
    private List<T> elements; // todo verify if list can be empty
}
