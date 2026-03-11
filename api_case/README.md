# API Test Automation

Petstore API için Rest Assured ile yazılmış API test otomasyon projesi.

## Gereksinimler

- Java 17+
- Maven 3.6+

## Kurulum

```bash
git clone <repository-url>
cd api_case
mvn clean install -DskipTests
```

## Testleri Çalıştırma

```bash
# Testleri çalıştır
mvn clean test

# Allure raporu oluştur
mvn allure:report

# Raporu tarayıcıda aç
mvn allure:serve

# Tek test sınıfı çalıştır
mvn test -Dtest=PetSmokeTest
```

## Proje Yapısı

```
src/
├── main/java/com/insider/petstore/api/
│   ├── client/          # BaseApiClient, PetApiClient
│   ├── config/           # ApiConfig (api.properties)
│   ├── model/            # Pet, Category, Tag, ApiErrorResponse
│   └── utils/            # JsonUtils
├── main/resources/
│   └── api.properties
└── test/java/com/insider/petstore/api/
    ├── testdata/         # TestDataBuilder
    ├── tests/             # BaseTest, PetCrudPositiveTest, PetCrudNegativeTest, PetSmokeTest
    └── utils/             # ResponseExtractor
```

## Test Senaryoları

### CRUD İşlemleri (Pozitif)
- **POST /pet** - Yeni pet oluşturma (tek / çoklu tag, photoUrls)
- **GET /pet/{id}** - Pet bilgilerini getirme
- **PUT /pet** - Pet güncelleme (tam / partial name / partial status)
- **DELETE /pet/{id}** - Pet silme ve sonrasında GET ile 404 doğrulama

### Negatif Senaryolar
- **GET /pet/{id}** - Var olmayan, geçersiz, negatif, sıfır ID ile 404
- **GET /pet/findByStatus** - Geçersiz status ile boş liste
- **DELETE /pet/{id}** - Var olmayan pet silme (404 veya 200)
- **POST /pet** - Eksik alanlar, boş isim, boş body, özel karakterler, uzun isim
- **PUT /pet** - Var olmayan ID ile güncelleme

### Smoke
- **GET /pet/findByStatus** - available, pending, sold statusları ile liste dönüşü

## Teknolojiler

- Java 17
- Rest Assured 5.4.0
- TestNG 7.9.0
- Allure Reports 2.25.0
- Jackson 2.16.1
- Lombok 1.18.30
- AssertJ 3.25.1
