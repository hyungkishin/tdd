package sample.cafekiosk.spring.domain.stock;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    public Stock(final String productNumber, final int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }


    public static Stock create(final String productNumber, final int quantity) {
        return Stock.builder()
                .productNumber(productNumber)
                .quantity(quantity)
                .build();
    }

    public boolean isQuantityLessThan(final int quantity) {
        return this.quantity < quantity;
    }

    public void deductQuantity(final int quantity) {
        if (isQuantityLessThan(quantity)) {
            throw new IllegalArgumentException("재고가 부족합니다.");

        }
        this.quantity -= quantity;
    }
}
