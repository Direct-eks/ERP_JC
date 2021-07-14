package org.jc.backend.controller.entries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.StatO.EntryStatO;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Entries Related")
@RestController
@RequestMapping("/entry")
public class EntryController {

    @ApiOperation(value = "", response = EntryStatO.class)
    @GetMapping("/getAuditEntries/{month}")
    public List<EntryStatO> getAuditEntries(@PathVariable("month") String month) {

        return null;
    }

    @ApiOperation(value = "", response = void.class)
    @PostMapping("/auditEntries")
    public void auditEntries(@RequestParam("month") String month) {

    }
}
