package jpabook.springjpashop.service;

import jpabook.springjpashop.domain.Member;
import jpabook.springjpashop.domain.item.Book;
import jpabook.springjpashop.domain.item.Item;
import jpabook.springjpashop.repository.ItemRepository;
import jpabook.springjpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경감지 기능 사용
     */
    @Transactional
    public void updateItem(Long itemId, UpdateItemDto itemDto) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(itemDto.getName());
        findItem.setStockQuantity(itemDto.getStockQuantity());
        findItem.setPrice(itemDto.getPrice());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
