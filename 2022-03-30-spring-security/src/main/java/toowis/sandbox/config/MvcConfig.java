package toowis.sandbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        
        // registry.addRedirectViewController("/", "/home");
        
        /**
         * controller class없이 view 설정
         * 
         * 아래와 동일하다.
         * @RequestMapping("/")
         * public String index(Model model) { return "index" }
         */
        registry.addViewController("/")   // http://도메인/
                .setViewName("index");   // index 템플릿으로 연결
    }
}