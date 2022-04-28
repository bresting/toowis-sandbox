package toowis.sandbox.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/admin/member")
@RestController
@Tag(name = "MemeberController", description = "어드민 멤버 컨트롤러")
public class MemeberController {
    
    @GetMapping(value = "/hello")
    @Operation(summary = "hello admin", description = "hello 설명")
    public String hello(){
        return "hello admin";
    }
    
    @GetMapping("/search")
    @ApiResponses({
          @ApiResponse(responseCode = "200", description = "ok")
        , @ApiResponse(responseCode = "404", description = "page not found!")
    })
    @Operation(summary = "어드민 조회", description = "어드민 조회 설명")
    public Map<String, String> search(
            @Parameter(description = "아이디", required = true, example = "admin")
            @RequestParam String userId
    ) {
        Map<String, String> response = new HashMap<String, String>();
        response.put("name", "kimdojin");
        response.put("age", "37");
        response.put("email", "xxxxxxxx@gmail.com");
        return response;
    }
}
