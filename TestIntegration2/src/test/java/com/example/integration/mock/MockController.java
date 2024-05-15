package com.example.integration.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class MockController {
    private static final Logger logger = LoggerFactory.getLogger(MockController.class);

    @PostMapping("/mock-endpoint")
    public void saveInterfaceLog() {
        logger.info("Test");
    }
}
