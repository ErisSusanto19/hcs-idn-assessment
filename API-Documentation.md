## Referensi API (Endpoints)

Berikut adalah dokumentasi untuk endpoint-endpoint kunci. Semua endpoint yang memerlukan autentikasi harus menyertakan `Authorization: Bearer <JWT_TOKEN>` di header.

### 1. Autentikasi (`/api/auth`)

Endpoint ini digunakan untuk proses login dan mendapatkan token.

---

#### `POST /api/auth/login`

Melakukan autentikasi untuk semua jenis pengguna (Admin, Staff, Customer) dan mengembalikan JWT jika kredensial valid.

-   **Otorisasi:** Publik (tidak memerlukan token).
-   **Request Body:**
    ```json
    {
        "login": "admin",
        "password": "secretpassword123"
    }
    ```
    *Catatan: `login` bisa diisi dengan `username` atau `email`.*

-   **Success Response (200 OK):**
    ```json
    {
        "message": "User authenticated successfully.",
        "code": 200,
        "data": {
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1MzI4MTcwMywiZXhwIjoxNzUzMzY4MTAzLCJpZCI6ImM0ZDNkMmUxLWY2YTgtNGI5Yy04ZTdkLTlmMGExYjJjM2Q0ZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXX0.abcdefg...",
            "id": "c4d3d2e1-f6a8-4b9c-8e7d-9f0a1b2c3d4e",
            "username": "admin",
            "email": "admin@hcs.com",
            "roles": [
                {
                    "authority": "ROLE_ADMIN"
                }
            ]
        },
        "pagination": null,
        "timestamp": "2025-07-22T10:05:00.12345"
    }
    ```

-   **Error Response (401 Unauthorized):**
    Terjadi jika password salah atau pengguna tidak ditemukan.
    ```json
    {
        "message": "Unauthorized: Bad credentials",
        "code": 401,
        "data": null,
        "pagination": null,
        "timestamp": "2025-07-22T10:06:15.67890"
    }
    ```



### 2. Users / Staf (`/api/v1/users`)

Endpoint ini digunakan untuk mengelola data pengguna internal (staf).

---

#### `POST /api/v1/users`

Membuat pengguna (staf) baru. Operasi ini hanya dapat dilakukan oleh **ADMIN**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN`.
-   **Request Body:**
    ```json
    {
        "fullName": "Budi Santoso",
        "username": "budi.staff",
        "email": "budi.staff@hcs.com",
        "password": "password123",
        "roles": ["STAFF"]
    }
    ```
    *Catatan: `roles` adalah array yang bisa berisi `"ADMIN"` atau `"STAFF"`.*

-   **Success Response (201 Created):**
    ```json
    {
        "message": "User created successfully by admin.",
        "code": 201,
        "data": {
            "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
            "fullName": "Budi Santoso",
            "username": "budi.staff",
            "email": "budi.staff@hcs.com",
            "roles": ["STAFF"],
            "createdAt": "2025-07-22T10:15:30.54321"
        },
        "pagination": null,
        "timestamp": "2025-07-22T10:15:30.55555"
    }
    ```

---

#### `GET /api/v1/users`

Mendapatkan daftar semua pengguna (staf). Operasi ini hanya dapat dilakukan oleh **ADMIN**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN`.
-   **Query Params (Opsional):** Jika Anda mengimplementasikan paginasi, Anda bisa menambahkan `?page=0&size=10`.
-   **Success Response (200 OK):**
    ```json
    {
        "message": "All users retrieved successfully.",
        "code": 200,
        "data": [
            {
                "id": "c4d3d2e1-f6a8-4b9c-8e7d-9f0a1b2c3d4e",
                "fullName": "Default Admin User",
                "username": "admin",
                "email": "admin@hcs.com",
                "roles": ["ADMIN"],
                "createdAt": "2025-07-22T10:00:00.00000"
            },
            {
                "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                "fullName": "Budi Santoso",
                "username": "budi.staff",
                "email": "budi.staff@hcs.com",
                "roles": ["STAFF"],
                "createdAt": "2025-07-22T10:15:30.54321"
            }
        ],
        "pagination": null, // atau isi dengan data paginasi
        "timestamp": "2025-07-22T10:20:05.11223"
    }
    ```

---

#### `GET /api/v1/users/me`

Mendapatkan detail profil dari pengguna (staf) yang sedang login. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Success Response (200 OK):**
    Sama seperti objek tunggal di dalam array `GET /api/v1/users`.

---

#### `PUT /api/v1/users/me`

Memperbarui detail profil dari pengguna (staf) yang sedang login. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Request Body:**
    ```json
    {
        "fullName": "Budi Santoso Updated"
    }
    ```
    *Catatan: Hanya field yang diizinkan di `UserProfileUpdateDTO` yang dapat diubah.*

-   **Success Response (200 OK):**
    Respons akan berisi data profil yang sudah diperbarui.

---

#### `DELETE /api/v1/users/{id}`

Menghapus seorang pengguna (staf). Operasi ini hanya dapat dilakukan oleh **ADMIN**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN`.
-   **Path Variable:** `id` dari pengguna yang ingin dihapus.
-   **Success Response (200 OK):**
    ```json
    {
        "message": "User deleted successfully.",
        "code": 200,
        "data": "User with id a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d was deleted.",
        "pagination": null,
        "timestamp": "2025-07-22T10:25:10.98765"
    }
    ```



### 3. Customers (`/api/v1/customers`)

Endpoint ini digunakan untuk mengelola data pelanggan. Staf dapat mengelola semua pelanggan, sementara pelanggan hanya dapat mengakses data mereka sendiri.

---

#### `POST /api/v1/customers`

Mendaftarkan seorang pelanggan baru. Operasi ini dapat dilakukan oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Request Body:**
    ```json
    {
        "name": "PT. Cipta Karya",
        "birthdate": "2005-10-20",
        "birthplace": "Jakarta",
        "username": "ciptakarya_cust",
        "email": "contact@ciptakarya.com",
        "password": "customerpassword"
    }
    ```

-   **Success Response (201 Created):**
    ```json
    {
        "message": "Customer created successfully.",
        "code": 201,
        "data": {
            "id": "b2c3d4e5-f6a7-4b8c-9d0e-1f2a3b4c5d6e",
            "name": "PT. Cipta Karya",
            "birthdate": "2005-10-20",
            "birthplace": "Jakarta",
            "username": "ciptakarya_cust",
            "email": "contact@ciptakarya.com",
            "createdBy": {
                "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                "fullName": "Budi Santoso"
            },
            "updatedBy": {
                "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                "fullName": "Budi Santoso"
            },
            "createdAt": "2025-07-22T10:30:00.12345",
            "updatedAt": "2025-07-22T10:30:00.12345"
        },
        "pagination": null,
        "timestamp": "2025-07-22T10:30:00.13456"
    }
    ```

---

#### `GET /api/v1/customers`

Mendapatkan daftar semua pelanggan. Dapat diakses oleh **ADMIN** dan **STAFF**. Sangat disarankan untuk menggunakan paginasi.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Query Params (Opsional):** `?page=0&size=5&sort=name,asc`
-   **Success Response (200 OK):**
    ```json
    {
        "message": "All customers retrieved successfully.",
        "code": 200,
        "data": [
            // ... array of customer objects like the one above ...
        ],
        "pagination": {
            "currentPage": 0,
            "totalElements": 25,
            "totalPages": 5,
            "pageSize": 5
        },
        "timestamp": "2025-07-22T10:35:15.45678"
    }
    ```

---

#### `GET /api/v1/customers/me`

Mendapatkan detail profil dari pelanggan yang sedang login. Hanya dapat diakses oleh **CUSTOMER**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_CUSTOMER`.
-   **Success Response (200 OK):**
    Respons akan berisi objek data pelanggan yang sedang login, mirip dengan respons `POST`.

---

#### `PUT /api/v1/customers/me`

Memperbarui detail profil dari pelanggan yang sedang login. Hanya dapat diakses oleh **CUSTOMER**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_CUSTOMER`.
-   **Request Body:**
    ```json
    {
        "name": "PT. Cipta Karya Persada",
        "birthplace": "Bandung"
    }
    ```
-   **Success Response (200 OK):**
    Respons akan berisi data profil pelanggan yang sudah diperbarui.

---

#### `GET /api/v1/customers/me/transactions`

Mendapatkan riwayat transaksi dari pelanggan yang sedang login. Hanya dapat diakses oleh **CUSTOMER**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_CUSTOMER`.
-   **Query Params (Opsional):**
    - `startDate`: `2025-01-01T00:00:00`
    - `endDate`: `2025-06-30T23:59:59`
    - Paginasi: `?page=0&size=10`
-   **Success Response (200 OK):**
    Respons akan berisi daftar transaksi (akan didokumentasikan di bagian Transaksi).



### 4. Taxes & Products (`/api/v1/taxes` & `/api/v1/products`)

Endpoint ini digunakan untuk mengelola data master untuk pajak dan produk.

---

#### `POST /api/v1/taxes`

Membuat jenis pajak baru. Hanya dapat diakses oleh **ADMIN**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN`.
-   **Request Body:**
    ```json
    {
        "name": "PPN",
        "rate": 11.00
    }
    ```

-   **Success Response (201 Created):**
    ```json
    {
        "message": "Tax created successfully.",
        "code": 201,
        "data": {
            "id": "tax-uuid-1",
            "name": "PPN",
            "rate": 11.00
        },
        "pagination": null,
        "timestamp": "2025-07-22T10:40:00.11111"
    }
    ```

---

#### `GET /api/v1/taxes`

Mendapatkan daftar semua jenis pajak. Hanya dapat diakses oleh **ADMIN**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN`.
-   **Success Response (200 OK):**
    Respons akan berisi array dari objek pajak di dalam field `data`.

---
---

#### `POST /api/v1/products`

Membuat sebuah produk baru. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Request Body:**
    ```json
    {
        "name": "Laptop Pro 14 inch",
        "price": 25000000.00,
        "taxIds": [
            "tax-uuid-1" 
        ]
    }
    ```
    *Catatan: `taxIds` adalah array dari UUID pajak yang ingin diasosiasikan. Bisa berupa array kosong `[]` jika tidak ada pajak.*

-   **Success Response (201 Created):**
    ```json
    {
        "message": "Product created successfully.",
        "code": 201,
        "data": {
            "id": "product-uuid-1",
            "name": "Laptop Pro 14 inch",
            "price": 25000000.00,
            "taxes": [
                {
                    "id": "tax-uuid-1",
                    "name": "PPN",
                    "rate": 11.00
                }
            ]
        },
        "pagination": null,
        "timestamp": "2025-07-22T10:45:00.22222"
    }
    ```

---

#### `GET /api/v1/products`

Mendapatkan daftar semua produk. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Query Params (Opsional):** `?page=0&size=10`
-   **Success Response (200 OK):**
    Respons akan berisi daftar produk yang dipaginasi di dalam field `data`.



### 5. Transactions & Reports (`/api/v1/transactions` & `/api/v1/reports`)

Endpoint ini digunakan untuk membuat transaksi dan menghasilkan laporan dari data yang terkumpul.

---

#### `POST /api/v1/transactions`

Membuat sebuah transaksi baru untuk pelanggan. Dapat diakses oleh **ADMIN** dan **STAFF**. Server akan menghitung total harga dan pajak secara otomatis.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Request Body:**
    ```json
    {
        "customerId": "b2c3d4e5-f6a7-4b8c-9d0e-1f2a3b4c5d6e",
        "transactionTime": "2025-07-22T11:00:00",
        "paymentStatus": "PAID",
        "paymentMethod": "CREDIT_CARD",
        "items": [
            {
                "productId": "product-uuid-1",
                "quantity": 1
            },
            {
                "productId": "product-uuid-2",
                "quantity": 2
            }
        ]
    }
    ```

-   **Success Response (201 Created):**
    ```json
    {
        "message": "Transaction created successfully.",
        "code": 201,
        "data": {
            "id": "transaction-uuid-1",
            "transactionTime": "2025-07-22T11:00:00",
            "paymentStatus": "PAID",
            "paymentMethod": "CREDIT_CARD",
            "netAmount": 50000000.00,
            "totalTax": 5500000.00,
            "totalAmountPaid": 55500000.00,
            "customer": {
                "id": "b2c3d4e5-f6a7-4b8c-9d0e-1f2a3b4c5d6e",
                "name": "PT. Cipta Karya",
                "email": "contact@ciptakarya.com"
            },
            "createdBy": {
                "id": "a1b2c3d4-e5f6-4a7b-8c9d-0e1f2a3b4c5d",
                "fullName": "Budi Santoso"
            },
            "details": [
                {
                    "product": {
                        "id": "product-uuid-1",
                        "name": "Laptop Pro 14 inch"
                    },
                    "quantity": 1,
                    "pricePerUnit": 25000000.00,
                    "lineItemNetAmount": 25000000.00,
                    "lineItemTaxAmount": 2750000.00,
                    "lineItemTotalAmount": 27750000.00
                },
                // ... detail item lainnya
            ]
        },
        "pagination": null,
        "timestamp": "2025-07-22T11:00:05.12345"
    }
    ```

---

#### `GET /api/v1/transactions`

Mendapatkan daftar transaksi dengan filter dinamis. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Query Params (Semua opsional):**
    -   `page`, `size`, `sort` (contoh: `sort=transactionTime,desc`)
    -   `startDate` (Format ISO: `2025-01-01T00:00:00`)
    -   `endDate` (Format ISO: `2025-12-31T23:59:59`)
    -   `customerName` (contoh: `Cipta`)
    -   `statuses` (bisa >1, contoh: `statuses=PAID&statuses=NOT_PAID`)
    -   `paymentMethod` (contoh: `CREDIT`)
    -   `staffId` (UUID dari staf yang membuat transaksi)
-   **Success Response (200 OK):**
    Respons akan berisi daftar transaksi yang dipaginasi, dengan struktur yang sama seperti di atas.

---
---

#### `GET /api/v1/reports/customer-spending`

Menghitung total uang yang dihabiskan oleh seorang pelanggan. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Query Params:**
    -   `customerId` (UUID, **Wajib**)
    -   `startDate` (Opsional)
    -   `endDate` (Opsional)
-   **Success Response (200 OK):**
    ```json
    {
        "message": "Customer spending report retrieved successfully.",
        "code": 200,
        "data": {
            "total": 55500000.00
        },
        "pagination": null,
        "timestamp": "2025-07-22T11:05:00.33333"
    }
    ```

---

#### `GET /api/v1/reports/spending-per-tax`

Menghitung total uang yang dihabiskan per jenis pajak. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Success Response (200 OK):**
    ```json
    {
        "message": "Spending per tax report retrieved successfully.",
        "code": 200,
        "data": [
            {
                "entityName": "PPN",
                "totalAmount": 5500000.00
            },
            {
                "entityName": "Pajak Impor",
                "totalAmount": 1250000.00
            }
        ],
        "pagination": null,
        "timestamp": "2025-07-22T11:10:00.44444"
    }
    ```

---

#### `GET /api/v1/reports/spending-per-product`

Menghitung total uang yang dihabiskan per produk. Dapat diakses oleh **ADMIN** dan **STAFF**.

-   **Otorisasi:** `Bearer Token` dengan `ROLE_ADMIN` atau `ROLE_STAFF`.
-   **Success Response (200 OK):**
    ```json
    {
        "message": "Spending per product report retrieved successfully.",
        "code": 200,
        "data": [
            {
                "entityName": "Laptop Pro 14 inch",
                "totalAmount": 27750000.00
            },
            {
                "entityName": "Mouse Wireless",
                "totalAmount": 750000.00
            }
        ],
        "pagination": null,
        "timestamp": "2025-07-22T11:15:00.55555"
    }
    ```