package sample.unit;

import lombok.Getter;
import sample.unit.beverage.Beverage;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    public static final String INVALIDATE_ORDER_MESSAGE = "주문 가능 시간이 아닙니다.";

    public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);

    public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(final Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(final Beverage beverage, int count) {
        if(count <= 0) {
            throw new IllegalArgumentException("음료 개수는 1개 이상이어야 합니다.");
        }

        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(final Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        return beverages.stream()
                .map(Beverage::getPrice)
                .reduce(0, Integer::sum);
    }

    public Order createOrder() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalStateException(INVALIDATE_ORDER_MESSAGE);
        }

        return new Order(LocalDateTime.now(), beverages);
    }

    public Order createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();

        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalStateException(INVALIDATE_ORDER_MESSAGE);
        }

        return new Order(LocalDateTime.now(), beverages);
    }

}
