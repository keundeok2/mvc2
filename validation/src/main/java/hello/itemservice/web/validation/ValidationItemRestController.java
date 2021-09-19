package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validtaion/api/items")
public class ValidationItemRestController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        /*
            - price를 qqq로 호출하면 400 bad request 발생
            -> HttpMessageConverter가 ItemSaveForm을 바인딩한 후 controller를 호출하는데, 바인딩조차 실패하여 컨트롤러를 호출하지 못함
            -> 바인딩에 성공한 후 검증로직을 처리함
                - @ModelAttribute는 HTTP 요청 파라미터를 각 필드단위로 처리하기때문에 어느 한 필드에서 타입오류가 발생해도 다른 필드를 검증한다
                - @RequestBody는 객체단위로 처리하기때문에 바인딩에 실패하면 Controller를 호출하지 않고 검증로직을 처리할 수 없다.
            -> 추후 예외처리 강의에서 해결방안 제시 (ConrollerAdvicde를 사용하지 않을까?)
         */

        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증오류 발생 errors = {}", bindingResult);
            return bindingResult.getAllErrors();
            /*
            bindingResult.getAllErrors() : 모든 ObjectError, FieldError 반환
             */
        }

        log.info("성공 로직 실행");
        return form;
    }
}
