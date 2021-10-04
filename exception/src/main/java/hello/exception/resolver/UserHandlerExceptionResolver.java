package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            if (ex instanceof UserException) {
                log.info("UserException resolver to 400 error");
                String acceptHeadder = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if (acceptHeadder.equals("application/json")) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(objectMapper.writeValueAsString(errorResult).toString());
                    return new ModelAndView();
                } else {
                    // accept가 text/html인 경우
                    return new ModelAndView("error/500");
                }
            }

            /*
                - 기존 MyHandlerExceptionResolver와 차이점
                기존에는 sendError()를 통해 에러를 WAS에 전달후 다시 WAS가 오류페이지 컨트롤러를 호출했던 것과는 다르게
                ExceptionResolver에서 response까지 모두 처리했기때문에 WAS는 오류페이지 컨트롤러를 다시 호출하지 않는다
                (ServletContainer까지 예외가 전달되면 다시 오류페이지 컨트롤러를 호출하는 번거로운 과정을 거치게 되지만,
                 ExceptionHandler를 사용하면 예외가 발생해도 ServletContainer까지 전달되지 않고 스프링MVC에서 모두 예외를 처리해버림.
                 WAS 입장에서는 정상처리흐름으로 받아들임)
             */

        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }
}
