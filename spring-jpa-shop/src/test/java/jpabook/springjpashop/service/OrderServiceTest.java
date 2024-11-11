package jpabook.springjpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.springjpashop.domain.Address;
import jpabook.springjpashop.domain.Member;
import jpabook.springjpashop.domain.Order;
import jpabook.springjpashop.domain.OrderStatus;
import jpabook.springjpashop.domain.item.Book;
import jpabook.springjpashop.exception.NotEnoughStockException;
import jpabook.springjpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest // 좋은 테스트는 스프링X, DBX 순수하게 단위테스트가 좋음, 통합테스트시 사용
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        //given
        Member member = createMember();

        Book book = createBook( "시골 JPA", 10000,10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 주문취소() {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다.");
        assertEquals(10, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() {
        //given
        Member member = createMember();
        Book book = createBook( "시골 JPA", 10000,10);

        int orderCount = 11;

        //when & then
        assertThrowsExactly(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));

        //then
//        fail("재고 수량 부족 예외가 발생해야 한다.");
    }


    private Book createBook(String name, int orderPrice, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(orderPrice);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        entityManager.persist(member);
        return member;
    }
}