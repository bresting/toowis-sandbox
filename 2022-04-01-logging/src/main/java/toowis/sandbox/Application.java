package toowis.sandbox;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * filter -> interceptor 순서로 처리됨
 * 인터셉터 단계에선 호출정보(컨트롤러)등 접근이 용이함
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    // @Bean
    // public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
    //     CustomRequestLoggingFilter customRequestLoggingFilter = new CustomRequestLoggingFilter();
    //     customRequestLoggingFilter.setIncludeQueryString(true);
    //     customRequestLoggingFilter.setIncludePayload(true);
    //     customRequestLoggingFilter.setIncludeHeaders(true);
    //     customRequestLoggingFilter.setIncludeClientInfo(true);
    //     customRequestLoggingFilter.setMaxPayloadLength(100000);
    // 
    //     return customRequestLoggingFilter;
    // }
}

/**
 * 필터 사용
 * @see org.springframework.web.filter.OncePerRequestFilter
 */
@Component
class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomRequestLoggingFilter.class);
    
    MessageMaker messageMaker = null;
    
    CustomRequestLoggingFilter() {
        logger.info("■ init CustomRequestLoggingFilter");
        super.setIncludeQueryString(true);
        super.setIncludeClientInfo(true);
        super.setIncludeHeaders(true);
        super.setIncludePayload(true);
        super.setMaxPayloadLength(10000);
        
        // custom message
        // messageMaker = new MessageMaker() {
        //     @Override
        //     public String make(HttpServletRequest request, String prefix, String suffix) {
        //         return null;
        //     }
        // };
    }
    
    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isInfoEnabled();
    }
    
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        MDC.put("trace-id", UUID.randomUUID().toString());
        logger.info("■ trace-id [{}]", MDC.get("trace-id"));
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
        logger.info("■ trace-id [{}]", MDC.get("trace-id"));
        MDC.clear();
    }
    
    @Override
    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        if ( this.messageMaker != null ) {
            return this.messageMaker.make(request, prefix, suffix);
        } else {
            return super.createMessage(request, prefix, suffix);
        }
    }
    
    public interface MessageMaker {
        String make(HttpServletRequest request, String prefix, String suffix);
    }
}

/**
 * WebMvcConfigurer 로그 인터셉터 연결
 */
@Configuration
class LogConfig implements WebMvcConfigurer {
    private final CustomLogInterceptor customLogInterceptor;
    public LogConfig(final CustomLogInterceptor logTraceIdInterceptor) {
        this.customLogInterceptor = logTraceIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customLogInterceptor)
                .addPathPatterns("/**");
    }
}

/**
 * 로그 인터셉터
 */
@Component
class CustomLogInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomLogInterceptor.class);
    
    private static ThreadLocal<Long> threadLocalValue = new ThreadLocal<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        threadLocalValue.set(System.currentTimeMillis());
        
        if ( handler instanceof HandlerMethod ) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.info("호출정보: {}.{}",  method.getBeanType().getSimpleName(), method.getMethod().getName());
        }
        
        logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ REQUEST_INFO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        logger.debug("[{}] RequestURI : {}", MDC.get("trace-id"), request.getRequestURI());
        Enumeration<?> headerNames  = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            Object element = headerNames.nextElement();
            logger.debug("{} : {}", element, request.getHeader(element.toString()));
        }
        logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ REQUEST_INFO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long startTime = threadLocalValue.get();
        threadLocalValue.remove();
        logger.debug("======================================= END_REQUEST ===========================================");
        logger.debug("■■■■■ [{}] RequestURI : {}", MDC.get("trace-id"), request.getRequestURI());
        logger.debug("■■■■■ 처리시간: {}", String.format("%.3f", ( (double) (System.currentTimeMillis() - startTime ) / 1000 ) ) );
        logger.debug("======================================= END_REQUEST ===========================================");
    }
}

@RestController
class GreetingsController {
    @RequestMapping("/greetings")
    public Map<String, String> greetings(){
        try {
            Thread.sleep(1250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("greetings", MDC.get("trace-id"));
    }
}