package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ResponseStatusExceptionResolver가 httpcode를 설정해준다. message도 설정(String 또는 messages.properties의 code로 사용가능 )
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException{
}
