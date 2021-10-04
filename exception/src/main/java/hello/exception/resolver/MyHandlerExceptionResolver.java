package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {


            if (ex instanceof IllegalArgumentException) {
                log.info("Illegal resolve to 400");
                // sendError() 사용하므로써 예외를 먹어버림.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                // 비어있는 ModelAndView를 return 하므로써 Servletcontainer까지 예외가 없는 정상적인 return, View는 렌더링 되지 않음
                return new ModelAndView();

                // 이제 예외를 먹어버리고, 400 에러로 리턴했으니 클라이언트는 400 에러를 전달 받게 됨
                // 위의 처리를 하지 않으면 500 에러
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("resolver ex", e);
        }

        // null로 return 시 ServletContainer까지 기존 예외가 전달
        // ModelAndView에 Model, View를 지정하면 View 렌더링
        return null;
    }
}
