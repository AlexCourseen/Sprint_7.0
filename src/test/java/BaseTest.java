import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static ru.praktikum.scooter.constants.Config.*;

public class BaseTest {

    private Faker faker = new Faker(new Locale("ru"));
    private LocalDate date = LocalDate.now();

    private String firstName = faker.name().firstName();
    private String login = faker.pokemon().name();

    private String firstNameOrder = faker.name().firstName();
    private String lastNameOrder = faker.name().lastName();
    private String password = faker.idNumber().valid();
    private String address = faker.address().fullAddress();
    private String phone = faker.phoneNumber().cellPhone();
    private String delivDate = String.valueOf(date.plusDays((long)faker.number().randomDigit()));
    private int rent = faker.number().randomDigitNotZero();
    private String comment = faker.leagueOfLegends().summonerSpell();
    private int metro = faker.number().numberBetween(1, 237); // диапазон возможных number станций метро


    protected int idOrder;
    protected String newCourier = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\",\"firstName\":\"" + firstName + "\"}";
    protected String courierNoLogin = "{\"login\": \"\", \"password\": \"" + password + "\",\"firstName\":\"" + firstName + "\"}";
    protected String courierNoPass = "{\"login\": \"" + login + "\", \"password\": \"\",\"firstName\":\"" + firstName + "\"}";
    protected String courierNoName = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    protected String courierWrongLoginToAuth = "{\"login\": \"test" + login + "\", \"password\": \"" + password + "\"}";
    protected String courierWrongPassToAuth = "{\"login\": \"" + login + "\", \"password\": \"0000" + password + "\"}";
    protected String courierNoLoginToAuth = "{\"login\": \"\", \"password\": \"" + password + "\"}";
    protected String courierNoPassToAuth = "{\"login\": \"" + login + "\", \"password\": \"\"}";
    protected String orderBlackColor = "{" +
            "\"firstName\":\"" + firstNameOrder + "\"," +
            "\"lastName\": \"" + lastNameOrder + "\"," +
            "\"address\": \"" + address + "\"," +
            "\"metroStation\": " + metro + "," +
            "\"phone\": \"" + phone + "\"," +
            "\"rentTime\": " + rent + "," +
            "\"deliveryDate\": \"" + delivDate + "\"," +
            "\"comment\": \"" + comment + "\"," +
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
