package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class ServletExceptionController {

    /*
      - Java 직접 실행의 경우
    - Java의 main method 직접실행 -> main Thread 실행됨
    - 프로그램 실행 도중 main 메서드까지 예외가 던져지면 예외 정보를 남기고 해당 쓰레드는 종료됨


      - 웹 어플리케이션의 경우
    - 웹 어플리케이션은 사용자 요청별로 별도의 쓰레드가 할당되고 서블릿 컨테이너 안에서 실행됨
      - 흐름
    - WAS <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러
    */

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생"); // -> HttpStatus 500
    }


    /*
    response.sendError(): 이 메서드를 호출한다고해서 당장 예외가 발생하는 것은 아니지만,
    서블릿 컨테이너에게 오류가 발생했다는 것을 전달할 수 있다.

      - sendError() 흐름
    - WAS <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError())
    - response.sendError()를 호출하면 respons 내부에는 오류가 발생했다는 상태를 저장함.
    서블릿 컨테이너는 클라이언트에 응답하기 전에 sendError() 호출 여불르 확인하고 오류코드에 맞추어 응답

    */

    /*
        - 스프링 부트는 기본으로 /error 오류페이지 경로로 설정한다.
          BasicErrorController를 에러컨트롤러로 제공한다.
          BasicErrorController가 제공하는 룰과 우선순위를 따라서 오류페이지 화면만 등록하면 된다.
     */

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws Exception{
        response.sendError(404, "404오류!");
    }
    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws Exception{
        response.sendError(400, "400오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws Exception {
        response.sendError(500, "500오류!");
    }
}
