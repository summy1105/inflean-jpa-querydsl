package jpabook.springjpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.springjpashop.domain.Member;
import jpabook.springjpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager entityManager;

    public void save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            entityManager.merge(item);
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
