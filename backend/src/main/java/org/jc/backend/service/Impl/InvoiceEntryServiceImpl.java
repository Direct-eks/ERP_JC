package org.jc.backend.service.Impl;

import org.apache.ibatis.exceptions.PersistenceException;
import org.jc.backend.dao.InvoiceEntryMapper;
import org.jc.backend.entity.DO.CheckoutEntryDO;
import org.jc.backend.entity.InvoiceEntryO;
import org.jc.backend.entity.VO.CheckoutEntryWithProductsVO;
import org.jc.backend.service.InvoiceEntryService;
import org.jc.backend.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceEntryServiceImpl implements InvoiceEntryService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceEntryServiceImpl.class);

    private final InvoiceEntryMapper invoiceEntryMapper;

    public InvoiceEntryServiceImpl(InvoiceEntryMapper invoiceEntryMapper) {
        this.invoiceEntryMapper = invoiceEntryMapper;
    }

    /* ------------------------------ SERVICE ------------------------------ */

    @Transactional
    public void createEntryForCheckout(CheckoutEntryWithProductsVO checkoutEntryWithProductsVO, boolean isInbound) {

        try {
            String prefix;
            if (isInbound) {
                if (checkoutEntryWithProductsVO.getInvoiceType().equals("增值税票"))
                    prefix = "入增";
                else
                    prefix = "入普";
            }
            else {
                if (checkoutEntryWithProductsVO.getInvoiceType().equals("增值税票"))
                    prefix = "出增";
                else
                    prefix = "出普";
            }

            int count = invoiceEntryMapper.countNumberOfEntriesOfToday("入增");
            String newSerial = MyUtils.formNewSerial(prefix, count);

            InvoiceEntryO invoiceEntry = new InvoiceEntryO();
            BeanUtils.copyProperties(checkoutEntryWithProductsVO.getInvoiceEntry(), invoiceEntry);

            invoiceEntry.setPartnerCompanyID(checkoutEntryWithProductsVO.getPartnerCompanyID());

            invoiceEntry.setInvoiceEntrySerial(newSerial);
            invoiceEntry.setCheckoutDate(checkoutEntryWithProductsVO.getCheckoutDate());

            invoiceEntryMapper.insertEntry(invoiceEntry);

        } catch (PersistenceException e) {
            e.printStackTrace(); //todo remove in production
            logger.error("insert failed");
            throw e;
        }

    }
}
