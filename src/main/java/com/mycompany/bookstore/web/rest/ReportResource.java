package com.mycompany.bookstore.web.rest;


import com.mycompany.bookstore.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rest controller for managing reports
 */

@RestController
@RequestMapping("/api")
public class ReportResource {
    private final ReportService reportService;
    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    public ReportResource(ReportService reportService){
        this.reportService=reportService;
    }

    @GetMapping("/report")
    public void exportBooksToExel(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=bookstore.xlsx";
        httpServletResponse.setHeader(headerKey, headerValue);
        reportService.exportToExel(httpServletResponse);
    }
}
