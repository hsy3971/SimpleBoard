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
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

//    loadUserByUsername 메소드에서 유저의 id를 통해 member를 찾고 그 값이 없으면
//    UsernameNotFoundException예외를 발생시킨다.
//    그렇지 않은 경우에는 UserDetails(Member) 객체를 생성해서 리턴한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> name = memberRepository.findByUid(username);
        Member member = name.get();
        if (member == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return member;
    }
}
