#### gradle
- 프로젝트 root directory/gradlew는 실행파일이다!! (' D')
  - ./gradlew dependencies : 프로젝트 라이브러리 의존관계를 출력한다.

#### hikari
- db connection pool
- spring-boot-starter default

#### logback
- logging
- slf4j(인터페이스 모음)의 구현체중 하나, 다른 하나 중 유명한거 log4j
- spring-boot-starter default

#### spring-boot-starter-test
- junit, mockito(mock객체 만드는 라이브러리), assertj 

#### spring-boot-devtools
- 개발시 도움, thymeleaf

#### repository에서 save된 객체를 return하지 않는 이유
- command랑 query를 분리해라? : side effect를 방지
- [예제 method](../src/main/java/jpabook/springjpashop/repository/MemberRepository.java) Long save(Member member){}

#### 로그 라이브러리
- p6spy : 파라미터를 위치에 적용시켜서 출력, but 운영에서는 성능을 저하 시킬수 있기때문에, 적용에 대해 고려해야한다.

#### 외래 키가 있는 곳을 연관관계의 주인으로 정해라
- 연관관계의 주인은 단순히 외래 키를 누가 관리하냐의 문제이지 비즈니스상 우위에 있다고 주인으로 정하면 안된다.

#### 엔티티 클래스 개발 
- Getter, Setter
  - 이론적으로 Getter, Setter 모두 제공하지 않고, 꼭 필요한 별도의 메서드를 제공하는 것이 가장 이상적
  - 하지만, 실무에서 엔티티의 데이터는 조회할 일이 너무 많으므로, Getter의 경우 모두 열어두는 것이 편리하다. 
  - Setter을 열어두고, setter 호출하면, 데이터가 변하기 때문에, 엔티티의 변경 추적이 어려워진다.
  - Setter 대신에, 변경지점이 명확하도록 변경을 위한 비즈니스 메서드를 별도로 제공하는 것이 안전한다.
- @ManyToMany : 실무에서는 사용X

#### 엔티티 설계시 주의점
- 가급적 Setter를 사용하지 말자
- 모든 연관관계는 지연로딩으로 => 매우매우 중요
- 컬렉션은 필드에서 초기화 하자(null 문제에서 안전하다) : 하이버네이트에서 영속화 하면 엔티티가 wrapping됨, collection 필드를 잘못 생성하면, 하이버네이트의 내부 메커니즘에 문제가 발생할 수도 있음
- 테이블, 컬럼명 생성 전략 : 하이버네이트는 필드명 그대로 사용, 스프링부트 신규 설정(카멜 -> 스네이크, . -> _ , 대문자 -> 소문자 => SpringPhysicalNamingStrategy)
- 