package jpabook.springjpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager entityManager;


    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> resultList = findOrders();

        resultList.forEach(o->{
            o.setOrderItems(findOrderItemQueryDtos(o.getOrderId()));
        });
        return resultList;
    }

    private List<OrderQueryDto> findOrders() {
        String query = "select new jpabook.springjpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d";
        List<OrderQueryDto> resultList = entityManager.createQuery(query, OrderQueryDto.class)
                .getResultList();
        return resultList;
    }

    private List<OrderItemQueryDto> findOrderItemQueryDtos(Long orderId) {
        String query = "select new jpabook.springjpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId";
        List<OrderItemQueryDto> resultList = entityManager.createQuery(query, OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();

        return resultList;
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orders = findOrders();

        List<Long> orderIds = orders.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = findOrderItems(orderIds);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        orders.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(List<Long> orderIds) {
        String query = "select new jpabook.springjpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :orderIds";
        List<OrderItemQueryDto> orderItems = entityManager.createQuery(query, OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        String query = "select new jpabook.springjpashop.repository.order.query.OrderFlatDto(" +
                " o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i";
        List<OrderFlatDto> resultList = entityManager.createQuery(query, OrderFlatDto.class)
                .getResultList();
        return resultList;
    }
}
