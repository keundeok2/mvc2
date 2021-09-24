package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없읍니다";
        }
        // 세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name: {}, value: {}", name, session.getAttribute(name)));


        log.info("session={}", session.getId()); // 세션 ID. JSESSIONID
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval()); // 세션 유효시간
        // 시간을 Long으로 출력 -> Date 생성
        log.info("getCreationTime={}", new Date(session.getCreationTime())); // 세션 생성시간
        log.info("getLastAccessedTime={}", new Date(session.getLastAccessedTime())); // 세션 마지막 접근시간
        log.info("isNew={}", session.isNew()); // 새로 생성된 세션인지 여부

        // 세션의 종료시점은 서버에 가장 최근에 요청한 시점으로부터 계산한다

        return "세션 출력";
    }


}
