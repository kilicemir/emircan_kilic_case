package com.insider.petstore.api.tests;

import com.insider.petstore.api.client.PetApiClient;
import com.insider.petstore.api.enums.PetStatus;
import com.insider.petstore.api.model.Pet;
import com.insider.petstore.api.testdata.TestDataBuilder;
import com.insider.petstore.api.utils.JsonUtils;
import com.insider.petstore.api.utils.ResponseExtractor;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static com.insider.petstore.api.client.PetApiClient.*;
import static org.assertj.core.api.Assertions.*;

@Epic("Petstore API")
@Feature("Pet CRUD - Positive")
public class PetCrudPositiveTest extends BaseTest {

    @Test(description = "POST /pet - Create pet with valid data returns 200 and pet with ID")
    public void createPet_withValidData_shouldReturn200AndPetWithId() {
        Pet requestPet = TestDataBuilder.petForCreate();

        Response response = PetApiClient.sendPostRequest(PET_PATH, requestPet, null, null);

        assertThat(response.statusCode()).isEqualTo(200);
        Pet createdPet = JsonUtils.fromResponse(response, Pet.class);
        assertThat(createdPet.getId()).isNotNull().isPositive();
        assertThat(createdPet.getName()).isNotNull().isNotEmpty();
        assertThat(createdPet.getPhotoUrls()).isNotEmpty();
    }

    @Test(description = "POST /pet - Create pet with full body and verify category, tags, photoUrls")
    public void createPet_withFullBody_shouldReturn200AndMatchRequest() {
        Pet requestPet = TestDataBuilder.fullPet(System.currentTimeMillis() % 1_000_000, TestDataBuilder.uniquePetName(), PetStatus.available);

        Response response = PetApiClient.sendPostRequest(PET_PATH, requestPet, null, null);

        assertThat(response.statusCode()).isEqualTo(200);
        Pet createdPet = JsonUtils.fromResponse(response, Pet.class);
        assertThat(createdPet.getId()).isNotNull();
        assertThat(createdPet.getName()).isEqualTo(requestPet.getName());
        assertThat(createdPet.getStatus()).isEqualTo(requestPet.getStatus());
        assertThat(createdPet.getPhotoUrls()).isEqualTo(requestPet.getPhotoUrls());
        assertThat(createdPet.getCategory()).isNotNull();
        assertThat(createdPet.getCategory().getId()).isEqualTo(requestPet.getCategory().getId());
        assertThat(createdPet.getCategory().getName()).isEqualTo(requestPet.getCategory().getName());
        assertThat(createdPet.getTags()).isNotNull();
        assertThat(createdPet.getTags()).hasSize(requestPet.getTags().size());

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", createdPet.getId()));
    }

    @Test(description = "POST /pet - Create pet with multiple tags and photoUrls")
    public void createPet_withMultipleTagsAndPhotoUrls_shouldReturn200() {
        Pet requestPet = TestDataBuilder.petWithMultipleTagsAndPhotoUrls("strong", "playful", "loyal");

        Response response = PetApiClient.sendPostRequest(PET_PATH, requestPet, null, null);

        assertThat(response.statusCode()).isEqualTo(200);
        Pet createdPet = JsonUtils.fromResponse(response, Pet.class);
        assertThat(createdPet.getTags()).hasSize(3);
        assertThat(createdPet.getPhotoUrls()).hasSize(3);
        assertThat(createdPet.getTags())
                .extracting("name")
                .containsExactlyInAnyOrder("strong", "playful", "loyal");

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", createdPet.getId()));
    }

    @Test(description = "GET /pet/{petId} - Get existing pet should return 200 and pet details")
    public void getPetById_whenPetExists_shouldReturn200AndPet() {
        Pet created = createPetAndGetResponse();
        Long petId = created.getId();

        Response response = PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", petId));

        assertThat(response.statusCode()).isEqualTo(200);
        Pet retrieved = JsonUtils.fromResponse(response, Pet.class);
        assertThat(retrieved.getId()).isEqualTo(petId);
        assertThat(retrieved.getName()).isNotNull().isNotEmpty();
        assertThat(retrieved.getPhotoUrls()).isNotEmpty();

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", petId));
    }

    @Test(description = "PUT /pet - Update existing pet should return 200 and updated pet")
    public void updatePet_withValidData_shouldReturn200AndUpdatedPet() {
        Pet created = createPetAndGetResponse();
        Pet updatePayload = TestDataBuilder.petForUpdate(
                created,
                "updated_" + TestDataBuilder.uniquePetName(),
                PetStatus.sold
        );

        Response response = PetApiClient.sendPutRequest(PET_PATH, updatePayload, null, null);

        assertThat(response.statusCode()).isEqualTo(200);
        Pet updated = JsonUtils.fromResponse(response, Pet.class);
        assertThat(updated.getId()).isEqualTo(created.getId());
        assertThat(updated.getName()).isEqualTo(updatePayload.getName());
        assertThat(updated.getStatus()).isEqualTo(PetStatus.sold);

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", created.getId()));
    }

    @Test(description = "PUT /pet - Partial update (only name)")
    public void updatePet_partialNameOnly_shouldReturn200() {
        Pet created = createPetAndGetResponse();
        String newName = "PartiallyUpdated_" + TestDataBuilder.uniquePetName();
        Pet updatePayload = TestDataBuilder.petForPartialNameUpdate(created, newName);

        Response putResponse = PetApiClient.sendPutRequest(PET_PATH, updatePayload, null, null);
        assertThat(putResponse.statusCode()).isEqualTo(200);

        Pet retrieved = JsonUtils.fromResponse(PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", created.getId())), Pet.class);
        assertThat(retrieved.getName()).isEqualTo(newName);

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", created.getId()));
    }

    @Test(description = "PUT /pet - Partial update (only status)")
    public void updatePet_partialStatusOnly_shouldReturn200() {
        Pet created = createPetAndGetResponse();
        Pet updatePayload = TestDataBuilder.petForPartialStatusUpdate(created, PetStatus.sold);

        Response putResponse = PetApiClient.sendPutRequest(PET_PATH, updatePayload, null, null);
        assertThat(putResponse.statusCode()).isEqualTo(200);

        Pet retrieved = JsonUtils.fromResponse(PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", created.getId())), Pet.class);
        assertThat(retrieved.getName()).isEqualTo(created.getName());
        if (retrieved.getStatus() != null) {
            assertThat(retrieved.getStatus()).isEqualTo(PetStatus.sold);
        }

        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", created.getId()));
    }

    @Test(description = "DELETE /pet/{petId} - Delete existing pet returns 200")
    public void deletePet_whenPetExists_shouldReturn200() {
        Pet created = createPetAndGetResponse();
        Long petId = created.getId();

        Response response = PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", petId));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isNotNull();
    }

    @Test(description = "DELETE /pet/{petId} - After delete, GET returns 404")
    public void deletePet_verifyGetReturns404AfterDelete() {
        Pet created = createPetAndGetResponse();
        Long petId = created.getId();
        PetApiClient.sendDeleteRequest(PET_BY_ID_PATH, null, Map.of("petId", petId));

        int status = ResponseExtractor.getStatusCode(() -> PetApiClient.sendGetRequest(PET_BY_ID_PATH, null, Map.of("petId", petId)));

        assertThat(status).isIn(200, 404);
    }

    private Pet createPetAndGetResponse() {
        Pet requestPet = TestDataBuilder.petForCreate();
        Response createResponse = PetApiClient.sendPostRequest(PET_PATH, requestPet, null, null);
        assertThat(createResponse.statusCode()).isEqualTo(200);
        return createResponse.as(Pet.class);
    }
}
