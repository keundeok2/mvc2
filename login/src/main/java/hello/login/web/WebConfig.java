package hello.login.web;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // 필터 등록
        filterRegistrationBean.setOrder(1); // 필터 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 필터 url 패턴

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean logCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter()); // 필터 등록
        filterRegistrationBean.setOrder(2); // 필터 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 필터 url 패턴

        return filterRegistrationBean;
    }




}
