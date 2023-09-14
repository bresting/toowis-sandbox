package toowis.sandbox.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import toowis.sandbox.dto.Member;
import toowis.sandbox.repository.MemberMapper;

/**
 * <pre>
 * UserDetailsService ----------> UserDetails --> {사용자정보}
 *       └─ loadUserByUsername
 * </pre>
 */
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    private MemberMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // TODO 테스트
        // if ("bresting@gmail.com".equals(username)) {
        //     User user = new User();
        //     user.setId(1);
        //     user.setPassword("$2a$10$E2y9hpP.ymhrJn9DBehwP.h2Ta4w.7JlOCFJKWNQpwjEj.ZR3H2uq");
        //     user.setEmail("bresting@gmail.com");
        //     user.setName("사용자명");
        //     user.setProvider(AuthProvider.LOCAL);
        //     user.setRole(Role.ROLE_USER);
        //     return new DBManagedUserDetails(user);
        // }
        
        Optional<Member> memberMaper = userMapper.findByEmail(username);

        if ( memberMaper.isPresent() ) {
            logger.debug("기 등록된 사용자 - {}", memberMaper.get());
        } else {
            logger.debug("미 등록된 사용자 - 등록로직 추가");
            throw new UsernameNotFoundException(username +"이 존재하지 않습니다.");
        }
        
        return new DBManagedUserDetails(memberMaper.get());
    }
    
    /**
     * UserDetails를 implements 해서 Spring security가 사용 할 수 있도록 한다.
     * 
     * 추가적으로 사용자정보 "toowis.sandbox.dto.Member"와 같이 담을 수 있다.
     * 사용할 때는 DBManagedUserDetails 캐스팅 해서 접근한다.
     */
    public static class DBManagedUserDetails implements UserDetails {
        
        private static final long serialVersionUID = 1L;
        
        private Member member;
        
        public DBManagedUserDetails(Member user) {
            this.member = user;
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // authorities.add(new SimpleGrantedAuthority(member.getRole().name()));
            // Set<Role> roles = user.getRoles();
            // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            // for (Role role : roles) {
            //     authorities.add(new SimpleGrantedAuthority(role.name()));
            // }
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String mbRole = member.getMbRole();
            Optional.of(mbRole).ifPresent(t -> {
                String[] tmp = t.split(";");
                for (String role : tmp) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            });
            return authorities;
        }

        @Override
        public String getPassword() {
            return member.getMbPassword();
        }

        @Override
        public String getUsername() {
            return member.getMbName();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}

