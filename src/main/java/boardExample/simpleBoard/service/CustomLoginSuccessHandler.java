package boardExample.simpleBoard.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 세션에 사용자 정보 저장 로직을 호출하는 메서드
        storeUserSessionInfo(request, authentication);
        // 로그인 후 리다이렉트 처리
        response.sendRedirect("/boards");
    }

    public void storeUserSessionInfo(HttpServletRequest request, Authentication authentication) {
        // 이 메서드에서 필요한 데이터를 가져와 세션에 저장합니다.
        String uid = authentication.getName();
        // 예를 들어, 사용자 정보를 직접 설정하는 로직을 작성합니다.
        request.getSession().setAttribute("uid", uid);
    }
}
