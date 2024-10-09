package hello.jpa;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

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
//            teamNMember_OneToMany(entityManager);
//            printMemberNTeam(entityManager);
//            entityGetReference(entityManager);
//            useProxyAfterDetach(entityManager);
//            proxyCheck(entityManager, entityManagerFactory);
//            lazyFetchProxy(entityManager);
//            eagerFetchProblem(entityManager);
//            cascadeExample(entityManager);
//            orphanRemovalExample(entityManager);
            cascadeRemoveExample(entityManager);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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
//        member.changeTeam(team);
        entityManager.persist(member);

        entityManager.flush();
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, member.getId());
//        Long teamId = findMember.getTeamId();
//        Team findTeam = entityManager.find(Team.class, teamId);

//        Team findTeam = findMember.getTeam();
//        System.out.println(findTeam);

        Team team2 = new Team();
        team2.setName("TeamB");
//        findMember.changeTeam(team2);

//        entityManager.persist(findTeam);
        entityManager.persist(team2);
    }

    private static void teamNMember_OneToMany(EntityManager entityManager) {
        Team team = new Team();
        team.setName("TeamA");
        entityManager.persist(team);

        Member member1 = new Member();
        member1.setUsername("member1");
//        member1.changeTeam(team);
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
//        member3.changeTeam(team);
//        team.getMembers().add(member3);// team member에 추가를 하지 않으면 영속성 컨텍스트에 update가 안됨
        entityManager.persist(member3);


        team.getMembers().stream().forEach(m-> System.out.println("member name = "+ m.getUsername()));
        System.out.println();

        entityManager.flush();
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, member1.getId());
//        List<Member> members = findMember.getTeam().getMembers();

//        members.stream().forEach(m-> System.out.println("member name = "+ m.getUsername()));
    }

    private static Long initMemberNTeamDataSet(EntityManager entityManager) {
        Team team = new Team();
        team.setName("TeamA");
        entityManager.persist(team);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.changeTeam(team);
        team.getMembers().add(member1);
        entityManager.persist(member1);

        entityManager.flush();
        entityManager.clear();

        return member1.getId();
    }

    private static void printMemberNTeam(EntityManager entityManager) {
        Long memberId = initMemberNTeamDataSet(entityManager);

        Member member = entityManager.find(Member.class, memberId);
        System.out.println("findMember class= "+ member.getClass()); // findMember class= class hello.jpa.Member
        System.out.println(" before print===============");
        member.printMemberAndTeam();
    }

    private static void entityGetReference(EntityManager entityManager) {
        Long memberId = initMemberNTeamDataSet(entityManager);

        Member memberRef = entityManager.getReference(Member.class, memberId); // proxy 객체
        System.out.println("memberRef id = " + memberRef.getId());
        System.out.println("findMember class= "+ memberRef.getClass()); // findMember class= class hello.jpa.Member$HibernateProxy$8DIt0uSV
        System.out.println(" before print===============");
        memberRef.printMemberAndTeam(); // 실제 entity가 사용되는 시점에서 초기화 요청 -> 조회

        Member member1 = entityManager.find(Member.class, memberId); // 영속성 컨텍스트에 proxy객체로 초기화 되면 그 객체가 넘어옴
        System.out.println("member1 class= "+ member1.getClass()); // findMember class= class hello.jpa.Member$HibernateProxy$8DIt0uSV

        entityManager.clear();

        member1 = entityManager.find(Member.class, memberId);
        Member member2 = entityManager.find(Member.class, memberId); // class hello.jpa.Member
        System.out.println("member1 class= "+ member1.getClass()); //
        System.out.println(" member1 class == member2 class : " + (member1 == member2 ));

        System.out.println(" memberRef class == member2 class : " + (memberRef == member2 ));
    }

    private static void useProxyAfterDetach(EntityManager entityManager) {
        Long memberId = initMemberNTeamDataSet(entityManager);

        System.out.println("========");
        Member memberRef = entityManager.getReference(Member.class, memberId);
        System.out.println("memberRef class = "+ memberRef.getClass());

//        entityManager.clear();
        entityManager.detach(memberRef);
//        entityManager.close(); // 5.4.0.Final 버전까지는 예외가 발생하는데, 5.4.1.Final 버전부터 예외가 발생하지 않네요 url : https://www.inflearn.com/questions/53733

        System.out.println("username = " + memberRef.getUsername()); // 초기화(조회)하지 않고 detach하면 org.hibernate.LazyInitializationException 발생
    }

    private static void proxyCheck(EntityManager entityManager, EntityManagerFactory entityManagerFactory) {
        Long memberId = initMemberNTeamDataSet(entityManager);

        Member memberRef = entityManager.getReference(Member.class, memberId);
        System.out.println("memberRef class = "+ memberRef.getClass());

//        PersistenceUnitUtil persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        System.out.println("isLoaded = " + persistenceUtil.isLoaded(memberRef));

        Hibernate.initialize(memberRef); // 강제 초기화
    }

    private static void lazyFetchProxy(EntityManager entityManager) {
        Long memberId = initMemberNTeamDataSet(entityManager);

        Member findMember = entityManager.find(Member.class, memberId);
        System.out.println("findMember.getUsername() = " + findMember.getUsername());

        System.out.println("findMember.getTeam().getClass() = " + findMember.getTeam().getClass());
        System.out.println("findMember.getTeam().getName() = " + findMember.getTeam().getName());
    }

    private static void eagerFetchProblem(EntityManager entityManager) {
        Long memberId = initMemberNTeamDataSet(entityManager);
        Long memberId2 = initMemberNTeamDataSet(entityManager);

//        List<Member> members  = entityManager.createQuery("select m from Member m", Member.class)
//                .getResultList();
        // FetchType.EAGER로 실행할 경우, 위의 실행은 member 조회 query외에, member별로 team find조회 query도 실행된다
        // 예를 들어 멤버가 100명 조회가 되면, 팀을 id별로 조회 되기 때문에, 쿼리 100번이 실행되고, 총 101쿼리가 실행된다.

        // JPQL 해결방법 : fetch 조인
        List<Member> resultList1 = entityManager.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();
    }

    private static void cascadeExample(EntityManager entityManager) {

        Parent parent = new Parent();
        parent.setName("parent");

        Child child1 = new Child();
        child1.setName("child1");

        Child child2 = new Child();
        child2.setName("child2");

        parent.addChild(child1);
        parent.addChild(child2);

        entityManager.persist(parent);
//        entityManager.persist(child1);
//        entityManager.persist(child2);

        entityManager.flush();
        entityManager.clear();

        Child findChild = entityManager.find(Child.class, child1.getId());
        System.out.println("findChild.getName() = " + findChild.getName());
    }

    private static void orphanRemovalExample(EntityManager entityManager) {

        Parent parent = new Parent();
        parent.setName("parent");

        Child child1 = new Child();
        child1.setName("child1");

        Child child2 = new Child();
        child2.setName("child2");

        parent.addChild(child1);
        parent.addChild(child2);

        entityManager.persist(parent);

        entityManager.flush();
        entityManager.clear();

        Parent findParent = entityManager.find(Parent.class, parent.getId());
        System.out.println("=======");
        Child child = findParent.getChildList().get(0);
        System.out.println("child.getId() = " + child.getId());
        findParent.removeChild(child);
    }

    private static void cascadeRemoveExample(EntityManager entityManager) {

        Parent parent = new Parent();
        parent.setName("parent");

        Child child1 = new Child();
        child1.setName("child1");

        Child child2 = new Child();
        child2.setName("child2");

        parent.addChild(child1);
        parent.addChild(child2);

        entityManager.persist(parent); // 부모만 영속

        entityManager.flush();
        entityManager.clear();

        Parent findParent = entityManager.find(Parent.class, parent.getId());
        entityManager.remove(findParent); // 부모만 remove
    }
}
