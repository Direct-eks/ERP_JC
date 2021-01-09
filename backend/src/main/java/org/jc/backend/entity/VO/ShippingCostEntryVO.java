package org.jc.backend.entity.VO;

import lombok.Getter;
import lombok.Setter;
import org.jc.backend.entity.DO.InboundEntryDO;
import org.jc.backend.entity.DO.OutboundEntryDO;

import java.util.List;

@Getter
@Setter
public class ShippingCostEntryVO {
    private String shippingCostEntrySerial;
    private int partnerCompanyID;
    private int isTaxDeduction;
    private String invoiceNumber;
    private double totalAmount;
    private double invoiceAmount;
    private String shippingPaymentType;
    private String remark;
    private String drawer;
    private String creationDate;
    private String checkoutDate;
    private int inOrOut;
    private String invoiceDate;
    private int isModified;

    private List<InboundEntryDO> inboundEntries;
    private List<OutboundEntryDO> outboundEntries;
}
