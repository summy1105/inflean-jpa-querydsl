package study.spring_data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.spring_data_jpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        Item item = new Item("a");
        itemRepository.save(item);//id가 null이 아닐 경우 merge를 사용 : id값의 엔티티가 있는지 확인하는 select 쿼리가 실행된다.
    }
}