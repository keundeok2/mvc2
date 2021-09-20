package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static
    private static long sequence = 0L; // static

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        /*
        List<Member> members = findAll();
        for (Member m : members) {
            if (m.getLoginId().equals(loginId)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
        */

        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId)) // filter 만족한 값만 다음 단계로. 만족하지 않은 값은 버려짐
                .findFirst();
    }


    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }




}
