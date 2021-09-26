package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    // Override 메서드 생성 단축키: ctrl + o

    /*
            - 스프링 인터셉터
        - 인터셉터는 스프링 MVC에 특화된 필터 기능을 제공한다.

        - 인터셉터 흐릅: 인터셉터는 디스패처 서블릿과 컨트롤러 사이에서 호출된다.
        HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러

        - URL패턴을 필터보다 더욱 정밀하게 설정할 수 있다.

        - 인터셉터 제한, 체인은 필터와 동일하게 사용할 수 있다.

        - 필터는 단순히 doFilter()만 제공했다면, 인터셉터는 컨트롤러 호출 전 preHandle(),컨트롤 호출 후 postHandle(),용
         http 요청 완료 이후러(뷰 렌더링 이후) afterCompletion()처럼 단계적으로 세분화 되어있다.

        - postHandle() vs afterCompletion()
          컨트롤러에서 예외가 발생하면 postHandle()은 호출되지 않는다. 하지만 afterCompletion()은 호출된다. 예외를 param으로 받아서
          로깅도 할 수 있다.
     */


    private static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // handler: 여러 종류의 핸들러를 사용할 수 있도록 유연하게 만들어져 있다.
        // 현재는 handler == controller


        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        // @RequestMapping으로 요청을 받는 핸들러: HandlerMapping (MVC 1 강의!)
        // 정적 리소스를 사용하는 핸들러: ResourceHttpRequestHandler
        if (handler instanceof HandlerMapping) {
            HandlerMethod hm = (HandlerMethod) handler; // 호출할 컨트롤 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}] [{}] [{}]", uuid, requestURI, handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle() mav: [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String)request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}] [{}] [{}]", uuid , requestURI, handler);

        if (ex != null) {
            log.error("afterCompetion Error", ex);
        }


    }
}
