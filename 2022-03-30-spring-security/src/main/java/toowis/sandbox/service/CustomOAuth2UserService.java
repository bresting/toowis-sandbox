package toowis.sandbox.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * <pre class="code">
 * public class CustomOAuth2UserService extends DefaultOAuth2UserService {
 *     @Override
 *     public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
 *         OAuth2User user =  super.loadUser(userRequest);
 *         System.out.println("CustomOAuth2UserService invoked");
 *         return new CustomOAuth2User(user);
 *     }
 * }
 * 
 * https://www.youtube.com/watch?v=5jOn-t1MbvY
 * 
 * private final HttpSession httpSession;
 * private final UserRepository userRepository;
 * 
 * @lombok.AllArgsConstructor
 * @lombok.NoArgsConstructor
 * public class MyOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
 *     UserRepository userRepository;
 *     HttpSession httpSession
 *     
 *     @Override
 *     public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
 *         OAuth2UserService delegate = new DefaultOAuth2UserService();
 *         OAuth2User oauth2User = delegate.loadUser(userRequest);
 *         
 *         ClientRegistration clientRegistration = userRequest.getClientRegistration();
 *      
 *         // 서비스구분(구글, 네이버)
 *         String registrationId = clientRegistration.getRegistrationId();
 *      
 *         // 속성정보
 *         String userNameAttributeName = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
 *         
 *         String email = "";
 *         if (registrationId.equals("naver")) {
 *             Map<String, Object> hash = Map.class.cast(attributes.get("response"));
 *             email = (String) hash.get("email");
 *         }
 *         else if (registrationId.equals("kakao")) {
 *             Map<String, Object> hash = Map.class.cast(attributes.get("id"));
 *             email = (String) hash.get("email");
 *         }
 *         else if (registrationId.equals("google")) {
 *             email = String.class.cast(attributes.get("email"));
 *         }
 *         else {
 *             throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
 *         }
 *         
 *         // DB Repository 처리 - userRepository
 *         // email 조회, 없으면 등록 같은 ...
 *         
 *         httpSession.setAttribute("user", USER_DTO);
 *         
 *         return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("user.getRole()"))
 *              , attributes , userNameAttributeName);
 *     }
 * }
 * </pre>
 * <hr/>
 * <pre class="code">
 * userService 지정시
 * HttpSecurity.oauth2Login()
 *             .loginPage("/login")
 *             .userInfoEndpoint()
 *             .userService( CustomOAuth2UserService )
 * 
 * {@link org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer#userInfoEndpoint()}
 * 
 * 설정 없어서 자동 주입된다.
 * HttpSecurity...userInfoEndpoint.userService(주입 안한경우)
 * {@link org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider#authenticate(org.springframework.security.core.Authentication)}
 * 
 * </pre>
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        logger.info("== CustomOAuth2UserService invoked\n{}", userRequest.getAdditionalParameters());
        logger.info("== AccessToken invoked\n{}", userRequest.getAccessToken());
        
        /**
         * CustomOAuth2User 사용시
         * OAuth2User user = super.loadUser(userRequest)
         * return new CustomOAuth2User(user);
         * 
         * CustomOAuth2User customOAuth2user = (CustomOAuth2User) authentication.getPrincipal();
         * customOAuth2user.getName();
         * customOAuth2user.getEmail();
         */
        
        return super.loadUser(userRequest);
    }
}
