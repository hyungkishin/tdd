package sample.cafekiosk.spring.domain.fixture;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public class ProductFixture {

    public static final String PRODUCT_NUMBER_1 = "001";

    public static final String PRODUCT_NUMBER_2 = "002";

    public static final String PRODUCT_NUMBER_3 = "003";

    public Product 아메리카노 = createProduct(PRODUCT_NUMBER_1, ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);

    public Product 카페라때 = createProduct(PRODUCT_NUMBER_2, ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라때", 4500);

    public Product 팥빙수 = createProduct(PRODUCT_NUMBER_3, ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);


    public Product createProduct(String productNumber,
                                 ProductType type,
                                 ProductSellingStatus status,
                                 String name,
                                 int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(status)
                .name(name)
                .price(price)
                .build();
    }
}
