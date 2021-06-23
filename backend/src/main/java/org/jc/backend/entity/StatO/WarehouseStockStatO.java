package org.jc.backend.entity.StatO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseStockStatO {
    // identifier
    private boolean isInbound;
    // from e_entry
    private String inboundEntryID;
    private String outboundEntryID;
    private String entryDate;
    private String shipmentDate;

    private int inboundProductID;
    private int outboundProductID;

    private int skuID;

    private int quantity;
    private int stockQuantity;
    private int warehouseStockID;
    private int warehouseID;
    private String unitPriceWithoutTax;
    private String stockUnitPrice;
}
