package com.insider.petstore.api.tests;

import com.insider.petstore.api.client.PetApiClient;
import com.insider.petstore.api.model.Pet;
import com.insider.petstore.api.enums.PetStatus;
import com.insider.petstore.api.utils.JsonUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Epic("Petstore API")
@Feature("Pet API - Smoke")
public class PetSmokeTest extends BaseTest {
    @DataProvider(name = "petStatuses")
    public Object[][] petStatusProvider() {
        return new Object[][]{
                {PetStatus.available},
                {PetStatus.pending},
                {PetStatus.sold}
        };
    }

    @Test(description = "Find pets by status - smoke check")
    public void findPetsByStatus_shouldReturn200AndList() {
        Response response = PetApiClient.sendGetRequest(PetApiClient.PET_FIND_BY_STATUS_PATH, Map.of("status", "available"), null);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
        assertThat(response.jsonPath().getList("$")).isNotNull();
    }

    @Test(description = "GET /pet/findByStatus - Should return pets by status", dataProvider = "petStatuses")
    public void findPetsByStatus_withValidStatus_shouldReturnPetsWithMatchingStatus(PetStatus status) {
        Response response = PetApiClient.sendGetRequest(PetApiClient.PET_FIND_BY_STATUS_PATH, Map.of("status", status.name()), null);
        List<Pet> pets = JsonUtils.fromResponseList(response, Pet.class);

        assertThat(response.statusCode()).isEqualTo(200);
        for (Pet pet : pets) {
            assertThat(pet.getStatus()).isEqualTo(status);
        }
    }
}
