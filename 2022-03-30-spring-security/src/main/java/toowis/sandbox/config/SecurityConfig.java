package toowis.sandbox.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import toowis.sandbox.service.CustomOAuth2UserService;
import toowis.sandbox.service.CustomUserDetailsService;
/**
 * ■ Spring security architecture                                                                                       
 *                              ┌─────────────────────────────────────┐                                                 
 *                           ┌> │ UsernamePasswordAuthenticationToken │                                                 
 *                          2│  └─────────────────────────────────────┘                                                 
 *              1  ┌──────────────────────┐      ┌───────────────────────┐      ┌───────────────────────────┐           
 * http-request -> │ AuthenticationFilter │ 3--> │ AuthenticationManager │ 4--> │ AuthenticationProvider(s) │           
 *                 │                      │ <--9 │     <<interface>>     │ <--8 │                           │           
 *                 └──────────────────────┘      └───────────────────────┘      └───────────────────────────┘           
 *                           ↓10                            ↑ implements                   5↓  7↑                       
 *                 ┌─────────────────────┐          ┌─────────────────┐             ┌────────────────────┐              
 *                 │SecurityContextHolder│          │ ProviderManager │             │ UserDetailsService │              
 *                 │┌───────────────────┐│          └─────────────────┘             └─────────┬──────────┘              
 *                 ││  SecurityContext  ││                                                    │                         
 *                 ││┌─────────────────┐││                                             ┌──────┴────────┐                
 *                 │││ Authentication  │││                                             │ UserDetails   │                
 *                 ││└─────────────────┘││                                             │ <<interface>> │                
 *                 │└───────────────────┘│                                             └───────────────┘                
 *                 └─────────────────────┘                                                    ↑ implements              
 *                                                                                         ┌──────┐                     
 *                                                                                         │ User │                     
 *                                                                                         └──────┘                     
 *                                                                                                                      
 * <hr>                                                                                                                 
 * 다음은 대략적인 과정이다.                                                                                            
 *           ┌─────────────────────────────────────┐                                                                     
 *           │ UsernamePasswordAuthenticationToken │                                                                     
 *           └─────────────────────────────────────┘                                                                     
 *                             ↑ 2.ID,PW로 토큰생성                                                                     
 *                  ┌──────────────────────┐      ┌───────────────────────┐     ┌────────────────────┐                  
 * 1. 로그인요청 -> │ AuthenticationFilter │ 3--> │ AuthenticationManager │4--> │ UserDetailsService │                  
 *    ID/PW         └──────────────────────┘ <--9 └───────────────────────┘<--6 └────────────────────┘                  
 *                             │                           7.PW확인                       ↓ 5.사용자정보(ID로 조회)     
 *                             ↓                     BCryptPasswordEncoder             ┌──────┐                         
 *                   ┌─────────────────────┐                                           │  DB  │                         
 *                   │SecurityContextHolder│ 10.Authentication 저장                    └──────┘                         
 *                   └─────────────────────┘                                                                            
 *                                                                                                                      
 * <hr>
 * <pre>
 * ClientRegistration + CommonOAuth2Provider 자동 주입하지 않고 개발자 직접 구현하는 방법
 * 
 * ClientRegistration
 * org.springframework.security.oauth2.client.registration.ClientRegistrationRepository#findByRegistrationId
 * └- org.springframework.security.oauth2.client.registration.ClientRegistration#getRegistrationId
 * 
 * org.springframework.security.config.oauth2.client.CommonOAuth2Provider
 * 
 * @EnableAutoConfiguration(exclude = { WebMvcAutoConfiguration.class }) Application 설정
 * @EnableAutoConfiguration(exclude = { WebMvcAutoConfiguration.class }) Application 설정
 * 
 * HttpSecurity
 * .clientRegistrationRepository(clientRegistrationRepository())
 * .authorizedClientService(authorizedClientService())
 * 
 * 참조
 * https://godekdls.github.io/Spring%20Security/oauth2/
 * 
 * @Configuration
 * public class OAuth2LoginConfig {
 *     @Bean
 *     public ClientRegistrationRepository clientRegistrationRepository() {
 *         return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
 *     }
 *     private ClientRegistration googleClientRegistration() {
 *         return ClientRegistration.withRegistrationId("google")
 *             .clientId("google-client-id")
 *             .clientSecret("google-client-secret")
 *             .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
 *             .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
 *             .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
 *             .scope("openid", "profile", "email", "address", "phone")
 *             .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
 *             .tokenUri("https://www.googleapis.com/oauth2/v4/token")
 *             .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
 *             .userNameAttributeName(IdTokenClaimNames.SUB)
 *             .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
 *             .clientName("Google")
 *             .build();
 *     }
 * }
 * ---------------------------------------------------------------------------------------------------------------------
 * private final Environment env;
 * private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
 * private static List<String> clients = Arrays.asList("google", "kakao");
 * 
 * @Bean
 * public ClientRegistrationRepository clientRegistrationRepository() {
 *     List<ClientRegistration> registrations = clients.stream()
 *             .map(c -> getRegistration(c))
 *             .filter(registration -> registration != null)
 *             .collect(Collectors.toList());
 *     
 *     return new InMemoryClientRegistrationRepository(registrations);
 * }
 * 
 * private ClientRegistration getRegistration(String client){
 *     // API Client Id 불러오기
 *     String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
 *     
 *     // API Client Id 값이 존재하는지 확인하기
 *     if (clientId == null) {
 *         return null;
 *     }
 *     
 *     // API Client Secret 불러오기
 *     String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
 *     
 *     switch (client) {
 *     case "google":
 *         return CommonOAuth2Provider.GOOGLE.getBuilder(client)
 *                 .clientId(clientId)
 *                 .clientSecret(clientSecret)
 *                 .build();
 *     case "kakao":
 *         return AdditionalOAuth2Provider.KAKAO.getBuilder(client)
 *                 .clientId(clientId)
 *                 .clientSecret(clientSecret)
 *                 .scope("profile_nickname")
 *                 .build();
 *     default:
 *         return null;
 *     }
 * }
 * 
 * @Bean
 * public OAuth2AuthorizedClientService authorizedClientService() {
 *     return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
 * }
 * </pre>
 * <hr>
 * <pre>
 * 2023.05.24
 * 2.7.x Deprecated org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
 *  
 * </pre>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Autowired private CustomOAuth2UserService customOAuth2UserService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 1. 여기서 객체를 bean으로 제공 하거나
     * 2. @see CustomUserDetailsService @service 로 bean 등록해서 @Autowired 로 사용해도 된다.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // new CustomUserDetailsService 이 대충 요런식이다.
        // new UserDetailsService() {
        //     @Override
        //     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //         return new UserDetails() {
        //             // TODO implement UserDetails
        //             @Override public Collection<? extends GrantedAuthority> getAuthorities() { ... }
        //             @Override public String getPassword() { ... }
        //             @Override public String getUsername() { ... }
        //             @Override public boolean isAccountNonExpired() { ... }
        //             @Override public boolean isAccountNonLocked() { ... }
        //             @Override public boolean isCredentialsNonExpired() { ... }
        //             @Override public boolean isEnabled() { ... }
        //         };
        //     }
        // };
        
        return new CustomUserDetailsService();
    }
    
    /**
     * AuthenticationProvider bean을 메서드에서 같이 처리한다.
     * 단순하게 주입받아서 처리 하려면 @see "AuthenticationManager가 사용할 AuthenticationProvider bean생성" 참조
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder   (passwordEncoder   ());
        /**
         * 보안을 위해서 자격증명 실패 BadCredentialException 으로 처리된다.
         * UsernameNotFoundException을 사용하기 위해 setHideUserNotFoundExceptions(false)한다.
         */
        authProvider.setHideUserNotFoundExceptions(false);
        
        List<AuthenticationProvider> authenticationProviderList = new ArrayList<>();
        authenticationProviderList.add(authProvider);
        
        ProviderManager authenticationManager = new ProviderManager(authenticationProviderList);
        authenticationManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher());
        
        return authenticationManager;
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // AuthenticationManager가 사용할 AuthenticationProvider bean생성
    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService());
    //     authProvider.setPasswordEncoder   (passwordEncoder   ());
    //     /**
    //      * 보안을 위해서 자격증명 실패 BadCredentialException 으로 처리된다.
    //      * UsernameNotFoundException을 사용하기 위해 setHideUserNotFoundExceptions(false)한다.
    //      */
    //     authProvider.setHideUserNotFoundExceptions(false);
    //     return authProvider;
    // }
    // AuthenticationProvider bean을 AuthenticationConfiguration 통해서 주입 후 AuthenticationManager bean 제공
    // AuthenticationManager bean생성
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
    //     return authConfiguration.getAuthenticationManager();
    // }
    // -----------------------------------------------------------------------------------------------------------------
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/", "/login").permitAll()
            
            .anyRequest().authenticated()
            .and().logout().logoutSuccessUrl("/login").permitAll()
            .and()
            .formLogin().permitAll()
                .loginPage("/login")
                .usernameParameter("email") // ID field name
                .passwordParameter("pass" ) // PW field name
                .defaultSuccessUrl("/member/user-info")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException exception) throws IOException, ServletException {
                        
                        logger.error("로그인 실패 :: " + exception.getMessage());
                        
                        response.sendRedirect("login?error");
                    }
                })
            .and()
            .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
                            , Authentication authentication) throws IOException, ServletException {
                        
                        logger.info("===== oauth2 로그인 성공 =====");
                        logger.info("{}, {}, {}", authentication.getName()
                                                , authentication.getDetails()
                                                , authentication.getPrincipal());
                        
                        response.sendRedirect("/member/user-info");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException exception) throws IOException, ServletException {
                        logger.error("===== oauth2 로그인 실패 =====");
                        logger.error("{}", exception.getMessage());
                        
                        response.sendRedirect("index");
                    }
                })
            ;
        
        return http.build();
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    // 2.7.x 오면서 ... extends WebSecurityConfigurerAdapter가 deprecated 되었다.
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.authenticationProvider(authenticationProvider());  // AuthenticationProvider 지정한다
    // }
    //
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.csrf().disable() // csrf토큰 비활성화(테스트시 걸어두는게 좋음)
    //             .authorizeRequests()
    //             .antMatchers("/","/auth/**","/js/**","/css/**","/image/**") // 접근허용
    //             .permitAll()
    //             .anyRequest()
    //             .authenticated()
    //         .and()
    //             .formLogin()
    //             .loginPage("/auth/loginForm")
    //             .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청된 로그인 정보를 가로채서 loadUserByName으로 제공한다.
    //             .defaultSuccessUrl("/"); // 정상일떄
    // 
    //     http.sessionManagement()
    //             .maximumSessions(1)
    //             .maxSessionsPreventsLogin(false);
    // }
    // -----------------------------------------------------------------------------------------------------------------
}
