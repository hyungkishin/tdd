package sample.unit;

import lombok.Getter;
import sample.unit.beverage.Beverage;
import sample.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    private final List<Beverage> beverages = new ArrayList<Beverage>();

    public void add(final Beverage beverage) {
        System.out.println("beverage = " + beverage);
        beverages.add(beverage);
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

    public Order order() {
        return new Order(LocalDateTime.now(),  beverages);
    }

}
