package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * ExceptionHandlerExceptionResolver가 애너테이션을 확인한 후 정상흐름으로 return (Status: 200 OK)
     *
     * 처리하는 Exception의 자식까지 모두 처리함
     *
     * 컨트롤러에서 받는 arguments 대부분을 받아서 사용할 수 있다. 또한 return value도 컨트롤러와 비슷함
     * 컨트롤러 메서드와 매우 유사함. 참고)
     * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-exceptionhandler-args
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HttpStatus도 변경하고 싶으면 추가
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptinoHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler // 파라미터 생략가능. 메서드의 파라미터로 받는 Exception을 처리함
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptinoHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptinoHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }


    @GetMapping("/api2/members/{id}")
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

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;

    }


}
