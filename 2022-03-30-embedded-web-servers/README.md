## embedded web servers  

* 임베디드 서버 핸들링 방법
    1. filter orders
    2. unloadDelay

    
* 실행
http://localhost:8080/factory/greetings


```
특이사항

spring boot 2.x 변경됨
EmbeddedServletContainerCustomizer -> WebServerFactoryCustomizer
https://www.baeldung.com/embeddedservletcontainercustomizer-configurableembeddedservletcontainer-spring-boot
https://docs.spring.io/spring-boot/docs/2.6.5/reference/html/howto.html#howto.webserver

자동설정 정보
org.springframework.boot.autoconfigure.SpringBootApplication
/META-INF/spring.factories
...
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
...
```
