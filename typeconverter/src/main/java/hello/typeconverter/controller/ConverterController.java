package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
    - HttpMessageConverter에는 컨버전 서비스가 적용되지 않는다.
    JSON을 객체로 변환하거나 객체를 JSON으로 변환할 때에는 HttpMessageConverter를 사용하지 않는다.
    해당 라이브러리가 제공하는 설정을 변경해야함

    컨버전 서비스는 @RequestParam, @ModelAttribute, @PathVariable, 뷰 템플릿에서 사용할 수 있음
 */

@Controller
public class ConverterController {

    @GetMapping("/test")
    @ResponseBody
    public String test(@RequestParam String hello) {
//        System.out.println("body: " + body);
        System.out.println("hello: " + hello);
        return "ok";
    }

    @Data
    static class Body {
        private String name;
        private int age;
    }

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
        return "converter-view";
    }


    @GetMapping("/converter/edit")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        Form form = new Form(ipPort);
        model.addAttribute("form", form);
        return "converter-form";
    }

    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute Form form, Model model) {
        // @ModelAttribute에서 String -> IpPort로 변환
        IpPort ipPort = form.getIpPort();
        model.addAttribute("ipPort", ipPort);
        return "converter-view";
    }

    @Data
    static class Form {
        private IpPort ipPort;

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }

}
