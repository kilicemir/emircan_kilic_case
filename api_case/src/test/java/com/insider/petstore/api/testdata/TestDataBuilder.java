package com.insider.petstore.api.testdata;

import com.insider.petstore.api.model.Category;
import com.insider.petstore.api.model.Pet;
import com.insider.petstore.api.enums.PetStatus;
import com.insider.petstore.api.model.Tag;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TestDataBuilder {

    private static final String DEFAULT_PHOTO_URL = "https://example.com/photo.jpg";

    private TestDataBuilder() {
    }

    public static String uniquePetName() {
        return "pet_" + System.nanoTime() + "_" + Thread.currentThread().getId();
    }

    public static Pet petForCreate() {
        return Pet.builder()
                .name(uniquePetName())
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(PetStatus.available)
                .build();
    }

    public static Pet fullPet(Long id, String name, PetStatus status) {
        Category category = new Category();
        category.setId(1L);
        category.setName("Dogs");
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("friendly");
        return Pet.builder()
                .id(id)
                .name(name)
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(status)
                .category(category)
                .tags(List.of(tag))
                .build();
    }

    public static Pet petForUpdate(Pet existing, String newName, PetStatus newStatus) {
        return Pet.builder()
                .id(existing.getId())
                .name(newName)
                .photoUrls(existing.getPhotoUrls())
                .status(newStatus)
                .category(existing.getCategory())
                .tags(existing.getTags())
                .build();
    }

    public static Pet invalidPetMissingName() {
        return Pet.builder()
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(PetStatus.available)
                .build();
    }

    public static Pet invalidPetMissingPhotoUrls() {
        return Pet.builder()
                .name("doggie")
                .status(PetStatus.available)
                .build();
    }

    public static Pet invalidPetNonExistingId(String name) {
        long nonExistingId = 77_777_777_777L;
        return fullPet(nonExistingId, name, PetStatus.available);
    }

    public static Pet invalidPetEmptyName() {
        return Pet.builder()
                .name("")
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(PetStatus.available)
                .build();
    }

    public static Pet petWithSpecialCharsInName() {
        return Pet.builder()
                .name("Pet!@#$%^&*()_+-=[]{}|;':\",./<>?")
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(PetStatus.available)
                .build();
    }

    public static Pet petWithLongName(int length) {
        return Pet.builder()
                .name("A".repeat(length))
                .photoUrls(Collections.singletonList(DEFAULT_PHOTO_URL))
                .status(PetStatus.available)
                .build();
    }

    public static Pet petWithMultipleTagsAndPhotoUrls(String... tagNames) {
        List<Tag> tags = Arrays.stream(tagNames)
                .map(t -> {
                    Tag tag = new Tag();
                    tag.setName(t);
                    return tag;
                })
                .toList();
        return Pet.builder()
                .name(uniquePetName())
                .photoUrls(Arrays.asList(
                        "https://example.com/photo1.jpg",
                        "https://example.com/photo2.jpg",
                        "https://example.com/photo3.jpg"
                ))
                .status(PetStatus.available)
                .tags(tags)
                .build();
    }

    public static Pet petForPartialNameUpdate(Pet existing, String newName) {
        return Pet.builder()
                .id(existing.getId())
                .name(newName)
                .status(existing.getStatus())
                .photoUrls(existing.getPhotoUrls())
                .category(existing.getCategory())
                .tags(existing.getTags())
                .build();
    }

    public static Pet petForPartialStatusUpdate(Pet existing, PetStatus newStatus) {
        return Pet.builder()
                .id(existing.getId())
                .name(existing.getName())
                .status(newStatus)
                .photoUrls(existing.getPhotoUrls())
                .category(existing.getCategory())
                .tags(existing.getTags())
                .build();
    }
}
