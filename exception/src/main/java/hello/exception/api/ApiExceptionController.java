package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
            // JSON이 아닌 미리 설정되어있는 오류페이지가 반환된다.
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id );
    }


    /*
        - 스프링부트 기본 에러컨트롤러 BasicErrorController를 사용

          produce에 따른 메서드가 정의되어있음
          errorHtml(): Accept 헤더 값이 text/html 인 경우 ModelAndView 반환
          error(): 그 외의 경우 HttpBody에 JSON 데이터 반환

          BasicErrorController를 확장하여 JSON 데이터의 내용을 변경할 수도 있지만, 실제로는 사용X
          대신 @ExceptionHandler 사용한다

     */


    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    /**
     * 개발자가 직접 수정할 수 없어 @ResponseStatus를 붙이지 못하는 Exception의 경우에는 ResponseStatusException을 생성해서 return
     */
    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    /**
     * DefaultHandlerExceptionResolver: 스프링 내부 예외가 처리되어있는 resolver
     * 아래의 예는 RequestParam이 문자열로 들어와 TypeMismatchException이 발생했을 때
     * WAS까지 예외를 전달하지 않고, 400 bad request로 변환한다
     */
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;

    }
}
