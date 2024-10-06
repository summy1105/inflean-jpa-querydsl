package jpabook.jpashop;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            makeNewOrder(entityManager);
            makeNewBook(entityManager);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }

    static private void makeNewOrder(EntityManager entityManager) {
        Order order = new Order();

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(order);

//        order.addOrderItem(orderItem1);
    }

    static private void makeNewBook(EntityManager entityManager) {
        Book book = new Book();
        book.setName("JPA");
        book.setAuthor("김영한");

        entityManager.persist(book);
    }
}
