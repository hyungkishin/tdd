package sample.unit.order;

import lombok.Getter;
import sample.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Order {

    private final LocalDateTime orderDateTime;

    private final List<Beverage> beverages;

    public Order(final LocalDateTime orderDateTime, final List<Beverage> beverages) {
        this.orderDateTime = orderDateTime;
        this.beverages = beverages;
    }

}
