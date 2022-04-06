package toowis.sandbox;


import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.catalina.core.StandardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class WebServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }
    
    @Bean
    FilterRegistrationBean<CustomXFilter> xFilterRegistrationBean() {
        FilterRegistrationBean<CustomXFilter> bean = new FilterRegistrationBean<CustomXFilter>(new CustomXFilter());
        bean.setOrder(0);
        return bean;
    }
    
    @Bean
    FilterRegistrationBean<CustomYFilter> yFilterRegistrationBean() {
        FilterRegistrationBean<CustomYFilter> bean = new FilterRegistrationBean<CustomYFilter>(new CustomYFilter());
        bean.setOrder(1);
        return bean;
    }
    
    // 빈으로 만들어 처리
    // @Bean
    // public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sessionManagerCustomizer() {
    //     return factory -> factory.addContextCustomizers(context -> context.setSessionTimeout(24 * 60));
    // }
    // @Bean
    // public WebServerFactoryCustomizer<TomcatServletWebServerFactory> contextPath() {
    //     return factory -> factory.setContextPath("/factory");
    // }
    // @Bean
    // public WebServerFactoryCustomizer<TomcatServletWebServerFactory> factory() {
    //     return new ServletCustomizer();
    // }
}

@Component
class ServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setContextPath("/factory");
        factory.addContextCustomizers(context -> {
            if(context instanceof StandardContext) {
                /**
                 * 서버 종료까지 대기시간(기본값 2000ms)
                 * 종료 요청되면 이후 거래는 받지 않는다. 이전 발생 거래는 unloadDelay 시간내 처리된다.
                 * unloadDelay는 거래 서비스 최대 예상 시간으로 설정한다.
                 * 
                 * <context unloadDelay="15000">
                 */
                ((StandardContext)context).setUnloadDelay(15 * 1000);
            }
        });
    }
}

// @Component -> @Bean xFilterRegistrationBean에서 설정
class CustomXFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("▶ CustomXFilter init - {}", filterConfig);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("CustomXFilter do filter - {}, {}", request, response);
        chain.doFilter(request, response);
    }
    public void destroy() {
        logger.info("▶ CustomXFilter destroy");
    }
}

class CustomYFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("▶ CustomYFilter init - {}", filterConfig);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("CustomYFilter do filter - {}, {}", request, response);
        chain.doFilter(request, response);
    }
    public void destroy() {
        logger.info("▶ CustomYFilter destroy");
    }
}

@RestController
class GreetingsController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @RequestMapping("/greetings")
    public Map<String, String> greetings(@RequestParam(value = "sleep", required = false, defaultValue = "0") int sleep) {
        
        logger.info("thread-id: {}, sleep {}", Thread.currentThread().getId(), sleep);
        
        if ( 0 < sleep ) {
            try {
                Thread.sleep(sleep * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return Collections.singletonMap("greetings", "hello embedded servlet, sleep " + sleep);
    }
}