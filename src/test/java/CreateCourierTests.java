import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.scooter.constants.Response.*;

public class CreateCourierTests extends BaseTest {

    @Test
    public void testCreateCourier() {
        Response createCourier = createCourier(newCourier);
        createCourier
                .then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    public void testCourierIsCreated() {
        createCourier(courierNoName);
        Response loginCourier = loginCourier(newCourier);
        loginCourier
                .then()
                .statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    @Test
    public void testCantCreateCourierWithSameLogin() {
        createCourier(newCourier);
        Response createCourier = createCourier(newCourier);
        createCourier
                .then()
                .statusCode(409)
                .assertThat().body("message", equalTo(LOGIN_IS_ALREADY_USED))
                .assertThat().body("code", equalTo(409));
    }

    @Test
    public void testCreateCourierErrorIfNoLogin() {
        Response createCourier = createCourier(courierNoLogin);
        createCourier
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_ACCOUNT))
                .assertThat().body("code", equalTo(400));
    }

    @Test
    public void testCreateCourierErrorIfNoName() {
        Response createCourier = createCourier(courierNoName);
        createCourier
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_ACCOUNT))
                .assertThat().body("code", equalTo(400));
    }

    @Test
    public void testCreateCourierErrorIfNoPass() {
        Response createCourier = createCourier(courierNoPass);
        createCourier
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo(NOT_ENOUGH_DATA_TO_CREATE_ACCOUNT))
                .assertThat().body("code", equalTo(400));
    }

}