package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    /*
    ArgumentResolver: Args를 개발자가 직접 컨트롤하기 위함
     */


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        // Controller 호출 전 @Login이 있는지 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        // Class.isAssignableFrom: 특정 Class가 어떤 클래스/인터페이스를 상속/구현 했는지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        // return true이면 resolveArgument() 실행
        return hasLoginAnnotation && hasMemberType;
    }

    // resolveArgument() 컨트롤러 호출 직전에 호출되어 필요한 파라미터 정보를 생성해준다.
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        log.info("resolveArgument()");

        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("session == null");
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
