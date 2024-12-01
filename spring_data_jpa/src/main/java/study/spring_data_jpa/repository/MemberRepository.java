package study.spring_data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.spring_data_jpa.dto.MemberDto;
import study.spring_data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") // 1. named query를 먼저 찾음 -> 2. 없으면 메소드이름으로 쿼리 생성
    List<Member> findByUsername(@Param("username")/*setParameter*/ String username);

    @Query("select m from Member m where m.username = :username and m.age = :age") // 앱 로딩시점에 쿼리 점검이 됨
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.spring_data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);// collection, content가 없어도 null이 아닌, empty list가 반환됨
    Member findMemberByUsername(String username); // 단건, 단 entity가 2개 이상 발생되면 error발생
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    @Query(value = "select m from Member m left join fetch m.team t", countQuery = "select count(m.id) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    // 벌크연산은 db에만 적용되고, persist context에는 업데이트 되지 않는다,-> clearAutomatically=true 로 설정해야함: entity.clear() 실행
    @Modifying(clearAutomatically = true) // jpa의 executeUpdate 메소드 역할
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @Query("select m from Member m")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(@Param("username") String username);

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true")) // 변경감지 turn off
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    List<UsernameOnlyDto> findProjectionsRecordByUsername(@Param("username") String username);

    <T> List<T> findProjectionsGenericByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
