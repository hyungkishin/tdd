package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

class OrderTest {

    @DisplayName("주문 생성시 주문 상태는 주문생성 상태 이다.")
    @Test
    void createOrderTest() {
        // given
        final List<Product> products = List.of(createProduct("001", 1000));

        // when
        final Order order = Order.create(products, LocalDateTime.now());

        // then
        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderStatus())
                        .isEqualByComparingTo(OrderStatus.INIT) // isEqualByComparingTo : enum 비교
        );
    }

    @DisplayName("주문 생성시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        final List<Product> products = List.of(createProduct("001", 1000));

        final LocalDateTime registeredDateTime = LocalDateTime.now();
        // when
        final Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    @DisplayName("주문 생성시, 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        final List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );
        // when
        final Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}