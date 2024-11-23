package study.spring_data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.spring_data_jpa.entity.Member;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }


}
