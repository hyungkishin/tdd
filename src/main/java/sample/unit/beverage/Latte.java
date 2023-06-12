package sample.unit.beverage;

public class Latte implements Beverage {

    @Override
    public int getPrice() {
        return 4500;
    }

    @Override
    public String getName() {
        return "리떼";
    }

}
