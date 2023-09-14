package toowis.sandbox.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import toowis.sandbox.dto.Member;
import toowis.sandbox.repository.MemberMapper;

@Service
public class MyUserService {
    
    private static final Logger logger = LoggerFactory.getLogger(MyUserService.class);
    
    @Autowired AuthenticationManager authenticationManager;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired private MemberMapper userMapper;
    
    @Transactional
    public void userModify(Member user) {
        userMapper.findByEmail(user.getMbEmail()).orElseThrow(()->{
            logger.debug("미 등록된 사용자 - 등록로직 추가");
            return new UsernameNotFoundException(user.getMbEmail() +"이 존재하지 않습니다.");
        });
        
        // DB저장
        String encPassword = bCryptPasswordEncoder.encode(user.getMbPassword());
        userMapper.updatePassword(user.getMbEmail(), encPassword);

        //세션등록
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getMbEmail(), user.getMbPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
