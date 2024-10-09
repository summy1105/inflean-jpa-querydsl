package hello.jpa;

import hello.jpa.embeddable.Address;
import hello.jpa.embeddable.Period;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class ValueMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            embeddedTypeExample(entityManager);
//            embeddedTypeShareInstanceExample(entityManager);
            collectionExample(entityManager);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }

    private static void embeddedTypeExample(EntityManager entityManager) {
        Member member = new Member();
        member.setUsername("Hello");
        member.setHomeAddress(new Address("city", "street", "10000"));
        member.setWorkPeriod(new Period(LocalDateTime.now().withYear(2020), null));

        entityManager.persist(member);
    }

    private static void embeddedTypeShareInstanceExample(EntityManager entityManager) {
        Address shareRef = new Address("city", "street", "10000");
        Member member1 = new Member();
        member1.setUsername("Hello1");
        member1.setHomeAddress(shareRef);
        member1.setWorkPeriod(new Period(LocalDateTime.now().withYear(2020), null));
        entityManager.persist(member1);

        Member member2 = new Member();
        member2.setUsername("Hello2");
//        member2.setHomeAddress(shareRef); // 주의할 코드
        member2.setHomeAddress(shareRef.copy()); /// 값 복사해서 사용하기
        member2.setWorkPeriod(new Period(LocalDateTime.now().withYear(2020), null));
        member2.setHomeAddress(shareRef);


        entityManager.persist(member2);
//        member1.getHomeAddress().setCity("newCity"); // 이런 버그는 수면 위로 드러나는게 쉽지 않음. 엄청 주의해야함
    }

    private static void collectionExample(EntityManager entityManager) {
        Member member = new Member();
        member.setUsername("Hello");
        member.setHomeAddress(new Address("oldCity", "street", "10000"));
//        member.getAddressHistory().add(new Address("city1", "street", "10000"));
//        member.getAddressHistory().add(new Address("city2", "street", "10000"));
        member.getAddressHistory().add(new MemberAddressHistory("city1", "street", "10000"));
        member.getAddressHistory().add(new MemberAddressHistory("city2", "street", "10000"));
        member.getFavoriteFoods().add("치킨");
        member.getFavoriteFoods().add("족발");
        member.getFavoriteFoods().add("피자");

        entityManager.persist(member);

        entityManager.flush();
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, 1L);

        System.out.println("member.getUsername() = " + findMember.getUsername());
        findMember.getAddressHistory().stream().forEach(address -> System.out.println("address.getCity() = " + address.getCity()));
        findMember.getFavoriteFoods().stream().forEach(System.out::println);

        findMember.setHomeAddress(findMember.getHomeAddress().withCity("newCity"));
        // 피자 -> 삼겹살
        findMember.getFavoriteFoods().remove("피자"); // 피자는 delete됨
        findMember.getFavoriteFoods().add("삼겹살");

        // city1 -> oldCity : 기본에 모든 history를 삭제 -> 갯수별로 다시 insert
//        Address city1 = new Address("city1", "street", "10000");
//        findMember.getAddressHistory().remove(city1); // equals & hashcode를 제대로 구현안하면 지워지지 않음
//        findMember.getAddressHistory().add(city1.withCity("oldCity"));

        findMember.getAddressHistory().remove(0);
        findMember.getAddressHistory().add(new MemberAddressHistory("oldCity", "street", "1000"));
    }
}
