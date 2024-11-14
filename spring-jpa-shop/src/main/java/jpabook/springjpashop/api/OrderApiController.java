package jpabook.springjpashop.api;

import jpabook.springjpashop.api.query.OrderQueryService;
import jpabook.springjpashop.domain.Address;
import jpabook.springjpashop.domain.Order;
import jpabook.springjpashop.domain.OrderItem;
import jpabook.springjpashop.domain.OrderStatus;
import jpabook.springjpashop.repository.OrderRepository;
import jpabook.springjpashop.repository.OrderSearch;
import jpabook.springjpashop.repository.order.query.OrderFlatDto;
import jpabook.springjpashop.repository.order.query.OrderItemQueryDto;
import jpabook.springjpashop.repository.order.query.OrderQueryDto;
import jpabook.springjpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static jpabook.springjpashop.api.query.OrderQueryService.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderQueryService.ordersV1();
        return all;
    }

    @GetMapping("/api/v2/orders")
    public ResultResponse ordersV2() {
        List<OrderDto> result = orderQueryService.ordersV2();
        return new ResultResponse(result.size(), result);
    }



    @GetMapping("/api/v3/orders")
    public ResultResponse ordersV3() {
        List<OrderDto> result = orderQueryService.ordersV3();
        return new ResultResponse(result.size(), result);
    }

    @GetMapping("/api/v3.1/orders")
    public ResultResponse ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return new ResultResponse(result.size(), result);
    }

    @GetMapping("/api/v4/orders")
    public ResultResponse ordersV4() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findOrderQueryDtos();

        return new ResultResponse(orderQueryDtos.size(), orderQueryDtos);
    }

    @GetMapping("/api/v5/orders")
    public ResultResponse ordersV5() {
        List<OrderQueryDto> orderQueryDtos = orderQueryRepository.findAllByDto_optimization();

        return new ResultResponse(orderQueryDtos.size(), orderQueryDtos);
    }

    @GetMapping("/api/v6/orders")
    public ResultResponse ordersV6() {
        List<OrderFlatDto> orderFlatDtos = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> orderQueryDtos = orderFlatDtos.stream()
                .collect(Collectors.groupingBy(
                                                f -> new OrderQueryDto(f.getOrderId(), f.getName(), f.getOrderDate(), f.getOrderStatus(), f.getAddress())
                                                , Collectors.mapping(f-> new OrderItemQueryDto(f.getOrderId(), f.getItemName(), f.getOrderPrice(), f.getCount())
                                                                        , Collectors.toList())
                ))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .sorted(Comparator.comparing(OrderQueryDto::getOrderId).reversed())
                .collect(Collectors.toList());

        return new ResultResponse(orderQueryDtos.size(), orderQueryDtos);
    }
}
