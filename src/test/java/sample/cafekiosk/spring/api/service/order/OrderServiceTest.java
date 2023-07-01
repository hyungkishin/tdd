package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@DisplayName("주문 서비스 테스트")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockRepository stockRepository;

    private static final String PRODUCT_NUMBER_1 = "001";

    private static final String PRODUCT_NUMBER_2 = "002";

    private static final String PRODUCT_NUMBER_3 = "003";


//    @AfterEach
//    void tearDown() {
//        orderProductRepository.deleteAllInBatch();
//        orderRepository.deleteAllInBatch();
//        productRepository.deleteAllInBatch();
//        stockRepository.deleteAllInBatch();
//    }

    @Test
    @DisplayName("주문번호 리스트를 받아 가진 상품들을 조회한다.")
    void createOrder() {
        // given
        Product product1 = createProduct(HANDMADE, PRODUCT_NUMBER_1, 1000);
        Product product2 = createProduct(HANDMADE, PRODUCT_NUMBER_2, 3000);
        Product product3 = createProduct(HANDMADE, PRODUCT_NUMBER_3, 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        final OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // when
        final LocalDateTime registeredDateTime = LocalDateTime.now();

        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 4000);
        assertThat(response.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 가진 상품들을 조회한다.")
    void createOrderWithStock() {
        // given
        Product product1 = createProduct(BOTTLE, PRODUCT_NUMBER_1, 1000);
        Product product2 = createProduct(BAKERY, PRODUCT_NUMBER_2, 3000);
        Product product3 = createProduct(HANDMADE, PRODUCT_NUMBER_3, 5000);

        productRepository.saveAll(List.of(product1, product2, product3));


        final Stock stock1 = Stock.create("001", 2);
        final Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1, stock2));

        final OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        // when
        final LocalDateTime registeredDateTime = LocalDateTime.now();

        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response)
                        .extracting("registeredDateTime", "totalPrice")
                        .contains(registeredDateTime, 10_000),
                () -> assertThat(response.getProducts())
                        .hasSize(4)
                        .extracting("productNumber", "price")
                        .containsExactlyInAnyOrder(
                                tuple("001", 1000),
                                tuple("001", 1000),
                                tuple("002", 3000),
                                tuple("003", 5000)
                        )
        );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }


    @Test
    @DisplayName("재고가 없는 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    void createOrderWithNoStock() {
        // given
        Product product1 = createProduct(BOTTLE, PRODUCT_NUMBER_1, 1000);
        Product product2 = createProduct(BAKERY, PRODUCT_NUMBER_2, 3000);
        Product product3 = createProduct(HANDMADE, PRODUCT_NUMBER_3, 5000);

        productRepository.saveAll(List.of(product1, product2, product3));


        final Stock stock1 = Stock.create("001", 2);
        final Stock stock2 = Stock.create("002", 2);

        stock1.deductQuantity(1); // TODO

        stockRepository.saveAll(List.of(stock1, stock2));

        final OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        // when
        final LocalDateTime registeredDateTime = LocalDateTime.now();


        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    void createOrderDuplicateProductNumbers() {
        // given
        Product product1 = createProduct(HANDMADE, PRODUCT_NUMBER_1, 1000);
        Product product2 = createProduct(HANDMADE, PRODUCT_NUMBER_2, 3000);
        Product product3 = createProduct(HANDMADE, PRODUCT_NUMBER_3, 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        final OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when
        final LocalDateTime registeredDateTime = LocalDateTime.now();

        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 2000);
        assertThat(response.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}