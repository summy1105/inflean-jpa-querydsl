package hello.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.h2.util.StringUtils;

import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            jpqlExample(entityManager);
//            criteriaExample(entityManager);
            nativeQueryExample(entityManager);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }

    private static void jpqlExample(EntityManager entityManager) {
        List<Member> memberList = entityManager.createQuery(
                "select m from Member m where m.username like '%kim%'"
                , Member.class
        ).getResultList();

        memberList.stream().forEach(System.out::println);
    }

    private static void criteriaExample(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
        Root<Member> m = query.from(Member.class);
        CriteriaQuery<Member> criteriaQuery = query.select(m);

        String username = "kim";
        if ( !StringUtils.isNullOrEmpty(username)) {
            criteriaQuery = criteriaQuery.where(criteriaBuilder.equal(m.get("username"), username));
        }

        List<Member> memberList = entityManager.createQuery(criteriaQuery).getResultList();

        memberList.stream().forEach(System.out::println);
    }

    private static void nativeQueryExample(EntityManager entityManager) {
        Member member = new Member();
        member.setUsername("kim");
        entityManager.persist(member);

        List<Member> memberList = entityManager.createNativeQuery(
                "select m.*" +
                        " from Member m " +
                        " where m.username like '%kim%'"
                , Member.class
        ).getResultList();

        memberList.stream().forEach(System.out::println);
        System.out.println("memberList.get(0).equals(member) = " + memberList.get(0).equals(member));
    }

}
