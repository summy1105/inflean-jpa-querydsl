package jpabook.springjpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.springjpashop.domain.*;
import jpabook.springjpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * userA
 *  JPA1 BOOK
 *  JPA2 BOOK
 * userB
 *  SPRING1 BOOK
 *  SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "111-111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = getMemberAddress(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String userB, String 경기, String street, String zipcode) {
            Member member = new Member();
            member.setName(userB);
            member.setAddress(new Address(경기, street, zipcode));
            return member;
        }

        private Delivery getMemberAddress(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String SPRING2_BOOK, int price) {
            Book book1 = new Book();
            book1.setName(SPRING2_BOOK);
            book1.setPrice(price);
            book1.setStockQuantity(100);
            return book1;
        }

        public void dbInit2() {
            Member member = createMember("userB", "경기", "2", "222-111");
            em.persist(member);

            Book book1 = createBook("Spring1 Book", 10000);
            em.persist(book1);

            Book book2 = createBook("Spring2 Book", 20000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 4);

            Delivery delivery = getMemberAddress(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
    }
}
