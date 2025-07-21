# Hitachi Channel Solutions Indonesia - Backend API Assessment

**Nama:** [Your Name]
**Tanggal:** 22 Juli 2025

## Deskripsi Proyek

Aplikasi ini adalah server backend API yang dibangun menggunakan Spring Boot sesuai dengan assessment yang diberikan. Tujuannya adalah untuk mengelola data transaksi pelanggan, yang dioperasikan oleh staf perusahaan melalui REST API. Aplikasi ini mencakup fungsionalitas CRUD untuk pengguna (staf), pelanggan, produk, dan transaksi, serta menyediakan endpoint untuk pelaporan data.

Untuk dokumentasi detail setiap endpoint API, silakan merujuk ke file **`API_DOCUMENTATION.md`**.

## Fitur Utama

-   **Keamanan:** Otentikasi berbasis JWT (JSON Web Token) yang Stateless dengan Spring Security 6.
-   **Manajemen Pengguna (Staf):** Operasi CRUD penuh untuk pengguna dengan sistem peran (`ADMIN`, `STAFF`).
-   **Manajemen Pelanggan:** Operasi CRUD untuk pelanggan, yang dapat dikelola oleh staf. Pelanggan juga dapat login untuk melihat profil dan riwayat transaksi mereka.
-   **Manajemen Produk:** Operasi CRUD untuk produk, termasuk pengelolaan harga dan asosiasi pajak (`Many-to-Many`).
-   **Manajemen Transaksi:**
    -   Pembuatan transaksi dengan kalkulasi harga dan pajak otomatis.
    -   Pencarian transaksi dengan filter dinamis (rentang tanggal, nama pelanggan, status, dll.).
-   **Pelaporan:** Agregasi total belanja per pelanggan, per pajak, dan per produk.

## Teknologi yang Digunakan

-   **Framework:** Spring Boot 3.1.x
-   **Keamanan:** Spring Security 6
-   **Database:** Spring Data JPA, Hibernate
-   **Tipe Database:** PostgreSQL
-   **Keamanan API:** JSON Web Token (JWT)
-   **Build Tool:** Maven
-   **Lain-lain:** Lombok, Jakarta Persistence API

## Prasyarat

Sebelum menjalankan proyek ini, pastikan Anda telah menginstal:
1.  JDK 17 atau lebih tinggi.
2.  Apache Maven.
3.  PostgreSQL (atau Docker untuk menjalankan instance PostgreSQL).
4.  Postman atau klien API lainnya.

## Penyiapan dan Instalasi

Ikuti langkah-langkah berikut untuk menjalankan aplikasi secara lokal.

### 1. Penyiapan Database
Buat sebuah database baru di PostgreSQL.
```sql
CREATE DATABASE hitachi_assessment;
```

### 2. Konfigurasi Aplikasi
Buka file src/main/resources/application.properties. Sesuaikan properti berikut agar sesuai dengan konfigurasi database lokal Anda:

# Ganti dengan nilai konfigurasi PostgreSQL Anda
spring.datasource.url=jdbc:postgresql://localhost:5432/hitachi_assessment
spring.datasource.username=postgres
spring.datasource.password=your_database_password

### Inisialisasi Data Awal (Penting!)
Proyek ini menggunakan seeder berbasis Java (DataSeeder.java) untuk inisialisasi data awal, bukan file .sql statis seperti yang diminta pada assessment.
Pendekatan ini dipilih karena alasan teknis yang krusial:
1. Keamanan Enkripsi Password: Memungkinkan enkripsi password yang aman untuk pengguna admin pertama menggunakan PasswordEncoder dari Spring Security, sebuah praktik keamanan esensial yang tidak mungkin dilakukan dengan file .sql biasa yang menyimpan password sebagai teks biasa.
2. Portabilitas Database: Kode seeder Java tidak terikat pada dialek SQL tertentu (PostgreSQL, MySQL, dll.) dan akan berfungsi di berbagai database yang didukung oleh Hibernate.
3. Idempotent: Seeder ini dirancang untuk memeriksa apakah data sudah ada sebelum membuatnya, sehingga aman untuk dijalankan berulang kali tanpa menyebabkan error duplikasi data.

Saat aplikasi pertama kali dijalankan, seeder akan secara otomatis membuat semua peran yang dibutuhkan dan satu pengguna ADMIN default. Kredensialnya diatur di application.properties:

app.admin-username=admin
app.admin-email=admin@hcs.com
app.admin-password=secretpassword123

### 4. Menjalankan Aplikasi
Buka terminal di direktori root proyek dan jalankan perintah Maven berikut:

mvn spring-boot:run

Atau, Anda bisa menjalankan metode main di kelas ApiAssessmentApplication.java dari IDE Anda. Aplikasi akan berjalan di port 8080 (default).