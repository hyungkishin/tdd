package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DataJpaTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    void findAllByProductNumberIn() {
        final Stock stock1 = Stock.create("001", 1);
        final Stock stock2 = Stock.create("002", 2);
        final Stock stock3 = Stock.create("003", 3);


        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        final List<Stock> products = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 1),
                        tuple("002", 2)
                );
    }

    @Test
    @DisplayName("상품재고의 수량이 제공된 수량보다 작은지 확인한다.")
    void isQuantityLessThan() {
        final Stock stock1 = Stock.create("001", 1);
        int quantity = 2;

        boolean result = stock1.isQuantityLessThan(quantity);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("재고를 주어진 개수만큼 차감할 수 있다.")
    void isQuantityGreaterThan() {
        Stock stock = Stock.create("001", 1);
        int quantity = 1;

        stock.deductQuantity(quantity);

        assertThat(stock.getQuantity()).isZero();
    }

    @Test
    @DisplayName("재고보다 많은 수의 수량으로 차감 시도하는 경우 예외가 발생한다.")
    void deductQuantityOver() {
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}