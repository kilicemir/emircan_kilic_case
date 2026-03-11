# emircan_kilic_case

Üç modüllü test otomasyon projesi: API testleri, UI testleri ve yük testleri. Proje Petstore API, Insider kariyer sayfası ve n11 arama modülü üzerinde çalışır.

---

## Proje Yapısı

```
emircan_kilic_case/
├── api_case/      → Petstore API test otomasyonu (Rest Assured)
├── ui_case/       → Insider kariyer sayfası UI test otomasyonu (Selenium)
└── load_case/     → n11.com arama modülü yük testi (JMeter)
```

---

## Modül Özeti

| Modül | Açıklama | Teknolojiler |
|-------|----------|--------------|
| **api_case** | Petstore API üzerinde REST API testleri (CRUD, negatif, smoke) | Java 17, Rest Assured, TestNG, Allure |
| **ui_case** | insiderone.com kariyer sayfası end-to-end UI testleri | Java 17, Selenium WebDriver, TestNG, Allure |
| **load_case** | n11.com arama sayfası performans ve yük testleri | JMeter 5.6+ |

---

## 1. API Test Modülü (`api_case`)

Petstore API için hazırlanmış REST API test otomasyonu. Pozitif, negatif ve smoke senaryoları kapsar.

### Gereksinimler
- Java 17+
- Maven 3.6+

### Çalıştırma
```bash
cd api_case
mvn clean install -DskipTests
mvn clean test
mvn allure:serve
```

### Test Senaryoları
- **CRUD (Pozitif):** POST /pet, GET /pet/{id}, PUT /pet, DELETE /pet/{id}
- **Negatif:** Geçersiz ID, eksik alanlar, boş body vb.
- **Smoke:** findByStatus ile durum bazlı listeleme

### Detaylı Bilgi
→ [api_case/README.md](api_case/README.md)

---

## 2. UI Test Modülü (`ui_case`)

Insider kariyer sayfası için Selenium WebDriver ile yazılmış UI otomasyonu. Kariyer başvuru akışını uçtan uca test eder.

### Gereksinimler
- Java 17+
- Maven 3.6+
- Chrome veya Firefox (WebDriverManager otomatik indirir)

### Çalıştırma
```bash
cd ui_case
mvn clean install -DskipTests
mvn clean test
mvn allure:serve
```

### Test Akışı (InsiderFullFlowTest)
1. **Ana Sayfa** — insiderone.com açılır, temel bloklar doğrulanır
2. **Kariyer Sayfası** — QA ekibi seçilir, Istanbul lokasyonu ve departman filtrelenir
3. **İlan Listesi** — Filtrelenmiş ilanların ve bilgilerin doğruluğu kontrol edilir
4. **Lever Başvuru** — İlan açılır, Lever sayfasında başvuru formu ve ilan detayları doğrulanır

### Detaylı Bilgi
→ [ui_case/README.md](ui_case/README.md)

---

## 3. Yük Test Modülü (`load_case`)

n11.com arama modülü için JMeter tabanlı yük ve performans testi.

### Gereksinimler
- Java 8+
- JMeter 5.6+

### Çalıştırma

GUI ile:
```bash
cd load_case
jmeter -t n11-search-module-load-test.jmx
```

CLI ile raporlu:
```bash
jmeter -n -t n11-search-module-load-test.jmx -l results.jtl -e -o report/
```

### Senaryo
1. Ana sayfa (`/`) açılır
2. Sırayla aramalar: `klavye` → `airpods` → `mouse`
3. Her adımda HTTP 200 ve ilgili kelimenin sayfada geçmesi doğrulanır
4. İstekler arası 1–3 sn think time

### Detaylı Bilgi
→ [load_case/README.md](load_case/README.md)

---

## Kurulum Özeti

| Modül | Kurulum Komutu |
|-------|----------------|
| api_case | `cd api_case && mvn clean install -DskipTests` |
| ui_case | `cd ui_case && mvn clean install -DskipTests` |
| load_case | JMeter kurulumu + `jmeter -t n11-search-module-load-test.jmx` |

---

## Raporlar

- **api_case** ve **ui_case:** Allure raporları kullanır. `mvn allure:serve` ile tarayıcıda rapor açılır.
- **load_case:** `jmeter -n ... -e -o report/` ile HTML performans raporu üretilir.

---

