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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    // 해당 엔티티를 저장하기 이전에 실행
    @PrePersist
    public void onPrePersist(){
        this.append_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.update_date = this.append_date;
    }
    // 해당 엔티티를 업데이트 하기 이전에 실행
    @PreUpdate
    public void onPreUpdate(){
        this.update_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    /* 소셜로그인시 이미 등록된 회원이라면 수정날짜만 업데이트하고
     *  기존 데이터는 그대로 보존하도록 예외처리 */
    public Member updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    // 유저의 권한 목록(어떤 권한을 가졌는지 return해준다.)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.u_auth));
    }

    @Override
    public String getPassword() {
        return this.u_pw;
    }

    @Override
    public String getUsername() {
        return this.uid;
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
