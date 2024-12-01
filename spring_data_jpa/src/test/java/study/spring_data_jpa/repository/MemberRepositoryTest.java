package study.spring_data_jpa.repository;

import com.sun.source.tree.MemberSelectTree;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa.dto.MemberDto;
import study.spring_data_jpa.entity.Member;
import study.spring_data_jpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        findMember1.changeUsername("new member!");

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long countAfterDelete = memberRepository.count();
        assertThat(countAfterDelete).isEqualTo(0);
    }


    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findTest() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> top3HelloBy = memberRepository.findTop3HelloBy();
        for (Member member : top3HelloBy) {
            System.out.println("member = " + member);
        }
    }


    @Test
    public void namedQueryTest() {
        Member m1 = new Member("aaa", 10);
        Member m2 = new Member("aaa", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("aaa");

        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(10);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testQueryMethod() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void testFindUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList).containsExactly("AAA", "BBB");
    }

    @Test
    public void testFindMemberDto() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(List.of("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        for (Member member : aaa) {
            System.out.println("member = " + member);
        }

        Member aaa1 = memberRepository.findMemberByUsername("AAA");
        System.out.println("aaa1 = " + aaa1);

        Optional<Member> aaa2 = memberRepository.findOptionalByUsername("AAA");
        System.out.println("aaa2.get() = " + aaa2.get());

        List<Member> asgds = memberRepository.findListByUsername("ASGDS");
        assertThat(asgds.size()).isEqualTo(0);

        Member asgds1 = memberRepository.findMemberByUsername("ASGDS");
        assertThat(asgds1).isNull();
    }

    @Test
    public void paging() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        // given
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamA));
        memberRepository.save(new Member("member3", 10, teamA));
        memberRepository.save(new Member("member4", 10, teamB));
        memberRepository.save(new Member("member5", 10, teamB));

        int age = 10;
        int offset = 0;
        int limit = 3;
        // when
        PageRequest pageRequest = PageRequest.of(0, 3 , Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> memberDtoPage = page.map(mb -> new MemberDto(mb.getId(), mb.getUsername(), mb.getTeam().getName()));

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
        for (MemberDto memberDto : memberDtoPage) {
            System.out.println("memberDto = " + memberDto);
        }

        // then
        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void slice() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;
        // when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

        List<Member> content = slice.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        // then
        assertThat(content.size()).isEqualTo(3);
        assertThat(slice.getNumber()).isEqualTo(0);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);
//        entityManager.flush();
//        entityManager.clear();

        // 쿼리 업데이트시 영속성 컨텍스트에 member5의 엔티티가 업데이트 되지 않음
        Member member5 = memberRepository.findMemberByUsername("member5");
//        assertThat(member5.getAge()).isEqualTo(40);
        assertThat(member5.getAge()).isEqualTo(41);
    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamB));
        entityManager.flush();
        entityManager.clear();

//        List<Member> members = memberRepository.findMemberFetchJoin();
//        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberEntityGraph();
//        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        List<Member> members = memberRepository.findNamedEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam() = " + member.getTeam());
        }

    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        memberRepository.save(new Member("member2", 20));
        entityManager.flush();
        entityManager.clear();

        //when
//        Member findMember = memberRepository.findById(member1.getId()).get();
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.changeUsername("member3");
        entityManager.flush();
    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        memberRepository.save(new Member("member2", 20));
        entityManager.flush();
        entityManager.clear();

        //when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
        findMember.get(0).changeUsername("member3");
        entityManager.flush();
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() {
        // given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        entityManager.persist(member1);
        entityManager.persist(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        Specification<Member> spec = MemberSpec.username("member1").and(MemberSpec.teamName("teamA"));
        List<Member> members = memberRepository.findAll(spec);

        //then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        entityManager.persist(member1);
        entityManager.persist(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        //Probe
        Member conditionEntity = new Member("member1");
        conditionEntity.changeTeam(new Team("teamA"));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

//        Example<Member> conditionExample = Example.of(conditionEntity); // age 기본형 : 0값이 조건으로 들어감
        Example<Member> conditionExample = Example.of(conditionEntity, matcher);

        // 단점 : inner join만 가능
        List<Member> result = memberRepository.findAll(conditionExample);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void projections() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        entityManager.persist(member1);
        entityManager.persist(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("member1");
        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
            System.out.println("usernameOnly.getUsername() = " + usernameOnly.getUsername());
        }

        List<UsernameOnlyDto> result2 = memberRepository.findProjectionsRecordByUsername("member2");
        for (UsernameOnlyDto usernameOnlyDto : result2) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto.username());
        }

        List<NestedClosedProjections> result3 = memberRepository.findProjectionsGenericByUsername("member2"
                , NestedClosedProjections.class);
        for (NestedClosedProjections nestedClosedProjections : result3) {
            System.out.println("nestedClosedProjections = " + nestedClosedProjections);
            System.out.println("nestedClosedProjections.getUsername() = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections.getTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }
        //then
    }
    
    @Test
    public void nativeQuery() {
        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        entityManager.persist(member1);
        entityManager.persist(member2);

        entityManager.flush();
        entityManager.clear();
        
        //when
        Member result = memberRepository.findByNativeQuery("member1");
        System.out.println("result = " + result);

        //then
    }

    @Test
    public void findByNativeQuery() {

        //given
        Team teamA = new Team("teamA");
        entityManager.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        entityManager.persist(member1);
        entityManager.persist(member2);

        entityManager.flush();
        entityManager.clear();

        //when
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        System.out.println("result = " + result);
        for (MemberProjection memberProjection : result) {
            System.out.println("memberProjection.getId() = " + memberProjection.getId());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }
}