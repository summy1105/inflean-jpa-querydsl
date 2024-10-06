### 데이터베이스 방언

[persistence.xml](../src/main/resources/META-INF/persistence.xml) 파일 아래 코드 설명
```xml
<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
```
 
```text
# jpa는 특정 데이터 베이스에 종속 X
# 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다름
    - 가변 문자 : MySQL은 VARCHAR, Oracle은 VARCHAR2
    - 문자열을 자르는 함수 : SQL 표준은 SUBSTRING(), Oracle은 SUBSTR()
    - 페이징 : MySQL은 LIMIT, Oracle은 ROWNUM
# 방언 : SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능
````

#
### 영속성 컨텍스트 (EntityManager)
Transaction 기준으로 entity data를 저장해 놓는 일시적인 캐쉬라고 생각하면 될것 같음.
write(insert, update)할 SQL을 저장
EntityManager.persist(entity) =>  영속성 컨텍스트에 저장   
EntityManager.detach(entity) => 영속성 컨텍스트에서 제거  
EntityManager.remove(entity) => 커밋할때 db에서 제거

- 영속 엔티티의 동일성 보장
  - 1차 캐시로 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리켕션 차원에서 제공 
- 쓰기 지연 SQL 저장소
- 1차 캐시
- 이점
  - 1차 캐시
  - entity 동일성 보장
  - 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
  - 변경 감지(dirty checking)
  - 지연 로딩(Lazy Loading)

#
### 플러시 (Flush)
영속성 컨텍스트의 변경내용을 데이터베이스에 반영   
: 영속성켄텍스트의 내용과 DB의 내용을 맞추는 작업이라고 보면 됨
- 플러시 작업 순서?
  - 변경감지 --> 수정된 엔티티 쓰기 지연 SQL 저장소에 등록 --> 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송
- 플러시 발생
  - EntityManager.flush() : 직접 호출
  - 트랜잭션 커밋 : 자동 호출
  - JPQL 쿼리 실행 : 자동 호출
- 플러시 모드 옵션
  - FlushModeType.AUTO : 커밋이나 쿼리를 실행할 때 플러시(기본값) -> 가급적 변경하지 말고 사용하자
  - FlushModeType.COMMIT : 커밋할 때 만 플러시
- 영속성 컨텍스트를 비우지는 않음
- 트랜잭션이라는 작업 단위가 중요 : 커밋 직전에 동기화 하면 됨

#
### 준영속 상태
영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)
영속성 컨텍스트가 제공하는 기능을 사용 못함
- 준영속 상태로 만들기
  - EntityManager.detach(entity) : 특정 엔티티만 준영속 상태로 전환
  - EntityManager.clear() : 영속성 컨텍스트 초기화 // 테스트시, 눈으로 쿼리를 확인하고 싶을 때 사용
  - EntityManager.close() : 영속성 컨텍스트를 종료

#
### @Entity
@Entity가 붙은 클래스는 JPA가 관리   
- 주의
  - 기본 생성자 필수(public or protected)
  - final 클래스, enum, interface, inner 클래스는 사용하면 안됨
  - 저장할 필드에 final 사용하면 안됨   

#
### hibernate.hbm2ddl.auto
  - create : 기존 테이블 삭제 후 다시 생성(drop+create)(사용X)
  - create-drop : create와 같으나 종료시점에 테이블 drop(사용X)
  - update : 변경분만 반영(운영X, test 서버에서만)
  - validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
  - none : 사용하지 않음

#
### DDL 생성기능
ex ) @Column(nullable = false, length = 10)   
DDL 생성기능은 DDL을 자동 생성할 때만 사용되고, JPA의 실행 로직에는 영향을 주지 않는다.

#
### 필드와 컬럼 매핑

1. @Column
   1. name, insertable(insert 시 데이터반영여부), updatable(update 시 데이터 반영여부)
   2. nullable = false : not null
   3. unique : 잘 쓰이지 않음 -> constraint 이름이 랜덤으로 생성되어서, 로그등 파악에 문제가 발생할 확률이 높음  => @Table annotation에서 uniqueConstraints를 사용
   4. length, columnDefinition(데이터 베이스 컬럼 정보를 직접 줄 수 있다.)
   5. precision(소수점을 포함한 전체 자릿수), scale(소수점 자리수) : BigInteger, BigDecimal type에서 사용
2. @Enumerated
   1. EnumType.ORDINAL : 쓰지 않는 것이 좋음. 숫자로 표현, enum 인스턴스의 순서에 따라, integer값이 결정 되기 때문에, 쓰는 것을 추천하지 않음
   2. EnumType.STRING : 이것을 권장함
3. @Temporal
   1. 과거 데이터 베이스를 사용할 경우 사용
   2. LocalDate, LocalDateTime을 사용할 때는 생략가능(최신 하이버네이트에서 지원)
4. @Lob
   1. 매핑하는 필드 타입이 문자염 CLOB(String, char[], java.sql.CLOB) 매핑, 나머지는 BLOB(byte[], java.sql.BLOB) 매핑

#
### 기본 키 매핑

1. @Id : primary key, @GeneratedValue를 추가 안하면 직접 할당. 
2. @GeneratedValue : 자동으로 키 생성
   1. GenerationType.IDENTITY : 기본 키 생성을 데이터베이스에 위침(MySql은 auto_increment)
      1. Persist Context는 pk값으로 entity를 구분 : insert케이스 경우, DB에서 키값을 받아와야 하기 때문에, EntityManager.persist(entity)시 바로 insert실행
   2. GenerationType.SEQUENCE : 오라클, PostgreSQL등 에서 사용
      1. (H2: select next value for MBR_SEQ 실행) 
      2. Long 데이터타입 이상으로.
      3. Entity객체에 @SequenceGenerator(name="MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ") 와 같이 사용
      4. allocationSize : 미리 sequence를 증가시켜서 서버 메모리에서 매번 query를 실행하지 않고, id를 사용
   3. GenerationType.TABLE
      1. 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시쿼스를 흉내내는 전략, 모든 DB에 적용가능하나, 성능상의 문제가 생길 수 있음(LOCK)

### 권장하는 식별자 전략
   1. 기본키 제약 조건 : null이 아님, 유일, 변하면 안된다.
   2. 미래까지 이 조건을 만족하는 자연키(ex.주민번호:성변환의 경우?, 주민번호 암호화 될 경우)는 찾기 어렵다. 대리키(대체키)를 사용하자.
   3. 권장 : Long형 + 대체키 + 키 생성전략 사용
   4. 비즈니스의 내용을 키로 가져오는 것을 권장하지 않음

#
### 연관 관계 매핑
   1. 단방향 연관관계 : @ManyToOne
   2. 양방향 연관관계 : @ManyToOne + @OneToMany(mappedBy = "team")  - Many=Member, One=Team
      1. mappedBy에는 Many가 되는 엔티티의 필드이름을 지정
      2. 객체에서는 단방향 관계 2개가 서로 엮여 있음
      3. 양방향 매핑시에 무한 루프를 조심하자 : ex. toString(), lombok, JSON 생성 라이브러리
         1. Spring Controller에서 entity를 반환하지 않게 한다 entity를 dto로 바꿔서 사용하자
      4. Many에 해당하는 entity의 fk 업데이트 문제가 있음
         1. Member의 Team이 변경 or Team의 member list에서 remove, add 할 때 해야하는지?
         2. 결론 : 둘 중 하나로 fk를 관리해야함 => 둘 중 하나를 연관관계의 주인으로 지정해야함
      5. (**_중요_**) 연관관계의 Owener
         1. 연관관계의 주임만이 외래 키를 관리(등록, 수정)
         2. 주인이 아닌쪽은 읽기만 가능 ( Team에서는 members를 읽기에만 사용한다. )
         3. 주인은 mappedBy 속성을 사용하지 않음
         4. 주인이 아니면 mappedBy 속성으로 주인 Entity의 필드 참조객체 지정
      6. **_가장 많이 하는 실수는 연관관계 주인에 값을 입력하지 않는 것_**
      7. 양쪽 entity에 참조값을 set해주는게 가장 베스트한 방법이다.
         1. 영속성 컨텍스트와 DB에 전부 값을 update를 해야함.
         2. 주인 entity의 field set method를 따로 편리하게 변경해 주는것이 좋다
         3. ``` java
             public void changeTeam(Team team) {
                if (this.team != null) {
                  this.team.getMembers().remove(this);
                }
                this.team = team;
                this.team.getMembers().add(this);
             }
            ```
   3. 정리
      1. 단방향 매핑만으로도 이미 연관관계 매핑은 완료 => 단방향 매핑으로 설계
      2. JPQL에서는 역방향으로 탐색할 일이 많음(entity로 데이터를 가져올때) : 양방향은 조회가 필요한 부분에서 사용
      3. 단방향 매핑을 잘하고 양방향은 필요할 때 추가하자

###
    1. 다대일 단방향 : @ManyToOne
    2. 다대일 양방향 : @ManyToOne + @OneToMany(mappedBy)
    3. 일대다 단방향 : @OneToMany => 추천하지 않음
        - One에 해당되는 Entity가 연관관계의 Owner
        - Many에 해당되는 Entity는 외래키를 관리하지 않지만, DB 테이블에서는 Many에 해당되는 entity의 테이블에서는 fk를 관리
        - One에 해당되는 Entity의 필드에 @JoinColumn 추가
        - @JoinColumn을 꼭 추가해야함 그렇지 않으면, 중간에 테이블이 하나 자동적으로 추가된다.
        - 단점 : 엔티티가 관리하는 외래 키가 다른 테이블에 있고, 연관관계 관리를 위해 추가로 update sql실행 
    4. 일대다 양방향 : 추천 X
        - Many에 해당되는 Entity에서 One의 참조는 읽기 전용으로 사용
    5. 일대일 : @OneToOne
        - 한 쪽 Entity에 @OneToOne 추가
        - 양방향으로 하면 다른 한쪽 Entity에서 @OneToOne 추가하고 mappedBy추가
        - 주 테이블에 외래 키 : 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인이 가능하지만, 값이 없으면 외래 키에 null이 허용되어야 함
        - 대상 테이블에 외래키 : 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지가 가능하지만, 프록시 기능의 한계로 지연로딩으로 설정해도 항상 즉시 로딩됨
    6. 다대다 : @ManyToMany
        - 실무에서 쓰지 않음
        - RDB는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없어서, 중간 연결 테이블이 생성됨
        - 연결 테이블을 추가해서 일대다, 다대일 관계를 풀어내야함
        - 객체는 다대다 관계가 가능하지만, 추천하지 않는다.
        - 여러 데이터가 들어 올 수 있으므로, 중간테이블이 결국 entity가 됨

#
### 상속관계 매핑
1. RDB에는 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
   1. @Inheritance(strategy)
   2. @DiscriminatorColumn : 부모 엔티티에 설정, 부모 엔티티 테이블에 type을 나타내는 컬럼이 생기고 entity명이 입력됨
   3. @DiscriminatorValue : 자식 엔티티에 설정, 부모 엔티티 테이블의 type컬럼에 엔티티 명대신, 저장될 값을 지정
2. 종류
   1. 단일 테이블 전략 : 한 테이블에 다 합쳐버림 => default 
      - @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
      - DTYPE 컬럼이 기본으로 생성됨(@DiscriminatorColumn이 기본)
      - 장점 : 조인 X, 조회 성능이 빠름
      - 단점 : 자식 엔티티에 해당하는 컬럼들은 모두 null허용을 해야함(무결성X), 테이블이 커질수 있고, 상황에 따라서, 조회성능이 오히려 느려질 수 있다.
      ![poster](./image/inheritance-rdb-single-table.png)
   2. 조인 전략 : 공통데이터(슈퍼타입) 테이블과 그 특별한 데이터(서브타입) 테이블을 분리해서 조인으로 데이터를 가져옴
      - @Inheritance(strategy = InheritanceType.JOINED)
      - 기본 정석 방식이라고 추천함
      - 장점 : 테이블 정규화, 외래 키 참조 무결성 제약조건 활용가능, 저장공간 효율화
      - 단점 : 조회시 조인을 많이 사용, 성능저하, 조회 쿼리가 복잡, 데이터 저장시 insert 2번 호출
      ![poster](./image/inheritance-rdb-join.png)
   3. 구현 클래스마다 테이블 매핑 전략
       - @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
       - 추천하지 않음 => 서로 묶이지 않음
       - 장점 : 서브 타입을 명확하게 구분해서 처리할 때 효과적, not null 제약조건 사용 가능
       - 단점 : 부모타입으로 조회할 경우, union으로 모든 테이블 데이터를 조회함 -> 느림
      ![poster](./image/inheritance-rdb-table-per-class.png)

### @MappedSuperclass
1. 공통 매핑 정보가 필요할 때 사용(id, name)
2. DB랑 관련없이 객체에서 공통적인 부분을 사용하고 싶을 때, 사용
   ![poster](./image/mapped-superclass.png)
3. 상속관계 매핑X
4. 엔티티X, 테이블과 매핑X
5. 부모클래스를 상속 받는 자식클래스에 매핑 정보만 제공
6. 조회 검색 불가(em.find(BaseEntity.class) 불가)
7. 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
8. 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 일
9. 주로 등록일, 수정일, 등록자, 수정가 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
10. 참고 : @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속가능 









