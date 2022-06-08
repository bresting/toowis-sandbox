package com.toowis;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
class SimpleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);
    
    @RequestMapping("/generate/{size}")
    public String generate(@PathVariable int size) {
        Instant start = Instant.now();
        
        logger.info("generate size {}", size);
        
        List<SomeDto> list = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            SomeDto dto = new SomeDto();
            dto.key   = "key : " + i;
            dto.value = "1234567890 / 1234567890 / 1234567890 / 1234567890 / 1234567890";
            list.add(dto);
        }
        logger.info("list size {}", list.size());
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        return timeElapsed.toNanos() + " nanos";
    }
}

class SomeDto {
    public String key;
    public String value;
}
