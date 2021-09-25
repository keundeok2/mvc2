package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    /*
        웹 관련 공통된 관심사가 있을 경우 서블릿 필터 또는 스프링 인터셉터를 사용한다.
        AOP를 사용할 수도 있지만 필터와 인터셉터는 HttpServletRequest를 지원하기 때문에 사용하기 더 좋음.

        - 필터 흐름
        HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러

        위의 서블릿은 디스패처 서블릿

        - 제한된 필터 흐름
        HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 //로그인 사용자
        HTTP 요청 -> WAS -> 필터(적절하지 않은 요청이라 판단, 서블릿 호출X) //비 로그인 사용자

        - 필터체인으로 여러 필터를 등록할 수 있음
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LogFilter doFilter");

        // ServletRequest 는 Http가 아닌 프로토콜도 고려하여 만들어진 것임. HttpServletRequest로 캐스팅하기
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}], [{}]", uuid, requestURI);
            // chain.doFilter를 호출해야 다음 필터를 호출. 다음 필터가 없으면 서블릿 호출. 이 메서드를 호출하지 않으면 다음단계로 진행되지 않음
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            // finally 블럭은 반드시 호출되기 때문에 서블릿의 모든 로직이 끝난 후에 호출됨
            log.info("RESPONSE [{}], [{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("LogFilter destory");
    }
}
