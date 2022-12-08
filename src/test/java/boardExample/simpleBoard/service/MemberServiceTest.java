package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        MemberDto memberDto = new MemberDto(null,"fodb12","1234","홍성윤", "USER", null, null, "fodb@naver.com");
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
        Date time = new Date();
        String localTime = format.format(time);
        //when
        Member member = Member.builder()
                .uno(memberDto.getUno())
                .uid(memberDto.getUid())
                .u_auth("USER")
                .u_pw((memberDto.getU_pw()))
                .uname(memberDto.getUname())
                .email(memberDto.getEmail())
                .append_date(localTime)
                .update_date(localTime)
                .build();
        Member user = memberRepository.save(member);
        Member result = memberRepository.findByUno(user.getUno());
        //then
        assertEquals(member, result);
    }
    @Test
    public void 중복회원검증() throws Exception {
        MemberDto memberDto = new MemberDto(null,"fodb12","1234","홍성윤", "USER", null, null, "fodb@naver.com");
        MemberDto memberDto2 = new MemberDto(null,"fodb12","1234","홍성윤", "USER", null, null, "fodb@naver.com");
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
        Date time = new Date();
        String localTime = format.format(time);
        //when
        Member member = Member.builder()
                .uno(memberDto.getUno())
                .uid(memberDto.getUid())
                .u_auth("USER")
                .u_pw((memberDto.getU_pw()))
                .uname(memberDto.getUname())
                .email(memberDto.getEmail())
                .append_date(localTime)
                .update_date(localTime)
                .build();
        memberRepository.save(member);
        try {
            memberService.DuplicateMember(memberDto2);
        }
        catch (IllegalStateException e) {
            return;
        }
    }
}