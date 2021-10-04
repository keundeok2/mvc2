package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    /*
           -오류페이지 작동 원리

        - Exception이 WAS까지 전달되면, ErrorPage 경로를 다시 요청한다. (ex. /error-page/500)
        (필터, 인터셉터 모두 호출됨 -> 이후 강의에서 디테일 설명)

        - 단순히 ErrorPage 경로를 다시 요청하는 것이 아니라 오류 정보를 request의 attribute에 추가해서 넘겨준다.

     */

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    /*
    produce -> Http 요청의 accept type에 따라서 컨트롤러의 호출을 분기할 수 있음
              accept=application/json 요청 -> produce=application_json인 컨트롤러 메서드가 우선순위로 호출됨
     */
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response) {

        log.info("api errorpage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }




    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE)); // ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType()); // 중요해 이후 강의에서 설명
        /*
            - 에러가 발생했을 때 다시 필터와 인터셉터를 거칠 필요가 없을 때?
        dispatchType -> http 요청이 WAS 외부에서 발생한 것인지 내부에서 발생한 것인지 판별할 수 있음
        에러 페이지에서: dispatchType = ERROR
        클라이언트에서: dispatchType = REQUEST

            - DispatchType
        REQUEST: 클라이언트 요청
        ERROR: 오류 요청
        FORWARD: 서블릿에서 다른 서블릿이나 JSP를 호출할 때
        INCLUDE: 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때
        ASYNC: 서블릿 비동기 호출
         */

    }

}
