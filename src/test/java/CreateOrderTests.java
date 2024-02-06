import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTests extends BaseTest{

        private final String order;
        private final int code;

        public CreateOrderTests(String order, int code) {
            this.order = order;
            this.code = code;
        }

        @Parameterized.Parameters
        public static Object[][] getOrderData() {
            String orderBlackColor = "{" +
                    "\"firstName\":\"Aleksandr\"," +
                    "\"lastName\": \"Kursin\"," +
                    "\"address\": \"Krasnodar, 142 apt.\"," +
                    "\"metroStation\": 4," +
                    "\"phone\": \"+7 800 355 35 35\"," +
                    "\"rentTime\": 5," +
                    "\"deliveryDate\": \"2020-06-06\"," +
                    "\"comment\": \"Thank you, Test\"," +
                    "\"color\": [" +
                    "\"BLACK\"" +
                    "]}";
            String orderGreyColor = "{" +
                    "\"firstName\":\"Aleksandr\"," +
                    "\"lastName\": \"Kursin\"," +
                    "\"address\": \"Krasnodar, 142 apt.\"," +
                    "\"metroStation\": 4," +
                    "\"phone\": \"+7 800 355 35 35\"," +
                    "\"rentTime\": 5," +
                    "\"deliveryDate\": \"2020-06-06\"," +
                    "\"comment\": \"Thank you, Test\"," +
                    "\"color\": [" +
                    "\"GREY\"" +
                    "]}";
            String orderTwoColors = "{" +
                    "\"firstName\":\"Aleksandr\"," +
                    "\"lastName\": \"Kursin\"," +
                    "\"address\": \"Krasnodar, 142 apt.\"," +
                    "\"metroStation\": 4," +
                    "\"phone\": \"+7 800 355 35 35\"," +
                    "\"rentTime\": 5," +
                    "\"deliveryDate\": \"2020-06-06\"," +
                    "\"comment\": \"Thank you, Test\"," +
                    "\"color\": [" +
                    "\"GREY\"," +
                    "\"BLACK\"" +
                    "]}";
            String orderNoColor = "{" +
                    "\"firstName\":\"Aleksandr\"," +
                    "\"lastName\": \"Kursin\"," +
                    "\"address\": \"Krasnodar, 142 apt.\"," +
                    "\"metroStation\": 4," +
                    "\"phone\": \"+7 800 355 35 35\"," +
                    "\"rentTime\": 5," +
                    "\"deliveryDate\": \"2020-06-06\"," +
                    "\"comment\": \"Thank you, Test\"," +
                    "\"color\": [" +
                    "]}";

            return new Object[][]{
                    {orderBlackColor, 201},
                    {orderGreyColor, 201},
                    {orderTwoColors, 201},
                    {orderNoColor, 201},
            };
        }

        @Test
        public void testCreateOrder() {
            Response createOrder = createOrder(order);
            createOrder
                    .then()
                    .statusCode(code)
                    .assertThat().body("track", notNullValue());
        }
}
