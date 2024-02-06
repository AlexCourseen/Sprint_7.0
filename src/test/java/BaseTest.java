import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static ru.praktikum.scooter.constants.Config.*;

public class BaseTest {

    protected int idOrder;
    protected String newCourier = "{\"login\": \"29testCourier\", \"password\": \"123456\",\"firstName\":\"kursin_29\"}";
    protected String courierNoLogin = "{\"login\": \"\", \"password\": \"123456\",\"firstName\":\"kursin_29\"}";
    protected String courierNoPass = "{\"login\": \"29testCourier\", \"password\": \"\",\"firstName\":\"kursin_29\"}";
    protected String courierNoName = "{\"login\": \"29testCourier\", \"password\": \"123456\"}";
    protected String courierWrongLoginToAuth = "{\"login\": \"testCourier\", \"password\": \"123456\"}";
    protected String courierWrongPassToAuth = "{\"login\": \"29testCourier\", \"password\": \"0000123456\"}";
    protected String courierNoLoginToAuth = "{\"login\": \"\", \"password\": \"123456\"}";
    protected String courierNoPassToAuth = "{\"login\": \"29testCourier\", \"password\": \"\"}";
    protected String orderBlackColor = "{" +
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

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Create courier")
    public Response createCourier(String string) {
        Response response = given().header("Content-type", "application/json").body(string).when().post(CREATE_COURIER);
        return response;
    }

    @Step("Login courier")
    public Response loginCourier(String string) {
        Response response = given().header("Content-type", "application/json").body(string).when().post(LOGIN_COURIER);
        return response;
    }

    @Step("Delete courier")
    public Response deleteCourier(int id) {
        Response response = given().delete(CREATE_COURIER + id);
        return response;
    }

    @Step("Create order")
    public Response createOrder(String string) {
        Response response = given().header("Content-type", "application/json").body(string).when().post(CREATE_ORDER);
        idOrder = response.jsonPath().getInt("track");
        return response;
    }

    @Step("Take order")
    public Response takeOrder(int idOrder, int idCourier) {
        Response response = given().header("Content-type", "application/json").when().put(TAKE_ORDER + idOrder
                + "?courierId=" + idCourier);
        return response;
    }

    @Step("Finish order")
    public Response finishOrder(int idOrder) {
        Response response = given().header("Content-type", "application/json").when().put(FINISH_ORDER + idOrder);
        return response;
    }

    @Step("Get orders")
    public Response getOrders() {
        Response response = given().get(GET_ORDERS);
        return response;
    }


    @After
    public void delTestData() {
        if (idOrder > 0) {
            createCourier(newCourier);
            int idCourier = loginCourier(courierNoName).jsonPath().getInt("id");
            takeOrder(idOrder, idCourier);
            finishOrder(idOrder);
            deleteCourier(idCourier);
            idOrder = 0;
        } else {
            Response loginCourier = loginCourier(courierNoName);
            if (loginCourier.getStatusCode() == 200) {
                int idCourier = loginCourier.jsonPath().getInt("id");
                deleteCourier(idCourier);
            }
        }
    }
}
