package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long uno;
    @NotEmpty(message = "아이디를 입력해주세요")
    private String uid;
//    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 입력해주세요.")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String u_pw;
    @NotEmpty(message = "이름을 입력해주세요")
    private String uname;
    private String u_auth;
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
    private String append_date;
    private String update_date;

//    dto -> Entity
    public Member toEntity() {
        Member build = Member.builder()
                .uno(uno)
                .uid(uid)
                .u_pw(u_pw)
                .uname(uname)
                .u_auth("USER")
                .email(email)
                .append_date(append_date)
                .update_date(update_date)
                .build();
        return build;
    }

    @Builder
    public MemberDto(Long uno, String uid, String u_pw, String uname, String u_auth, String append_date, String update_date, String email) {
        this.uno = uno;
        this.uid = uid;
        this.u_pw = u_pw;
        this.uname = uname;
        this.u_auth = u_auth;
        this.append_date = append_date;
        this.update_date = update_date;
        this.email = email;
    }

//    잘되나 확인해보기 -> CustomOAuth2UserService에 세션을 저장하기 위한 dto
    @Getter
    public static class UserSessionDto implements Serializable {

        private final Long uno;
        private final String uid;
        private final String u_pw;
        private final String uname;
        private final String u_auth;
        private final String append_date;
        private final String update_date;
        private final String email;

        /* Entity -> dto (Response) */
        public UserSessionDto(Member member) {
            this.uno = member.getUno();
            this.uid = member.getUid();
            this.u_pw = member.getU_pw();
            this.uname = member.getUname();
            this.u_auth = member.getU_auth();
            this.append_date = member.getAppend_date();
            this.update_date = member.getUpdate_date();
            this.email = member.getEmail();
        }
    }
}
