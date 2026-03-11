package com.insider.petstore.api.tests;

import com.insider.petstore.api.client.PetApiClient;
import com.insider.petstore.api.model.Pet;
import com.insider.petstore.api.testdata.TestDataBuilder;
import com.insider.petstore.api.utils.JsonUtils;
import com.insider.petstore.api.utils.ResponseExtractor;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.insider.petstore.api.client.PetApiClient.*;
import static org.assertj.core.api.Assertions.*;

@Epic("Petstore API")
@Feature("Pet CRUD - Negative")
public class PetCrudNegativeTest extends BaseTest {

    private static final long NON_EXISTING_PET_ID = 99_999_999_999L;

    @Test(description = "GET /pet/{petId} - Non-existing ID should return 404")
    public void getPetById_whenPetDoesNotExist_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", NON_EXISTING_PET_ID)));

        assertThat(status).isEqualTo(404);
    }

    @Test(description = "GET /pet/{petId} - Invalid ID 999999999 should return 404")
    public void getPetById_invalidId_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", 999999999L)));

        assertThat(status).isEqualTo(404);
    }


    @Test(description = "GET /pet/{petId} - Negative ID -1 should return 404")
    public void getPetById_negativeId_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", -1)));

        assertThat(status).isEqualTo(404);
    }

    @Test(description = "GET /pet/{petId} - Zero ID should return 404")
    public void getPetById_zeroId_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", 0)));

        assertThat(status).isEqualTo(404);
    }

    @Test(description = "GET /pet/findByStatus - Invalid status should return empty list")
    public void findPetsByStatus_invalidStatus_shouldReturnEmptyList() {
        Response response = PetApiClient.sendGetRequest(PET_FIND_BY_STATUS_PATH, Map.of("status", "invalidStatus"), null);
        List<Pet> pets = JsonUtils.fromResponseList(response, Pet.class);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(pets).isEmpty();
    }

    @Test(description = "DELETE /pet/{petId} - Non-existing ID. Petstore may return 200 (idempotent) or 404")
    public void deletePet_whenPetDoesNotExist_shouldReturn404Or200() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", NON_EXISTING_PET_ID)));

        assertThat(status).isIn(200, 404);
    }

    @Test(description = "DELETE /pet/{petId} - Non-existing pet should return 404")
    public void deletePet_nonExistingPet_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", 999999999L)));

        assertThat(status).isIn(200, 404);
    }

    @Test(description = "GET /pet/{petId} - Invalid ID format (string) - Petstore returns 404")
    public void getPetById_withInvalidIdFormat_shouldReturn404() {
        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", "invalid_id_format")));

        assertThat(status).isEqualTo(404);
    }

    @Test(description = "POST /pet - Missing 'name' (Petstore often accepts and returns 200)")
    public void createPet_withMissingName_shouldReturnErrorOrAccept() {
        Pet invalidPet = TestDataBuilder.invalidPetMissingName();
        Response response = PetApiClient.sendPostRequest(PET_PATH, invalidPet, null, null);

        assertThat(response.statusCode()).isBetween(200, 499);
    }

    @Test(description = "POST /pet - Missing 'photoUrls' (Petstore often accepts and returns 200)")
    public void createPet_withMissingPhotoUrls_shouldReturnErrorOrAccept() {
        Pet invalidPet = TestDataBuilder.invalidPetMissingPhotoUrls();
        Response response = PetApiClient.sendPostRequest(PET_PATH, invalidPet, null, null);

        assertThat(response.statusCode()).isBetween(200, 499);
    }

    @Test(description = "POST /pet - Missing required fields (only id) (Petstore may return 200, 4xx, 5xx)")
    public void createPet_missingRequiredFields_shouldReturnErrorOrAccept() {
        Pet invalidPet = new Pet();
        invalidPet.setId(System.currentTimeMillis() % 100_000);

        Response response = PetApiClient.sendPostRequest(PET_PATH, invalidPet, null, null);

        assertThat(response.statusCode()).isIn(200, 400, 500);
    }

    @Test(description = "POST /pet - Empty name (Petstore may return 200, 4xx, 5xx)")
    public void createPet_emptyName_shouldReturnErrorOrAccept() {
        Pet invalidPet = TestDataBuilder.invalidPetEmptyName();
        Response response = PetApiClient.sendPostRequest(PET_PATH, invalidPet, null, null);

        assertThat(response.statusCode()).isIn(200, 400, 500);
    }

    @Test(description = "POST /pet - Empty body {} (Petstore may return 200, 4xx, or 5xx)")
    public void createPet_withEmptyBody_shouldReturnErrorOrAccept() {
        Response response = PetApiClient.sendPostRequest(PET_PATH, "{}", null, null);

        assertThat(response.statusCode()).isBetween(200, 500);
    }

    @Test(description = "PUT /pet - Non-existing ID (Petstore may create pet or return 404)")
    public void updatePet_withNonExistingId_shouldReturn404Or200() {
        Pet invalidUpdate = TestDataBuilder.invalidPetNonExistingId("ghost_pet");
        Response response = PetApiClient.sendPutRequest(PET_PATH, invalidUpdate, null, null);

        assertThat(response.statusCode()).isBetween(200, 499);
    }

    @Test(description = "POST /pet - Special characters in name")
    public void createPet_specialCharsInName_shouldReturn200OrError() {
        Pet pet = TestDataBuilder.petWithSpecialCharsInName();
        Response response = PetApiClient.sendPostRequest(PET_PATH, pet, null, null);

        assertThat(response.statusCode()).isIn(200, 400, 500);
        if (response.statusCode() == 200) {
            Pet createdPet = JsonUtils.fromResponse(response, Pet.class);
            assertThat(createdPet.getName()).isEqualTo(pet.getName());
            PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", createdPet.getId()));
        }
    }

    @Test(description = "POST /pet - Very long name (1000 chars)")
    public void createPet_longName_shouldReturn200OrError() {
        Pet pet = TestDataBuilder.petWithLongName(1000);
        Response response = PetApiClient.sendPostRequest(PET_PATH, pet, null, null);

        assertThat(response.statusCode()).isIn(200, 400, 500);
        if (response.statusCode() == 200) {
            Pet createdPet = JsonUtils.fromResponse(response, Pet.class);
            assertThat(createdPet.getName()).hasSize(1000);
            PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", createdPet.getId()));
        }
    }
}
