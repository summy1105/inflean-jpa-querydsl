package study.spring_data_jpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.spring_data_jpa.entity.Member;
import study.spring_data_jpa.entity.Team;

// jpa criteria 기술 : 실무에서 복잡해서 사용하지 않음
public class MemberSpec {

    public static Specification<Member> teamName(final String teamName) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(teamName)) {
                return null;
            }
            Join<Member, Team> t = root.join("team", JoinType.INNER);
            return builder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, builder) ->builder.equal(root.get("username"), username);
    }
}
