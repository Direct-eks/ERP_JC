package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.config.exception.GlobalException;
import org.jc.backend.dao.ModificationMapper;
import org.jc.backend.dao.OutboundEntryMapper;
import org.jc.backend.entity.ModificationO;
import org.jc.backend.entity.DO.OutboundEntryDO;
import org.jc.backend.entity.OutboundProductO;
import org.jc.backend.entity.VO.OutboundEntryWithProductsVO;
import org.jc.backend.service.OutboundEntryService;
import org.jc.backend.utils.IOModificationUtils;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OutboundEntryServiceImpl implements OutboundEntryService {
    private static final Logger logger = LoggerFactory.getLogger(OutboundEntryServiceImpl.class);

    private final OutboundEntryMapper outboundEntryMapper;
    private final ModificationMapper modificationMapper;

    public OutboundEntryServiceImpl(OutboundEntryMapper outboundEntryMapper,
                                    ModificationMapper modificationMapper) {
        this.outboundEntryMapper = outboundEntryMapper;
        this.modificationMapper = modificationMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntry(OutboundEntryWithProductsVO entryWithProductsVO) throws GlobalException {

        OutboundEntryDO newEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(entryWithProductsVO, newEntry);
        List<OutboundProductO> newProducts = entryWithProductsVO.getOutboundProducts();

        try {
            int count = outboundEntryMapper.countNumberOfEntriesOfToday();
            String newSerial = MyUtils.formNewSerial("销出", count, newEntry.getShipmentDate());

            newEntry.setOutboundEntryID(newSerial);
            outboundEntryMapper.insertNewEntry(newEntry);

            for (var product : newProducts) {
                product.setOutboundEntryID(newSerial);
                int id = outboundEntryMapper.insertNewProduct(product);
                logger.info("Insert new outbound product id: " + id);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<OutboundEntryWithProductsVO> getEntriesInDateRange(Date startDate, Date endDate,
                                                                   String type, int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<OutboundEntryWithProductsVO> entries = new ArrayList<>();
        try {
            List<OutboundEntryDO> entriesFromDatabase = outboundEntryMapper.queryEntriesInDateRangeByTypeAndCompanyID(
                    dateFormat.format(startDate), dateFormat.format(endDate), type, id);

            for (var entryFromDatabase : entriesFromDatabase) {
                OutboundEntryWithProductsVO tempEntry = new OutboundEntryWithProductsVO();
                BeanUtils.copyProperties(entryFromDatabase, tempEntry);

                List<OutboundProductO> products = outboundEntryMapper.queryProductsByEntryID(
                        tempEntry.getOutboundEntryID());
                tempEntry.setOutboundProducts(products);

                entries.add(tempEntry);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("query failed");
            throw e;
        }

        return entries;
    }

    @Transactional
    public void completeEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO) {

        OutboundEntryDO currentInfo = new OutboundEntryDO();
        BeanUtils.copyProperties(outboundEntryWithProductsVO, currentInfo);

        String id = currentInfo.getOutboundEntryID();

        OutboundEntryDO originInfo;
        try {
            originInfo = outboundEntryMapper.selectEntryShippingInfoForCompare(id);

            StringBuilder record = new StringBuilder("修改者: " + currentInfo.getDrawer() + "; ");
            boolean bool = IOModificationUtils.shippingInfoCompareAndFormModificationRecord(
                    record, currentInfo, originInfo);

            if (bool) {
                outboundEntryMapper.updateShippingInfo(currentInfo);

                logger.info("Completion: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originInfo.getOutboundEntryID(), record.toString()));
            }
            else {
                logger.warn("Nothing changed!");
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Completion failed");
            throw e;
        }

    }

    @Transactional
    public void modifyEntry(OutboundEntryWithProductsVO outboundEntryWithProductsVO) {

        OutboundEntryDO currentEntry = new OutboundEntryDO();
        BeanUtils.copyProperties(outboundEntryWithProductsVO, currentEntry);

        List<OutboundProductO> currentProducts = outboundEntryWithProductsVO.getOutboundProducts();

        String id = currentEntry.getOutboundEntryID();
        logger.info("Serial to be changed: " + id);
        OutboundEntryDO originEntry;
        List<OutboundProductO> originProducts;
        try {
            originEntry = outboundEntryMapper.selectEntryForCompare(id);
            originProducts = outboundEntryMapper.selectProductsForCompare(id);

            StringBuilder record = new StringBuilder("修改者: " + currentEntry.getDrawer() + "; ");
            boolean bool1 = IOModificationUtils.entryCompareAndFormModificationRecord(
                    record, currentEntry, originEntry);

            if (bool1) {
                logger.info("");//todo
                outboundEntryMapper.updateEntry(currentEntry);
            }

            boolean bool2 = false;
            for (var originProduct : originProducts) {
                boolean found = false;
                for (var currentProduct : currentProducts) {
                    if (currentProduct.getOutboundProductID() == originProduct.getOutboundProductID()) {
                        boolean bool3 = IOModificationUtils.productsCompareAndFormModificationRecord(
                                record, currentProduct, originProduct);

                        if (bool3) {
                            bool2 = true;
                            outboundEntryMapper.updateProduct(currentProduct);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    outboundEntryMapper.deleteProductByID(originProduct.getOutboundProductID());
                }
            }

            if (bool1 || bool2) {
                logger.info("Modification: " + record);
                modificationMapper.insertModificationRecord(new ModificationO(
                        originEntry.getOutboundEntryID(), record.toString()));
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("update failed");
            throw e;
        }

    }

    @Transactional
    public void deleteEntry(String id) {
        try {
            outboundEntryMapper.deleteProductsByEntryID(id);
            outboundEntryMapper.deleteEntryByID(id);
        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production mode
            logger.error("Deletion failed");
            throw e;
        }

        //todo: deduct stock
    }

    @Transactional(readOnly = true)
    public List<OutboundProductO> getNotCheckedOutProducts(int companyID, String invoiceType) {

        List<OutboundProductO> products = new ArrayList<>();

        try {
            List<String> entryIDs = outboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);
            for (var entryID : entryIDs) {
                List<OutboundProductO> tempProducts = outboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    if (tempProduct.getCheckoutSerial().equals("")) {
                        products.add(tempProduct);
                    }
                }
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in productsion
            logger.error("query failed");
            throw e;
        }

        return products;
    }

    @Transactional(readOnly = true)
    public List<OutboundProductO> getCheckoutButNotInvoicedProducts(int companyID, String invoiceType) {

        List<OutboundProductO> products = new ArrayList<>();

        try {
            List<String> entryIDs = outboundEntryMapper.queryEntriesByCompanyIDAndInvoiceType(companyID, invoiceType);
            for (var entryID : entryIDs) {
                List<OutboundProductO> tempProducts = outboundEntryMapper.queryProductsByEntryID(entryID);
                for (var tempProduct : tempProducts) {
                    if (!tempProduct.getCheckoutSerial().equals("") && tempProduct.getInvoiceSerial().equals("")) {
                        products.add(tempProduct);
                    }
                }
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("Query failed");
            throw e;
        }

        return products;
    }

    @Transactional
    public void updateProductsWithCheckoutSerial(List<OutboundProductO> products, String checkoutSerial) {

        try {
            for (var product : products) {
                product.setCheckoutSerial(checkoutSerial);
                outboundEntryMapper.updateProductsWithCheckoutSerial(product);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("Query failed");
            throw e;
        }

    }

    @Transactional
    public void updateProductsWithInvoiceSerial(List<OutboundProductO> products, String invoiceSerial) {

        try {
            for (var product : products) {
                product.setInvoiceSerial(invoiceSerial);
                outboundEntryMapper.updateProductsWithInvoiceSerial(product);
            }

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("Query failed");
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public List<OutboundProductO> getProductsWithCheckoutSerial(String checkoutSerial){

        List<OutboundProductO> products;
        try {
            products = outboundEntryMapper.getProductsWithCheckoutSerial(checkoutSerial);

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("Query failed");
            throw e;
        }

        return products;
    }

    @Transactional(readOnly = true)
    public List<OutboundProductO> getProductsWithInvoiceSerial(String invoiceSerial){

        List<OutboundProductO> products;
        try {
            products = outboundEntryMapper.getProductsWithInvoiceSerial(invoiceSerial);

        } catch (PersistenceException e) {
            e.printStackTrace(); // todo remove in production
            logger.error("Query failed");
            throw e;
        }

        return products;
    }
}
