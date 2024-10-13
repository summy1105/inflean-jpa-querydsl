package hello.jpql;

import hello.jpql.dto.MemberDto;
import hello.jpql.entity.Member;
import hello.jpql.entity.MemberType;
import hello.jpql.entity.Team;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            typeQueryAndQuery(entityManager);
//            projectionExample(entityManager);
//            pagingExample(entityManager);
//            joinExample(entityManager);
//            typeExample(entityManager);
//            caseExample(entityManager);
//            functionExample(entityManager);
//            pathExpressionExample(entityManager);
//            fetchJoinExample(entityManager);
//            batchSizeExample(entityManager);
//            entityExample(entityManager);
//            namedQueryExample(entityManager);
            bulkExample(entityManager);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }


    private static void typeQueryAndQuery(EntityManager entityManager) {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);
        entityManager.persist(member);

        TypedQuery<Member> typedQuery1 = entityManager.createQuery("select m from Member m" +
                " where m.username=:username", Member.class);
        typedQuery1.setParameter("username", "kim");

        TypedQuery<String> typedQuery2 = entityManager.createQuery("select m.username from Member m where m.id = ?1", String.class);
        typedQuery2.setParameter(1, 1);

        Query query = entityManager.createQuery("select m.username, m.age from Member m");


        List<Member> resultMembers = typedQuery1.getResultList();
        for (Member resultMember : resultMembers) {
            System.out.println(resultMember.getUsername());
        }
        String singleResult = typedQuery2.getSingleResult();
        System.out.println("singleResult = " + singleResult);
    }


    private static void projectionExample(EntityManager entityManager) {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);

        Team team1 = new Team("team1");
        member.changeTeam(team1);

        entityManager.persist(member);
        entityManager.persist(team1);

        entityManager.flush();
        entityManager.clear();


        List<Team> resultList = entityManager.createQuery("select m.team from Member m", Team.class)
                .getResultList();

//        resultList = entityManager.createQuery("select t from Member m join m.team t", Team.class)
//                .getResultList();

        List<Object[]> resultList2 = entityManager.createQuery("select m.username, m.age from Member m")
                .getResultList();

        Object[] objects = (Object[]) resultList2.get(0);

        for (Object o : objects) {
            System.out.println("o = " + o);
        }

        List<MemberDto> dtoList = entityManager.createQuery("select new hello.jpql.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                .getResultList();
        for (MemberDto memberDto : dtoList) {
            System.out.println("memberDto = " + memberDto);
        }

        Team team = resultList.get(0);
        System.out.println(team.getName());
        team.setName("team2");
    }

    private static void initData(EntityManager entityManager) {

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Team team = new Team();
            team.setName("team"+ (char)('A'+i));
            teams.add(team);

            entityManager.persist(team);
        }

        Member memberOuter = new Member();
        memberOuter.setUsername("member");
        memberOuter.setAge(9);
        entityManager.persist(memberOuter);

        for (int i = 0; i < 20; i++) {
            Member member = new Member();
            if (i % 5 == 0) {
                member.setUsername("team" + (char) ('A' + i));
                member.setType(MemberType.ADMIN);
            } else {
                member.setUsername("member" + i);
                member.setType(MemberType.USER);
            }
            if (i % 7 == 0) {
                member.setUsername(null);
            }
            member.setAge(10+i);
            member.changeTeam(teams.get(i%5));
            entityManager.persist(member);
        }

        entityManager.flush();
        entityManager.clear();
    }

    private static void pagingExample(EntityManager entityManager) {
        initData(entityManager);

        String jpqlStr = "select m from Member m order by m.age desc";
        List<Member> resultList = entityManager.createQuery(jpqlStr, Member.class)
                .setFirstResult(5)
                .setMaxResults(10)
                .getResultList();
        for (Member member : resultList) {
            System.out.println("member = " + member);
        }
    }

    private static void joinExample(EntityManager entityManager) {
        initData(entityManager);

//        String query = "select m from Member m inner join m.team t where t.name = :teamName"; // 내부조인
//        String query = "select m from Member m left outer join m.team t where t is null"; // 와부조인
//        String query = "select m from Member m, Team t where m.team.id = t.id"; // 세타조인
//        String query = "select m from Member m left join m.team t on t.name='teamA'"; // 조인 대상 필터링
        String query = "select m from Member m join Team t on t.name= m.username "; // 세타조인 필터링

        List<Member> resultList = entityManager.createQuery(query, Member.class)
//                .setParameter("teamName", "teamC")
                .getResultList();
        for (Member member : resultList) {
            System.out.println("member = " + member);
//            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    private static void typeExample(EntityManager entityManager) {
        initData(entityManager);

        String query = "select m.username as mbmerUsername, 'HELLO', true, concat('team-',t.name), m.type from Member m join m.team t " +
                "   where m.type = :userType";

        List<Object[]> resultList = entityManager.createQuery(query)
                .setParameter("userType", MemberType.ADMIN)
                .getResultList();
        for (Object[] objects : resultList) {
            for (Object object : objects) {
                System.out.printf("o:"+object+", ");
            }
            System.out.println();
        }
    }

    private static void caseExample(EntityManager entityManager) {
        initData(entityManager);

//        String query =
//                "select " +
//                "   case when m.age <= 10 then '학생요금' " +
//                "        when m.age >= 60 then '경로요금' " +
//                "        else '일반요금' " +
//                "   end as fare " +
//                "from Member m";
//        String query = "select coalesce(m.username, '이름 없는 회원') from Member m ";
        String query = "select nullif(m.type, 'ADMIN') from Member m";

        List<MemberType> resultList = entityManager.createQuery(query, MemberType.class)
                .getResultList();
        for (MemberType result : resultList) {
            System.out.println("result= " + result);
        }
    }

    private static void functionExample(EntityManager entityManager) {
        initData(entityManager);

//        String query = "select 'a ' || m.username from Member m"; //concat
//        String query = "select substring(m.username, 2, 3) from Member m";
//        String query = "select locate('be', m.username) from Member m"; // return Integer
//        String query = "select size(t.members) from Team t";
        // group_concat은 h2가 기본적으로 가지고 있는 함수인데, 사용하려면 방언 등록을 해야함
//        String query = "select function('group_strSum', m.username) from Member m";
        String query = "select group_concat(m.username) from Member m";

        List resultList = entityManager.createQuery(query)
                .getResultList();
        for (Object result : resultList) {
            System.out.println("result= " + result);
        }
    }

    private static void pathExpressionExample(EntityManager entityManager) {
        initData(entityManager);

//        String query = "select m.team from Member m"; // 묵시적 내부 조인 발생
//        String query = "select m.team from Member m"; // 단일값 연관 경로 묵시적 내부 조인 발생
//        String query = "select t.members.size from Team t"; // 컬렉션값 연관 경로 묵시적 내부 조인 발생 -> 사용X
        String query = "select m.username from Team t join t.members m";

        List resultList = entityManager.createQuery(query)
                .getResultList();
        for (Object result : resultList) {
            System.out.println("result= " + result);
        }
    }

    private static void fetchJoinExample(EntityManager entityManager) {
        initData(entityManager);

        String query = "select m from Member m left join fetch m.team t";

        List<Member> resultList = entityManager.createQuery(query, Member.class)
                .setMaxResults(2)
                .getResultList();
        for (Member result : resultList) {
            // fetch join으로 회원과 팀을 함께 조회해서 지연 로딩 x
            System.out.println("result= " + result);
            System.out.println("result.getTeam() = " + result.getTeam());
        }

        query = "select t from Team t join fetch t.members";
//        query = "select t from Team t join fetch t.members as m where m.username = 'member'";// 사용하지 말아야함 // WARN: HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
        List<Team> teams = entityManager.createQuery(query, Team.class)
//                .setMaxResults(2) // 하이버네이트의 6버전에서는 members의 컬렉션이 완전하지 않아서 getMembers를 할 때, 다시 Member entity를 가져옴
                .getResultList();
        System.out.println("teams.size() = " + teams.size());
        for (Team team : teams) {
            System.out.println("team = " + team + ", members size= " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("team "+ team.getName()+" : member = " + member);
            }
        }
    }

    private static void batchSizeExample(EntityManager entityManager) {
        initData(entityManager);
        String query = "select t from Team t";

        List<Team> teams = entityManager.createQuery(query, Team.class)
                .getResultList();
        for (Team team : teams) {
            System.out.println("team = " + team + ", members size= " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("team "+ team.getName()+" : member = " + member);
            }
        }
    }

    private static void entityExample(EntityManager entityManager) {
        initData(entityManager);
        Member memberForFind = new Member();
        memberForFind.setId(1L);

        String query = "select m from Member m where m= :member";
        List<Member> members = entityManager.createQuery(query, Member.class)
                .setParameter("member", memberForFind)
                .getResultList();
        for (Member result : members) {
            // fetch join으로 회원과 팀을 함께 조회해서 지연 로딩 x
            System.out.println("result= " + result);
            System.out.println("result.getTeam() = " + result.getTeam());
        }

        Team team = new Team();
        team.setId(1L);

        query = "select m from Member m where m.team=:team";
        members = entityManager.createQuery(query, Member.class)
                .setParameter("team", team)
                .getResultList();
        for (Member result : members) {
            // fetch join으로 회원과 팀을 함께 조회해서 지연 로딩 x
            System.out.println("result= " + result);
            System.out.println("result.getTeam() = " + result.getTeam());
        }
    }

    private static void namedQueryExample(EntityManager entityManager) {
        initData(entityManager);

        List<Member> members = entityManager.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "member1")
                .getResultList();
        for (Member result : members) {
            // fetch join으로 회원과 팀을 함께 조회해서 지연 로딩 x
            System.out.println("result= " + result);
        }
    }

    private static void bulkExample(EntityManager entityManager) {
        initData(entityManager);
        Member member = entityManager.find(Member.class, 1);
        System.out.println(member);

        String query = "update Member m set m.age = m.age + 30 where m.age <= :age";
        int updateResult = entityManager.createQuery(query)
                .setParameter("age", 15)
                .executeUpdate();
        System.out.println("updateResult = " + updateResult);

        Member afterMember = entityManager.find(Member.class, 1);
        System.out.println("afterMember = " + afterMember);// flush&Clear를 안하면 변경된 값이 반영되어 있지 않다.

        entityManager.flush();
        entityManager.clear();

        Member afterFlushMember = entityManager.find(Member.class, 1);
        System.out.println("afterFlushMember = " + afterFlushMember);

//        List<Member> members = entityManager.createQuery("select m from Member m order by m.age", Member.class)
//                .getResultList();
//        for (Member result : members) {
//            System.out.println("result= " + result);
//        }
    }
}
