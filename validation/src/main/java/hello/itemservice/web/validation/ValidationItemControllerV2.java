package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        /*
        @ModelAttribute -> Model에 자동으로 setAttribute
        @ModelAttribute Item item -> model.addAttribute("item", item)
         */

        /*
        파라미터 순서
        @ModelAttribute 뒤에 BindingResult

        BindingResult -> v1의 errors 역할
        BindingResult는 자동으로 화면으로 넘어감 Model에 담을 필요 없음

        @ModelAttribute Binding시 타입 오류가 있을 때
        BindingResult가 있다면 controller 호출, 오류 정보를 BindingResult에 담아서 return
        BindingResult가 없다면 400 BadRequest(controller 못 들어옴)

        BindingResult 대신 Errors 사용할 수 있음
        구현체가 BindingResult, Errors 둘 다 implements
        하지만 Errors는 단순 기능만 있음
         */


        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 하나의 특정 필드에 대한 에러 -> FieldError 사용, param (objectName: Model에 담기는 object)
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }

        if ( item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // globalError일 경우 -> ObjectError 사용
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /*
        field의 type이 달라 binding에 실패할 경우에는 spring FieldError를 생성하여 해당 field의 값을 저장한다.
        그 이후 controller method를 호출하여 BindingResult에 FieldError를 담는다.
        FieldError의 두번째 생성자로 객체를 생성하기 때문에 이전의 값을 뷰에 전달 할 수 있다.
         */



        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 하나의 특정 필드에 대한 에러 -> FieldError 사용, param (objectName: Model에 담기는 object)
            // FieldError 두번째 생성자를 사용하면 이전의 값을 유지하여 뷰에 전달할 수 있음
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }

        if ( item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // globalError일 경우 -> ObjectError 사용
                bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /*
         errormessage 관리: FieldError의 parameter codes, arguments를 통해서 errors.properties에서 errormessage를 찾아올 수 있음
         */

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if ( item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                // globalError일 경우 -> reject() 사용
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }


        // BindingResult는 target을 이미 알고 있음 (현재 케이스는 Item) addError의 objectName(target)을 이미 알고있다는 것임
        // 축약된 오류코드 => bindingResult의 addError() 대신 rejectValue(), reject()를 사용하여 objectName 자동으로 설정
        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());


        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // rejectValue의 errorCode 규칙 -> required.item.itemName에서 제일 앞 단어만 입력 ( required.item(target).itemName(field) )
            // 자세한 내용은 MessageCodesResolver 이해 (이후 강의)
            // fieldType은 param으로 넣지 않아도 해당 field의 타입으로 자동 등록되는 듯
            bindingResult.rejectValue("itemName", "required");
        }
        // 위의 검증 로직을 간편히 할 수 있는 utils
        //  ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");


        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if ( item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 검증 로직
        itemValidator.validate(item, bindingResult);

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

