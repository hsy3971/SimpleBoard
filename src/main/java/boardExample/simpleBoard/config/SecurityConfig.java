package boardExample.simpleBoard.config;

//import boardExample.simpleBoard.service.CustomOAuth2UserService;
import boardExample.simpleBoard.service.CustomOAuth2UserService;
import boardExample.simpleBoard.service.MemberDetailsServiceImpl;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity        //spring security 를 적용한다는 Annotation
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberDetailsServiceImpl memberDetailsService;

    /* OAuth */
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder encryptPassword() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 규칙 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable() //로그인 창(사이트 간 요청 위조(Cross-Site Request Forgery) 공격 방지 기능 키기)
                .authorizeRequests()
                    .antMatchers( "/login", "/signup","/boards", "/access_denied", "/resources/**").permitAll()
                    // USER, ADMIN 접근 허용(보류)
                    .antMatchers("/userAccess").hasRole("USER")
                    .antMatchers("/userAccess").hasRole("ADMIN")
                    .antMatchers("/userAccess").hasRole("SOCIAL")
                    .and()
                .formLogin()
                    .loginPage("/login")
                    //구현한 로그인 페이지
                    .loginProcessingUrl("/login_proc")
                    //로그인 성공시 제공할 페이지
                    .defaultSuccessUrl("/boards")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .and()
                /* OAuth */
                .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/boards")
                    .userInfoEndpoint() // OAuth2 로그인 성공 후 가져올 설정들
                    .userService(customOAuth2UserService); // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
    }

    /**
     * 로그인 인증 처리 메소드
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberDetailsService).passwordEncoder(encryptPassword());
    }
}
