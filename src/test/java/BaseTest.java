import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static ru.praktikum.scooter.constants.Config.*;

public class BaseTestCourier {

    protected String courier = "{\"login\": \"29testCourier\", \"password\": \"123456\",\"firstName\":\"kursin_29\"}";
    protected String courierNoLogin = "{\"login\": \"\", \"password\": \"123456\",\"firstName\":\"kursin_29\"}";
    protected String courierNoPass = "{\"login\": \"29testCourier\", \"password\": \"\",\"firstName\":\"kursin_29\"}";
    protected String courierNoName = "{\"login\": \"29testCourier\", \"password\": \"123456\"}";
    protected String courierWrongLoginToAuth = "{\"login\": \"testCourier\", \"password\": \"123456\"}";
    protected String courierWrongPassToAuth = "{\"login\": \"29testCourier\", \"password\": \"0000123456\"}";
    protected String courierNoLoginToAuth = "{\"login\": \"\", \"password\": \"123456\"}";
    protected String courierNoPassToAuth = "{\"login\": \"29testCourier\", \"password\": \"\"}";

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

    @After
    public void delCourier() {
        Response loginCourier = loginCourier(courier);
        if (loginCourier.getStatusCode() == 200) {
            int idCourier = loginCourier.jsonPath().getInt("id");
            deleteCourier(idCourier);
        }
    }

}
