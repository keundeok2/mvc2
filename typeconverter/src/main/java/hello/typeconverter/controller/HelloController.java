package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // 문자 타입으로 조회
        Integer intValue = Integer.valueOf(data);// 숫자 타입으로 변경
        System.out.println("intValue = " + intValue);

        /*
            HTTP 요청 파라미터는 모두 문자로 처리된다. 따라서 다른 타입으로 변경하려면 변환과정을 거쳐야한다.
        */
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println("data= "+ data);
        return "ok";

        /*
         HTTP 쿼리스트링으로 전달되는 데이터 10은 문자 10이다.
         스프링이 중간에서 타입변환을 해주어서 컨트롤러에서 Integer로 받을 수 있다.
         @ModelAttribute, @PathVariable도 마찬가지로 적용된다

         - 개발자가 직접 타입 컨버터 확장 가능. Converter 인터페이스를 구현해서 등록
         */
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort IP = " + ipPort.getIp());
        System.out.println("ipPort port = " + ipPort.getPort());
        return "ok";

        /*
            -@RequestParam을 처리하는 ArgumentResolver에서 ConversionService를 사용해서 타입을 변환한다.
         */

        /*
        @NumberFormat: 숫자 관련 형식을 지정하는 포맷터를 사용.
        @DateTimeFormat: 날짜 간련 형식을 지정하는 포맷터를 사용.
         */
    }

}
