package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.OutboundProductModifyO;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class OutboundEntryModifyVO {
    private String inboundEntryID;
    @DecimalMin(value = "0.0", message = "totalAmount smaller than zero error")
    private double totalAmount;
    @Pattern(regexp = "^(自提|送货|代办发货|发货代收款)$", message = "deliveryMethod value error")
    private String deliveryMethod;
    @Pattern(regexp = "^(增值税票|普票|收据)$", message = "invoiceType value error")
    private String invoiceType;
    private String drawer;

    private int departmentID;
    // from e_department
    private String departmentName;

    private String remark;

    @Valid
    private List<OutboundProductModifyO> outboundProducts;
}
