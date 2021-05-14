package org.jc.backend.entity.VO;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ListUpdateVO<T> {
    @Valid
    @NotEmpty(message = "list cannot be empty")
    private List<T> elements;
}
