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
    // 기본 고유값인 Long형을 넘겨주는게 맞겠지? getU_id가 의미하는건 진짜 String형의 아이디를 뜻하기 때문에 맞지 않을것으로 추정
    @Transactional
    public Long joinMember(MemberDto memberDto) {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
        Date time = new Date();
        String localTime = format.format(time);

        // 비밀번호를 암호화하는 과정
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //DB에 저장시키고 회원가입을 시킨다
        //이게 결국 toEntity인데 이렇게 다시 해줘야하나?
        //MemberDto로 파라미터를 받았지만 저받은것을 다시 member라는 객체를 생성해줘서 builder에다가 memberDto의 getter들을 넣어준다.
        // 그리고 joinMember의 파라미터로 넘긴다.(joinMember의 매개변수타입이 Member형이기 떄문에 이렇게 형변환을 해준뒤 넘겨줘야한다)

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
    // 유효성 검사 : @NotEmpty
    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

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

    //아이디중복검사
    public void DuplicateMember(MemberDto member) {
        Optional<Member> uid = memberRepository.findByUid(member.getUid());
        if (!uid.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
