package jpabook.springjpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository는 순수 엔티티만을 사용하게 두고, Query Repository는 별도로 빼줌
 */
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager entityManager;

    // 단: 재사용성 없음
    // 장: 필요한 컬럼의 데이터만 가져옴(메모리 네트워크 비용이 상대적으로 낮음)
    // repository가 view에 의존적임. 성능에 큰 차이가 있지 않다면 비추(대부분 성능에 큰 차이가 없음)
    public List<OrderSimpleQueryDto> findOrderDtos() {
        String qlString =
                "select new jpabook.springjpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d";
        List<OrderSimpleQueryDto> resultList = entityManager.createQuery(qlString, OrderSimpleQueryDto.class)
                .getResultList();
        return resultList;
    }
}
