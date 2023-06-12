package sample.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    void getPrice() {
        Americano americano = new Americano();
        assertEquals(4000, americano.getPrice());
    }

    @Test
    void getName() {
        Americano americano = new Americano();

        assertThat("아메리카노")
                .isEqualTo(americano.getName());
    }

}