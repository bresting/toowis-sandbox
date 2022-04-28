package toowis.sandbox.online.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/online/simple")
@RestController
@Tag(name = "SimpleController", description = "단순 컨트롤러")
public class SimpleController {
    @GetMapping(value = "/time")
    @Operation(summary = "server time", description = "서버시간")
    public String serverTime(){
        return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
    }
}
