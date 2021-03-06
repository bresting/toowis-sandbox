package toowis.sandbox.online.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/online/demo")
@RestController
@Tag(name = "DemoController", description = "온라인 데모 컨트롤러")
public class DemoController {
    @GetMapping(value = "/status")
    @Operation(summary = "check status", description = "상태확인")
    public String status(){
        return "ok!!";
    }
}
