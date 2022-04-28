package toowis.sandbox.mapper;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import toowis.sandbox.dto.Member;
import toowis.sandbox.repository.MemberMapper;;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@MybatisTest
public class MapperTest {
    
    @Autowired private MemberMapper memberMapper;
    
    @Test
    @DisplayName("단순조회")
    public void testSelectSimple() {
        Optional<Member> result = memberMapper.findByEmail("bresting@gmail.com");
        
        if (result.isPresent()) {
            System.out.println(result.get());
        }
    }
}
