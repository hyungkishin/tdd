package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private Long id;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    private List<ProductResponse> products;


    @Builder
    public OrderResponse(final Long id,
                         final int totalPrice,
                         final LocalDateTime registeredDateTime,
                         final List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(final Order order) {

        final List<ProductResponse> products = order.getOrderProducts().stream()
                .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(products)
                .build();
    }

}
