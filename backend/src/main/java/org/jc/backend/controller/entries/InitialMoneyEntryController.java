package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.config.exception.GlobalParamException;
import org.jc.backend.entity.InitialMoneyEntryO;
import org.jc.backend.entity.VO.ListUpdateVO;
import org.jc.backend.service.InitialMoneyEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "InitialMoneyEntry Related")
@RestController
@RequestMapping("/initialMoneyEntry")
public class InitialMoneyEntryController {
    private static final Logger logger = LoggerFactory.getLogger(InitialMoneyEntryController.class);

    private final InitialMoneyEntryService initialMoneyEntryService;

    public InitialMoneyEntryController(InitialMoneyEntryService initialMoneyEntryService) {
        this.initialMoneyEntryService = initialMoneyEntryService;
    }

    /* ------------------------------ API ------------------------------ */

    // isInbound = true: 付款, false: 收款

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/updateEntries")
    public void updateEntries(
            @RequestParam("isInbound") boolean isInbound,
            @RequestBody @Validated ListUpdateVO<InitialMoneyEntryO> updateVO
    ) throws GlobalParamException {
        logger.info("GET Request to /initialMoneyEntry/updateEntries");

        initialMoneyEntryService.updateEntries(isInbound, updateVO.getElements());
    }

    @ApiOperation(value = "", response = InitialMoneyEntryO.class)
    @GetMapping("/getEntries")
    public List<InitialMoneyEntryO> getEntries(
            @RequestParam("isInbound") boolean isInbound
    ) {
        logger.info("GET Request to /initialMoneyEntry/getEntries");

        return initialMoneyEntryService.getEntries(isInbound);
    }

}
