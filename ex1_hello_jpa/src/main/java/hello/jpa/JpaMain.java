package hello.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            insert(entityManager);
//            findNRemove(entityManager);
//            update(entityManager);
//            selectList(entityManager);
//            persistContext(entityManager);
//            insert2Rows(entityManager);
//            executeFlush(entityManager);
//            executeDetach(entityManager);
//            executeClear(entityManager);
//            sequenceKeyTest(entityManager);
//            teamNMember_ManyToOne(entityManager);
            teamNMember_OneToMany(entityManager);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }

    private static void insert(EntityManager entityManager) {
        Member member = new Member();
//      member.setId(4L);
        member.setUsername("HelloD");
        System.out.println("== before");
        entityManager.persist(member);
        System.out.println("member.id=" + member.getId());
        System.out.println("== after");
    }

    private static void findNRemove(EntityManager entityManager) {
            Member findMember = entityManager.find(Member.class, 4L);
            System.out.println("findMember = " + findMember);
            entityManager.remove(findMember);
    }

    private static void update(EntityManager entityManager) {
            Member findMember = entityManager.find(Member.class, 1L);
            findMember.setUsername("HelloJPA");
//            entityManager.persist(findMember); // 필요없음 : persist contenxt에 있는 member 인스턴스를 변경 : entity와 snapshot을 비교하여 변경이 있으면 commit시 디비에 반영
    }

    private static void selectList(EntityManager entityManager) {
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(1)
                    .getResultList();
            System.out.println(result);
    }

    private static void persistContext(EntityManager entityManager) {
        // 비영속
        Member member = new Member();
        member.setId(102L);
        member.setUsername("JPA persistContext2");

        // 영속
        System.out.println("== before");
        entityManager.persist(member);
        System.out.println("== after");

        Member findMember1 = entityManager.find(Member.class, 102L); // select query가 동작하지 않음 -> PersistContext에서 가져옴
        Member findMember2 = entityManager.find(Member.class, 102L); // select query가 동작하지 않음 -> PersistContext에서 가져옴
        System.out.println("findMember = "+findMember1);
        System.out.println("findMember1 ,2 equal = "+ (findMember2 == findMember2));

        // 다시 비영속
        entityManager.detach(member);
    }

    private static void insert2Rows(EntityManager entityManager) {

        // 영속
        Member member1 = new Member(150L, "A");
        Member member2 = new Member(151L, "B");

        System.out.println("before persist ===============");
        entityManager.persist(member1);
        entityManager.persist(member2);
        System.out.println("after persist ===============");
    }

    private static void executeFlush(EntityManager entityManager) {

        // 영속
        Member member = new Member(161L, "161A");

        entityManager.persist(member);
        entityManager.flush();
        System.out.println("after flush ===============");
    }

    private static void executeDetach(EntityManager entityManager) {

        // 영속
        Member findMember = entityManager.find(Member.class, 161L); // select query가 동작하지 않음 -> PersistContext에서 가져옴
        findMember.setUsername("161B");

        entityManager.detach(findMember);
        System.out.println("after detach ===============");
    }

    private static void executeClear(EntityManager entityManager) {

        // 영속
        Member findMember1 = entityManager.find(Member.class, 161L); // select query가 동작하지 않음 -> PersistContext에서 가져옴
        findMember1.setUsername("161B");

        entityManager.clear();
        System.out.println("after clear ===============");

        Member findMember2 = entityManager.find(Member.class, 161L); // select query가 동작하지 않음 -> PersistContext에서 가져옴
    }

    private static void sequenceKeyTest(EntityManager entityManager) {

        // 영속
        Member member1 = new Member();
        member1.setUsername("A");

        Member member2 = new Member();
        member2.setUsername("B");

        Member member3 = new Member();
        member3.setUsername("C");
        System.out.println("before persist ===============");
        entityManager.persist(member1); // id 1 -> DB는 51
        System.out.println("member1.id = " + member1.getId());

        entityManager.persist(member2); // id 2 -> DB는 101
        System.out.println("member2.id = " + member2.getId());

        entityManager.persist(member3); // id 3
        System.out.println("member3.id = " + member3.getId());
        System.out.println("after persist ===============");
    }

    private static void teamNMember_ManyToOne(EntityManager entityManager) {
        Team team = new Team();
        team.setName("TeamA");
        entityManager.persist(team);

        Member member = new Member();
        member.setUsername("member1");
//        member.setTeamId(team.getId());
        member.changeTeam(team);
        entityManager.persist(member);

        entityManager.flush();
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, member.getId());
//        Long teamId = findMember.getTeamId();
//        Team findTeam = entityManager.find(Team.class, teamId);

        Team findTeam = findMember.getTeam();
        System.out.println(findTeam);

        Team team2 = new Team();
        team2.setName("TeamB");
        findMember.changeTeam(team2);

        entityManager.persist(findTeam);
        entityManager.persist(team2);
    }

    private static void teamNMember_OneToMany(EntityManager entityManager) {
        Team team = new Team();
        team.setName("TeamA");
        entityManager.persist(team);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.changeTeam(team);
        team.getMembers().add(member1); // 양쪽에 참조 값을 추가하는게 제일 좋은 방법이다
        entityManager.persist(member1);

//
        Member member2 = new Member();
        member2.setUsername("member2");
//        member2.setTeam(team);
        team.getMembers().add(member2);// 디비에 매핑 안됨
        entityManager.persist(member2);


        Member member3 = new Member();
        member3.setUsername("member3");
        member3.changeTeam(team);
//        team.getMembers().add(member3);// team member에 추가를 하지 않으면 영속성 컨텍스트에 update가 안됨
        entityManager.persist(member3);


        team.getMembers().stream().forEach(m-> System.out.println("member name = "+ m.getUsername()));
        System.out.println();

        entityManager.flush();
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, member1.getId());
        List<Member> members = findMember.getTeam().getMembers();

        members.stream().forEach(m-> System.out.println("member name = "+ m.getUsername()));
    }
}
