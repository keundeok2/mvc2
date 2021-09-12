package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {

        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        /*
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }

        결과
        messageCode = required.item
        messageCode = required

        디테일한 메시지가 먼저, 범용적인 메시지가 나중에 출력된다.

        BindingResult의 rejectValue(), reject()는 내부적으로 MessageCodesResolver를 사용하여 메시지코드를 등록
        MessageCodesResolver에서 생성된 String 배열을 ObjectError의 codes에 등록하는 것임
        -> new ObjectError("item", new String[]{"required.item", "required"}, null, null);



         */

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        /*
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        결과:
        messageCode = required.item.itemName
        messageCode = required.itemName
        messageCode = required.java.lang.String
        messageCode = required
         */

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );

    }


}
