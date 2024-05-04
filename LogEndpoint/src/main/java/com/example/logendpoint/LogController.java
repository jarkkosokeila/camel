package com.example.logendpoint;

import com.example.logendpoint.dto.InterfaceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping("/save-interface-log")
    public void saveInterfaceLog(@RequestBody InterfaceLog interfaceLog) {
        logger.info("{}", interfaceLog);
    }
}
