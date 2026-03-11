# n11 Search Load Test

> n11.com arama sayfası için JMeter tabanlı yük testi

---

### Nasıl çalıştırılır?

```bash
jmeter -t n11-search-module-load-test.jmx
```

Raporla birlikte (CLI):

```bash
jmeter -n -t n11-search-module-load-test.jmx -l results.jtl -e -o report/
```

> Java 8+ ve [JMeter 5.6+](https://jmeter.apache.org/download_jmeter.cgi) gerekir.

---

### Test ne yapar?

1. Ana sayfa (`/`) açılır
2. 3 arama sırayla yapılır: `klavye` → `airpods` → `mouse`
3. Her adımda HTTP 200 ve ilgili kelimenin sayfada geçmesi kontrol edilir
4. İstekler arası 1–3 sn bekleme (think time)

---

### Senaryo detayı

| # | İstek | Doğrulama |
|---|-------|------------|
| 1 | `GET /` | "n11" |
| 2 | `GET /arama?q=klavye` | "klavye" |
| 3 | `GET /arama?q=airpods` | "airpods" |
| 4 | `GET /arama?q=mouse` | "mouse" |

---

### Çıktılar

- `results.jtl` — Ham sonuçlar
- `report/index.html` — HTML rapor (TPS, latency, hata oranı)
