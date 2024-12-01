package study.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.spring_data_jpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
// Spring repository 이름+Impl 이 클래스 이름이어야함
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
