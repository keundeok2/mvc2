package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Controller에서 ExceptionHandler 분리하고
 * ControllerAdvice에서 Exception을 처리함
 *
 * @ControllerAdvice에 대상을 적용하지 않으면 모든 컨트롤러에 적용된다.
 * @RestControllerAdvice는 @ControllerAdvice에 @ResponseBody만 추가되어있음
 *
 * - 대상 지정
 *
 * 애너테이션 지정
 * @ControllerAdvice(annotations = RestController.class)
 *
 * 패키지 지정(하위 패키지까지 모두 적용됨)
 * @ControllerAdvice("org.example.controllers")
 *
 * 클래스 지정
 * @ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
 *
 */
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {

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


}
