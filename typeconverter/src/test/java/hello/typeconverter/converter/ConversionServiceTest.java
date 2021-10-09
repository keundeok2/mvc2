package hello.typeconverter.converter;

import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionServiceTest {

    @Test
    void conversionService() {

        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConveter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 사용
        // param: source, result type
        // ConversionService가 souce와 result type 정보를 보고 자동으로 converter를 찾아서 타입 변환
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");
        assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(new IpPort("127.0.0.1", 8080));
        assertThat(conversionService.convert(new IpPort("127.0.0.1", 8080), String.class)).isEqualTo("127.0.0.1:8080");


        /*
            - DefautConversionService 는 ISP(Interface Separatino Principal)이 적용되어있다.
            ConversionService, ConversionRegistry를 상속받는데, ConversionService는 Converter를 사용하는 기능,
            ConversionRegistry는 Converter를 등록하는 기능으로 분리되어있기 때문에 사용자는 DefaultConversionService에 등록된
            Converter를 사용하고 싶다면 ConversionService 인터페이스에만 의존하면 되고, 사용에 필요한 메서드만 알게 된다.
            이렇게 기능에 따라 인터페이스를 분리하는 것을 ISP라고 한다.
            (ConversionRegistry의 기능이 변경되더라도 ConversionService는 그대로이기 때문에 사용자는 코드를 변경하지 않아도 된다.)
         */


    }
}
