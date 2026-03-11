# UI Test Automation

Insider career sayfası için Selenium WebDriver ile yazılmış UI test otomasyon projesi.

## Gereksinimler

- Java 17+
- Maven 3.6+
- Chrome veya Firefox (WebDriverManager otomatik indirir)

## Kurulum

```bash
git clone <repository-url>
cd ui_case
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
mvn test -Dtest=InsiderFullFlowTest
```

## Proje Yapısı

```
src/
├── main/java/com/insider/
│   ├── base/              # BasePage
│   ├── pages/             # HomePage, CareersPage, JobListingsPage, LeverPage
│   └── utils/             # DriverFactory, DriverManager, ConfigReader, WaitUtils, ElementUtils, ScreenshotUtils
├── main/resources/
│   └── (yok)
└── test/java/com/insider/
    ├── listeners/         # TestListener (Allure attachment, screenshot)
    ├── tests/             # BaseTest, InsiderFullFlowTest
    └── resources/
        ├── config.properties
        ├── testng.xml
        └── allure.properties
```

## Test Senaryoları

### Kariyer Başvuru Akışı (InsiderFullFlowTest)

1. **Ana Sayfa** — `https://insiderone.com/` açılır, ana blokların yüklendiği doğrulanır
2. **Kariyer Sayfası** — QA ekibi seçilir, konum (Istanbul, Turkiye) ve departman filtrelenir
3. **İlan Listesi** — Filtrelenmiş ilanların göründüğü, pozisyon/departman/lokasyon bilgilerinin doğru olduğu kontrol edilir
4. **Lever Başvuru** — İlk ilan açılır, Lever sayfasında başvuru formu ve ilan detaylarının (Software Quality Assurance Engineer, Istanbul, QA) doğruluğu doğrulanır

## Konfigürasyon

`src/test/resources/config.properties` dosyasından ayarlanabilir:

| Ayar | Açıklama | Varsayılan |
|------|----------|------------|
| `insider.homepage.url` | Ana sayfa URL | https://insiderone.com/ |
| `insider.careers.page.url` | Kariyer sayfası URL | https://insiderone.com/careers/quality-assurance/ |
| `default.browser` | Tarayıcı (chrome / firefox) | chrome |
| `default.timeout` | Varsayılan bekleme süresi (saniye) | 30 |

## Teknolojiler

- Java 17
- Selenium WebDriver 4.25.0
- TestNG 7.10.2
- WebDriverManager 5.9.2
- Allure Reports 2.24.0
- AssertJ 3.26.3
- Commons IO 2.16.1
