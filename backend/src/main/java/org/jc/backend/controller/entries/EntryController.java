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

}
