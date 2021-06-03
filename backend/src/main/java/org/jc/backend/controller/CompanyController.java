package org.jc.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jc.backend.entity.CompanyCategoryO;
import org.jc.backend.entity.CompanyO;
import org.jc.backend.entity.RelevantCompanyCategoryO;
import org.jc.backend.entity.RelevantCompanyO;
import org.jc.backend.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Indexed
@Api(tags = "Company Related")
@RestController
@RequestMapping("/company")
public class CompanyController {
    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /* ------------------------------ API ------------------------------ */

    @ApiOperation(value = "", response = CompanyO.class, notes = "only abbreviated_name and phone fields are used")
    @GetMapping("/getCompanyByFuzzySearch")
    public List<CompanyO> getCompanyByFuzzySearch(
            @RequestParam(value = "phone", defaultValue = "") String phone,
            @RequestParam(value = "name", defaultValue = "") String name
    ) {
        logger.info("GET Request to /company/getCompanyByFuzzySearch, phone: " + phone + ", name: " + name);

        return companyService.getCompanyByFuzzySearch(phone, name);
    }

    @ApiOperation(value = "", response = CompanyCategoryO.class)
    @GetMapping("/getCompanyAreas")
    public List<CompanyCategoryO> getCompanyAreas() {
        logger.info("GET Request to /company/getCompanyAreas");
        //category is area here!
        return companyService.getCompanyCategories();
    }

    @ApiOperation(value = "", response = CompanyO.class)
    @GetMapping("/getCompaniesByCategory/{id}")
    public List<CompanyO> getCompaniesByCategory(@PathVariable("id") int id) {
        logger.info("GET Request to /company/getCompaniesByCategory, id: " + id);

        return companyService.getCompaniesByCategory(id);
    }

    @ApiOperation(value = "", response = CompanyO.class)
    @GetMapping("/getSelfCompany")
    public CompanyO getSelfCompany() {
        logger.info("GET Request to /company/getSelfCompany");

        return companyService.getSelfCompany();
    }


    @ApiOperation(value = "", response = RelevantCompanyCategoryO.class)
    @GetMapping("/getRelevantCompanyCategories")
    public List<RelevantCompanyCategoryO> getRelevantCompanyCategories() {
        logger.info("GET Request to /company/getRelevantCompanyCategories");

        return companyService.getRelevantCompanyCategories();
    }

    @ApiOperation(value = "", response = RelevantCompanyO.class)
    @GetMapping("/getRelevantCompaniesByCategory/{id}")
    public List<RelevantCompanyO> getRelevantCompaniesByCategory(@PathVariable("id") int id) {
        logger.info("GET Request to /company/getRelevantCompaniesByCategory, id: " + id);

        return companyService.getRelevantCompaniesByCategory(id);
    }
}
