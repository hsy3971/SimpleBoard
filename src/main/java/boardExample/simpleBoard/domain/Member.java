package boardExample.simpleBoard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uno;
    @Column(nullable = false)
    private String uid;
    private String u_pw;
    @Column(nullable = false)
    private String uname;
    @Column(nullable = false)
    private String u_auth;

    private String email;
    private String append_date;
    private String update_date;

//    Spring은 Front로 데이터를 보낼 때 Json으로 보내야하는 상황(RESTAPI postman)이면 Jackson을 통해 Json 형태로 변환하는데 순환구조일 경우 에러가 떠버린다.
//    위의 코드처럼 해당 연관관계 매핑 부분에 @JsonIgnore을 붙여주면 순환 참조 관계를 막을 수 있다
    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Member(Long uno, String uid, String u_pw, String uname, String u_auth, String append_date, String update_date, String email) {
        this.uno = uno;
        this.uid = uid;
        this.u_pw = u_pw;
        this.uname = uname;
        this.u_auth = u_auth;
        this.append_date = append_date;
        this.update_date = update_date;
        this.email = email;
    }

//  이미 여기서부터 잘못됨... 오류가 Timestamp 솰라솰라했는데 여기부터 천천히 다시 실행보기

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist(){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
        Date time = new Date();
        this.append_date = format.format(time);
        this.update_date = this.append_date;
    }
    /* 해당 엔티티를 업데이트 하기 이전에 실행*/
    @PreUpdate
    public void onPreUpdate(){
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
        Date time = new Date();
        this.update_date = format.format(time);
    }
    /* 소셜로그인시 이미 등록된 회원이라면 수정날짜만 업데이트하고
    *  기존 데이터는 그대로 보존하도록 예외처리 */
    public Member updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    /* 유저의 권한 목록 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.u_auth));
    }

    @Override
    public String getPassword() {
        return this.u_pw;
    }

    // 시큐리티의 userName
    // -> 따라서 얘는 인증할 때 id를 봄
    @Override
    public String getUsername() {
        return this.uid;
    }

    // Vo의 userName
    public String getUserName(){
        return this.uname;
    }
    /* 계정 만료 여부
    *  true : 만료 안됨
    *  false : 만료
    */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /* 계정 잠김 여부
     *  true : 잠기지 않음
     *  false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /* 비밀번호 만료 여부
     *  true : 만료 안됨
     *  false : 만료
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /* 사용자 활성화 여부
     *  true : 만료 안됨
     *  false : 만료
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
