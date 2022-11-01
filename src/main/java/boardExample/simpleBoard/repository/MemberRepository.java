package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //JPA를 사용할때는 Java의 변수명을 가지고 매핑하기 때문에 변수명을 오타없이 주의해서 작성해야 함(자동으로 쿼리를 날려줌 -> JPA에 특징)
    // 위의 이유로 u_name -> uname으로 바꿨다. 근데 생각해보니까 username을 할게아니라 u_id를 바꿔야하는거 아닌가?
    Optional<Member> findByUid(String uid);
    Member findByUname(String uname);
    Member findByUno(Long uno);
    boolean existsByUid(String uid);
    /* OAuth */
    Optional<Member> findByEmail(String email);
}
