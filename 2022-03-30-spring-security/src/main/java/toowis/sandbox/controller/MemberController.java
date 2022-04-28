package toowis.sandbox.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    
    private static final String authorizationRequestBaseUri = "oauth2/authorization";
    
    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
   
    /**
     * ClientRegistrationRepository을 login.htm VIEW에서 Oauth2 조립한다.
     * @return
     */
    @GetMapping("/login")
    public String login(Model model) {
        
        // ClientRegistrationRepository 주입 받아 처리
        // private final ClientRegistrationRepository clientRegistrationRepository;
        // 
        // Iterable<ClientRegistration> clientRegistrations = null;
        // ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        // if ( type != ResolvableType.NONE
        //   && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
        //     clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        // }
        // clientRegistrations.forEach( registration ->
        //     oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId() )
        // );
        
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        
        inMemoryClientRegistrationRepository.forEach(registration -> {
            oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId() );
        });
        
        model.addAttribute("oauth2UrlMap", oauth2AuthenticationUrls);
        
        return "login";
    }
    
    @GetMapping("/member/user-info")
    public String requestUserInfo(Model model) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if ( authentication.getPrincipal() instanceof toowis.sandbox.service.CustomUserDetailsService.MyUserDetails ) {
            return "member/user-info";
        }
        
        OAuth2User oauth2user = (OAuth2User) authentication.getPrincipal();
        
        logger.info("getAuthentication : {}", authentication);
        logger.info("getName : {}", authentication.getName());
        logger.info("getCredentials : {}", authentication.getCredentials());
        logger.info("getDetails : {}", authentication.getDetails());
        logger.info("---------------------------------------------------------");
        logger.info("{}", oauth2user.getAttributes());
        logger.info("{}", oauth2user.getAttribute("name").toString());
        
        model.addAttribute("loginName", oauth2user.getAttribute("name").toString());
        
        return "member/user-info";
    }
}

