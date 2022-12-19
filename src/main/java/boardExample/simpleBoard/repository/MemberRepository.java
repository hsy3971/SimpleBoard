package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    Member findByUname(String uname);
    Member findByUno(Long uno);
    boolean existsByUid(String uid);
    /* OAuth */
    Optional<Member> findByEmail(String email);
}
