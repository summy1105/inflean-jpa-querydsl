#### 세타조인
- from절에서 여러 엔티티를 선택해서 조인
- 단점 : 외부조인(left, right outer) 불가능 => join on을 사용하면 외부 조인 가

#### from절의 서브쿼리 한계
- JPA JPQL의 서브쿼리의 한계점으로 from 절의 서브쿼리(인라인 뷰)는 지원하지 않는다. -> 당연히 querydsl도 지원x
- 해결방안
  1. 서브쿼리를 join으로 변경
  2. 애플리케이션에서 퀴리를 2번 분리
  3. native sql사용
  4. ** 무조건 쿼리1개로 실행하는게 좋은 것은 아니다, 책 추천 : SQL Anti-patterns

#### case 문
- 강사 개인적으로는 query로 case문을 쓰지 않고 서버(앱)에서 처리하는 것을 추천한다.(어쩔수 없을때 씀) 

#### bulk 연산
- 단점 : 변경되는 데이터가 영속성 컨텍스트에 반영이 안됨
  - bulk연산후에는 꼭 entity.flush(), entity.clear()를 하자
