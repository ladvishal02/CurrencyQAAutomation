package com.currency;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import com.TestBase;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

public class CurrencyTest extends TestBase {


    //Data provider for currency tests for data-driven testing.
    @DataProvider(name = "currencyPriceRange")
    public static Object[][] currencyPriceRange() {
        return new Object[][]{
                {"AED", 3.6, 3.7},
                {"INR", 82.0, 85.0},
                {"XCD", 1.0, 4.0}
        };
    }

    @Test(description = "Verify API call is successful and match response schema.")
    public void matchJsonSchema(){
        given()
                .when()
                .get(BASE_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/schemas/currencyResponse.json")));

    }

    @Test(description = "Verify that 162 currency pairs are returned by the API.")
    public void verifyCountOfCurrencyPairs(){
        Response response = given()
                .when()
                .get(BASE_URL);
        JSONObject responseJson = new JSONObject(response.asString());
        Assert.assertEquals(responseJson.getJSONObject("rates").length(), 162);
    }

    @Test(description = "Fetch the USD price against a currency and verify the prices are within defined range.", dataProvider = "currencyPriceRange")
    public void verifyCurrencyPriceRange(String currency, double min, double max){
        Response response = given()
                .when()
                .get(BASE_URL);
        JSONObject responseJson = new JSONObject(response.asString());
        Assert.assertTrue(responseJson.getString("result").equalsIgnoreCase("success"), "result should be success.");
        JSONObject rates = responseJson.getJSONObject("rates");
        double currentPrice = rates.getDouble(currency);
        Assert.assertTrue((currentPrice >= min && currentPrice <= max), "Price for "+currency+" is not in the range "+min+", "+max);
    }


}
