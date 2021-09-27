package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리 TODO

        // 만료시간(maxAge) 정하지 않으면 세션쿠키(브라우저 종료시 쿠키 삭제)
        // 쿠키를 클라이언트에 전송하면 모든 request에 쿠키가 전달됨
        Cookie cookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(cookie);

        /*
            - 쿠키의 보안 문제
            1. 브라우저에서 쿠키의 값을 임의로 변경하여 서버로 전송할 수 있음
            2. 쿠키는 브라우저에 저장되기때문에 쿠키에 보관된 정보가 탈취될 수 있음
            - 대안
            1. 사용자 별 예측 불가능한 토큰을 노출시키고, 서버에서 토큰과 사용자ID를 매핑하여 사용
            2. 예측불가능한 값(토큰)을 쿠키에 담아야한다.
            3. 토큰의 만료시간을 지정해야함

            => Session 사용하여 해결
         */

        return "redirect:/";

    }

//    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공처리
        // 세션매니저를 통한 세션생성, 회원 데이터 저장
        sessionManager.createSession(loginMember, response);

        return "redirect:/";

    }

//    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공처리
        // request.getSession() -> 세션이 있으면 세션 반환, 없으면 신규 세션 반환
        // 파라미터 true -> 신규 세션 반환 (default: true)
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공처리
        // request.getSession() -> 세션이 있으면 세션 반환, 없으면 신규 세션 반환
        // 파라미터 true -> 신규 세션 반환 (default: true)
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }

//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response);
        return "redirect:/";
    }
//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0); // 쿠키를 지우고 싶으면 maxAge = 0
        response.addCookie(cookie);
    }


}
