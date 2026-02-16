# miniFlightApp ✈️

miniFlightApp, Spring Boot kullanılarak geliştirilmiş JWT tabanlı kimlik doğrulama ve rol bazlı yetkilendirme içeren bir uçuş/rezervasyon backend uygulamasıdır.

Proje; kullanıcı kayıt/giriş işlemleri, access + refresh token yönetimi, admin tarafından flight CRUD işlemleri ve kullanıcı tarafından booking oluşturma süreçlerini içerir.

---

##  Özellikler

-  JWT Access Token + Refresh Token (HttpOnly cookie) 
-  Role-based authorization (USER / ADMIN)
-  Flight CRUD (Admin yetkisi)
-  Booking oluşturma (User yetkisi)
-  PostgreSQL + JPA (Hibernate)
-  Katmanlı mimari (Controller → Service → Repository)
-  Seed data desteği

---

##  Kullanılan Teknolojiler

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT
- PostgreSQL
- Maven

---

##  Kurulum

### 1️⃣ Gereksinimler
- Java 21
- PostgreSQL
- Maven

### 2️⃣ Konfigürasyon

`application.properties` dosyası güvenlik sebebiyle repoya dahil edilmedi :D

Örnek dosyayı kopyalayarak kendi dosya yapılandırmanızı oluşturabilirsiniz:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
