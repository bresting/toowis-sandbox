package toowis.sandbox.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import toowis.sandbox.dto.Member;

@Mapper
public interface MemberMapper {
    //@Select("SELECT mb_password as mbPassword, mb_email as mbEmail, mb_name as mbName, mb_role as mbRole FROM TB_MEMBER WHERE MB_EMAIL = #{mb_email}")
    @Select("SELECT * FROM TB_MEMBER WHERE MB_EMAIL = #{mb_email}")
    Optional<Member> findByEmail(@Param("mb_email") String mbEmail);
}
