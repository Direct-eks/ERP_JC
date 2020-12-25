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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PostMapping("/getCompanyByFuzzySearch")
    public List<CompanyO> getCompanyByFuzzySearch(@RequestBody @Validated CompanyO companyO) {
        logger.info("POST Request to /company/getCompanyByFuzzySearch");

        return companyService.getCompanyByFuzzySearch(companyO);
    }

    @ApiOperation(value = "", response = CompanyCategoryO.class, notes = "for partner company: category is area")
    @GetMapping("/getCompanyCategories")
    public List<CompanyCategoryO> getCompanyCategories() {
        logger.info("GET Request to /company/getCompanyCategories");

        return companyService.getCompanyCategories();
    }

    @ApiOperation(value = "", response = CompanyO.class)
    @GetMapping("/getCompaniesByCategory/{id}")
    public List<CompanyO> getCompaniesByCategory(@PathVariable("id") int id) {
        logger.info("GET Request to /company/getCompaniesByCategory, id: " + id);

        return companyService.getCompaniesByCategory(id);
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
