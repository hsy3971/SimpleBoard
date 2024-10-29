package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    @Transactional
    public Long joinMember(MemberDto memberDto) {
        String localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        Member member = Member.builder()
                .uno(memberDto.getUno())
                .uid(memberDto.getUid())
                .u_auth("USER")
                .u_pw(encoder.encode(memberDto.getU_pw()))
                .uname(memberDto.getUname())
                .email(memberDto.getEmail())
                .append_date(localTime)
                .update_date(localTime)
                .build();

        return memberRepository.save(member).getUno();
    }

    @Transactional
    public boolean existsByMemberId(String uid){
        return memberRepository.existsByUid(uid);
    }
//  회원가입시 유효성 검사
    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        // 유효성 검사에 실패한 필드 목록을 받음
        for(FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Transactional
    public Member findByUno(Long id) {
        return memberRepository.findByUno(id);
    }

    @Transactional
    public Optional<Member> findByUid(String uid) {
        return memberRepository.findByUid(uid);
    }

    //아이디 중복검사
    public void DuplicateMember(MemberDto member) {
        Optional<Member> uid = memberRepository.findByUid(member.getUid());
        if (uid.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
