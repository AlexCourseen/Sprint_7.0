import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;


public class GetOrdersTests extends BaseTest {
    @Test
    public void getOrdersTest() {
        createOrder(orderBlackColor);
        getOrders()
                .then()
                .statusCode(200)
                .assertThat().body("orders", notNullValue())
                .assertThat().body("orders", instanceOf(List.class));
    }
}
