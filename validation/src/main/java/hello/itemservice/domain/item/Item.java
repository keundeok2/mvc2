package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000원 넘게 넣어주세요")
// 서로 다른 객체의 필드를 조합할 경우 사용하기 번거로움. 차라리 Object Error만 직접 검증하는 식으로 가는 것이 좋음
public class Item {

    /*
    같은 모델 객체를 사용하는 등록과 수정에서, 검증 로직이 서로 다를 경우에는 어떻게 할 것인지?
    ->  1. groups 사용
    -> 실무에서 주로 사용 2. Item을 직접 사용하지 않고 ItemSaveForm, ItemUpdateForm과 같이 별도의 모델객체를 만들어 각각 사용

     */

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}) // 빈 값 혹은 공백만 있는 경우를 허용 X
    private String itemName;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) // null 허용 X
//    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class}) // 범위 안의 값만 허용
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class) // 최대 9999까지 허용
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Item(long id, String itemName, Integer price, Integer quantity) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
