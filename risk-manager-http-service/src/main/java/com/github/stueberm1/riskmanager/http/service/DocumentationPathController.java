package com.github.stueberm1.riskmanager.http.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/docs", produces = {MediaType.TEXT_HTML_VALUE})
public class DocumentationPathController {


    private final Logger logger = LoggerFactory.getLogger(DocumentationPathController.class);
    @GetMapping
    public String getApiDoc() {

        logger.info("getApiDoc");
        return "docs/index.html";
    }
}
