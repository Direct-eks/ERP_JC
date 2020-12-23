package org.jc.backend.service.Impl;

import org.jc.backend.dao.PurchaseOrderMapper;
import org.jc.backend.entity.DO.PurchaseOrderEntryDO;
import org.jc.backend.entity.DO.PurchaseOrderProductDO;
import org.jc.backend.entity.VO.PurchaseOrderEntryWithProductsVO;
import org.jc.backend.entity.VO.PurchaseOrderProductVO;
import org.jc.backend.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    public Boolean createNewPurchaseOrder(PurchaseOrderEntryWithProductsVO entryWithProducts) {
        //todo
        return false;
    }

    public List<PurchaseOrderEntryWithProductsVO> getPurchaseOrdersWithinDateRangeByCompanyID(Date startDate, Date endDate, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<PurchaseOrderEntryWithProductsVO> entryList = new ArrayList<>();

        List<PurchaseOrderEntryDO> entryListFromDatabase = purchaseOrderMapper.queryPurchaseOrderEntriesByCompanyID(
                dateFormat.format(startDate), dateFormat.format(endDate), id);

        BeanUtils.copyProperties(entryListFromDatabase, entryList);

        entryList.forEach(entry -> {
            List<PurchaseOrderProductDO> productsDOs = purchaseOrderMapper.queryPurchaseOrderProductsByEntryID(
                    entry.getPurchaseOrderEntryID());
            List<PurchaseOrderProductVO> productVOs = new ArrayList<>();
            BeanUtils.copyProperties(productsDOs, productVOs);
            entry.setPurchaseOrderProducts(productVOs);
        });

        return entryList;
    }
}
