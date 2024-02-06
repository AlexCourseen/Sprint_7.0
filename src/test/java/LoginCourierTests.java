import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.scooter.constants.Response.*;


public class LoginCourierTests extends BaseTest {

    @Test
    public void testLoginCourier() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(newCourier);
        loginCourier
                .then()
                .statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    @Test
    public void testLoginCourierWithWrongLogin() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(courierWrongLoginToAuth);
        loginCourier
                .then()
                .statusCode(404)
                .assertThat().body("code", equalTo(404))
                .assertThat().body("message", equalTo(ACCOUNT_NOT_FOUND));
    }

    @Test
    public void testLoginCourierWithWrongPass() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(courierWrongPassToAuth);
        loginCourier
                .then()
                .statusCode(404)
                .assertThat().body("code", equalTo(404))
                .assertThat().body("message", equalTo(ACCOUNT_NOT_FOUND));
    }

    @Test
    public void testLoginCourierNoPass() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(courierNoPassToAuth);
        loginCourier
                .then()
                .statusCode(400)
                .assertThat().body("code", equalTo(400))
                .assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_LOGIN));
    }

    @Test
    public void testLoginCourierNoLogin() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(courierNoLoginToAuth);
        loginCourier
                .then()
                .statusCode(400)
                .assertThat().body("code", equalTo(400))
                .assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_LOGIN));
    }
}
