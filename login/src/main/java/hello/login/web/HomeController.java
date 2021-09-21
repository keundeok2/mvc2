package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

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

    @GetMapping("/")
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

}