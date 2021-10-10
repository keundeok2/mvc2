package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

    /*
    - Formatter vs Converter
        Converter: 범용적 (객체 -> 객체)
        Formatter: 문자에 특화(문자 -> 객체, 객체 -> 문자) + Locale(현지화)
     */

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        // "1,000" -> 1000
        return NumberFormat.getInstance(locale).parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        return NumberFormat.getInstance(locale).format(object);
    }

    /*
    컨버전 서비스에는 컨버터만 등록할 수 있고, 포맷터는 등록할 수 없지만, 포맷터를 지원하는 컨버전 서비스를 사용하면 됨.
    FormattingConversionService: 포맷터를 지원하는 컨버전 서비스

     */
}
