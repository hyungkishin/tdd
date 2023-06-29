package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ProductTypeTest {

    @Test
    @DisplayName("제조음료 타입은 재고 차감이 불가하다.")
    void containsFailStockType() {
        // given
        final ProductType givenType = ProductType.HANDMADE;

        // when
        final boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("베이커리 타입은 재고 차감이 가능하다.")
    void containsSuccessStockType() {
        // given
        final ProductType givenType = ProductType.BAKERY;

        // when
        final boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("병음료 는 재고 차감이 가능하다.")
    void containsSuccessStockType2() {
        // given
        final ProductType givenType = ProductType.BOTTLE;

        // when
        final boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();
    }

}