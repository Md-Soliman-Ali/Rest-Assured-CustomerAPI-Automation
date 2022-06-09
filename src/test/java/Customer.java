import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Customer {

    Properties prop = new Properties();
    FileInputStream file;

    {
        try {
            file = new FileInputStream("./src/test/resources/config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String token;

    public void callingLoginAPI() throws IOException, ConfigurationException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        String username = "salman";
        String password = "salman1234";

        Response res =
                given()
                        .contentType("application/json")
                        .body(
                                "{\"username\":\"" + username + "\",\n" +
                                        "    \"password\":\"" + password + "\"}"
                        ).
                        when()
                        .post("/customer/api/v1/login").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        token = jsonpath.get("token");
        Utils.setEnvVariable("token", token);
        System.out.println(res.asString());
    }

    public void customerList() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                (Response) given()
                        .contentType("application/json").header("Authorization", prop.getProperty("token")).
                        when()
                        .get("/customer/api/v1/list").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("Customers[0].id").toString(), "101");
        System.out.println(res.asString());
    }

    public void customerSearch() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                (Response) given()
                        .contentType("application/json").header("Authorization", prop.getProperty("token")).
                        when()
                        .get("/customer/api/v1/get/101").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonPath = res.jsonPath();
        Assert.assertEquals(jsonPath.get("name").toString(), "Mr. Kamal");
        System.out.println(res.asString());
    }

    public Integer ID;
    public String Name;
    public String email;
    public String emailGenerate;
    public String phoneNumber;

    public void generateCustomer() throws ConfigurationException, IOException {
        prop.load(file);
        RestAssured.baseURI = "https://api.namefake.com/english-united-states/male/";
        Response res =
                given()
                        .contentType("application/json")
                        .when()
                        .get()
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonPath = res.jsonPath();
        ID = (int) Math.floor(Math.random() * (999999 - 100000) + 1);
        Name = jsonPath.get("name");
        email = jsonPath.get("email_u");
        emailGenerate = email + "@test.com";
        phoneNumber = jsonPath.get("phone_w");

        Utils.setEnvVariable("id", ID.toString());
        Utils.setEnvVariable("name", Name);
        Utils.setEnvVariable("email", emailGenerate);
        Utils.setEnvVariable("phone_number", phoneNumber);
        System.out.println(res.asString());
    }

    public void createCustomer() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json").header("Authorization", prop.getProperty("token"))
                        .body("{\"id\":" + prop.getProperty("id") + ",\n" +
                                "    \"name\":\"" + prop.getProperty("name") + "\", \n" +
                                "    \"email\":\"" + prop.getProperty("email") + "\",\n" +
                                "    \"address\":\"Dhaka\",\n" +
                                "    \"phone_number\":\"" + prop.getProperty("phone_number") + "\"}")
                        .when()
                        .post("/customer/api/v1/create").
                        then()
                        .assertThat().statusCode(201).extract().response();

        System.out.println(res.asString());
    }
}
