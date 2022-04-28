package toowis.sandbox.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * org.springframework.security.oauth2.core.user.OAuth2User 직접 접근해도 됨
 * 
 * HttpSecurity
 * .oauth2Login()
 * .loginPage("/login")
 * .userInfoEndpoint().userService(customOAuth2UserService)
 * .and()
 * .successHandler(new AuthenticationSuccessHandler() {
 *     @Override
 *     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
 *             Authentication authentication) throws IOException, ServletException {
 *         
 *         CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
 *         
 *         // 이런식으로
 *         XXXService.processOAuthPostLogin(oauthUser.getEmail() / oauthUser.getUserId() );
 *         
 *         ~~~ XXXService.processOAuthPostLogin
 *         User existUser = repo.getUserByUsername(username);
 *         if (existUser == null) {
 *             User newUser = new User();
 *             newUser.setUsername(username);
 *             newUser.setProvider(Provider.GOOGLE);
 *             newUser.setEnabled(true);
 *             
 *             repo.save(newUser);
 *             
 *             System.out.println("Created new user: " + username);
 *         }
 *         ~~~
 *         
 *         response.sendRedirect("/member/user-info");
 *     }
 * })
 * .failureHandler(new AuthenticationFailureHandler() {
 *     @Override
 *     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
 *             AuthenticationException exception) throws IOException, ServletException {
 *             
 *         logger.error("처리 실패 했습니다.\n\n{}", exception);
 *     }
 * }) 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class CustomOAuth2User implements OAuth2User, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private OAuth2User oauth2User;
    
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }
    
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }
}