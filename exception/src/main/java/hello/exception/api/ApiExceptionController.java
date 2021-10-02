package hello.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
            // JSON이 아닌 미리 설정되어있는 오류페이지가 반환된다.
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



    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;

    }
}
