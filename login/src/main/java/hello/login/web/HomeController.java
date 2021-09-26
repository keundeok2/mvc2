package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    /*
      -package 구조
        - domain : 화면, UI, 기술인프라 등을 제외한 핵심 비즈니스 업무 영역
        향후 web을 다른 기술로 바꾸어도 도메인은 그대로 유지할 수 있어야 한다.
        web은 domain을 알고 있지만, domain은 web을 알지 못하도록 의존관계 설계
         -> 그래서 ItemRepository에서 ItemSaveForm으로 인자를 받아 상품을 저장하면 안했던 것
     */

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(value = "memberId", required = false) Long memberId, Model model) {
        /*
            - @CookieValue -> 해당하는 이름의 쿠키의 값을 가져옴
            스프링이 쿠키 memberId는 String이지만 Long으로 변환해준다
         */

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션관리자에 저장된 회원정보 가져오기
        Member member = (Member)sessionManager.getSession(request);

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 파라미터 false -> 로그인 하지않고 단순히 홈으로 들어온 사용자도 세션이 생성되기 때문
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 값이 없으면 홈으로 이
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인 홈으로 이동동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
        // @SessionAttribute는 세션을 새로 생성하지는 않는다.
        /*
        브라우저를 실행하고 최초 로그인 시 url끝에 jsessionid querystring이 붙는 이유
        -> 웹 브라우저가 쿠키를 지원하지 않는 경우 url을 통해 session을 유지시키는 방법
        -> 현실적으로 사용 불가능
        -> 사용 중지: server.servlet.session.tracking-modes=cookie
         */


        // 세션에 값이 없으면 홈으로 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인 홈으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        log.info("homeLogin() loginMember: {}", loginMember);

        // 세션에 값이 없으면 홈으로 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인 홈으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

}