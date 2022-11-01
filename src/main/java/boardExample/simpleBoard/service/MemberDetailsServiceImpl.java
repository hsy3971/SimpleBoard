package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
// 따로 만들어준 이유 :
// SecurityConfig의 configure method에서 MemberService를 참조하고 있고, MemberService에서 BCryptPasswordEncoder를 사용하기 위해 다시
// SecurityConfig를 참조하기 때문에 참조 사이클이 생겨 에러가 발생 -> UserDetailsService를 구현하는 Service를 따로 하나 구현하여 SecurityConfig에서 MemberService가 아닌 해당 Service를 참조하게 하면 해결
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //여기서 받은 유저 패스워드와 비교하여 로그인 인증
        Optional<Member> name = memberRepository.findByUid(username);
        Member member = name.get();
        if (member == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return member;
    }
}
