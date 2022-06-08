package com.toowis;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}

@RestController
class SimpleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);
    
    @RequestMapping("/publicKey")
    public String getPublicKey() {
        StringBuilder sbKey = new StringBuilder();
        sbKey.append("-----BEGIN PUBLIC KEY-----");
        sbKey.append(RSAManager.publicKey);
        sbKey.append("-----END PUBLIC KEY-----");
        return sbKey.toString();
    }
    
    @RequestMapping("/clientKey")
    public void putClientKey(String key) {
        logger.info("input: {}", key);
        logger.info("decrypt: {}", RSAManager.decrypt(key));
        
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(true);
        
        httpSession.setAttribute("clientKey", key);
    }
    
    @RequestMapping("/decText")
    public String getDecText(@RequestBody Map<String, String> body) {
        if ( body.get("body").isBlank() ) {
            return "EMPTY";
        }
        
        String decText = RSAManager.decrypt(body.get("body"));
        logger.info("encText: {}", body.get("body"));
        logger.info("decText: {}", decText);
        return decText;
    }
}