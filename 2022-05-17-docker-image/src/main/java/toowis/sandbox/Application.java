package toowis.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
class GreetingsController {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @GetMapping("/message")
    public String message() {
        logger.info("call v1 {}", System.currentTimeMillis());
        return "hello, time is " + System.currentTimeMillis();
    }
}