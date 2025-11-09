# PBO-Thread-and-Database
**Nama:** [M. Wahyu Hilal Abroor]  
**NIM:** [F1D02310123]

---
## **Sistem Pesanan Cafe (Multithreading + MySQL Database)**
Program ini merupakan implementasi sederhana konsep **multithreading** dan **database MySQL** menggunakan bahasa pemrograman **Java**.  
Program ini menampilkan simulasi sistem pesanan café di mana:
- Thread pertama bertugas menambahkan pesanan pelanggan ke database.
- Thread kedua bertugas menampilkan pesanan yang tersimpan secara periodik.
---

## **Fitur Utama**
1. **Koneksi ke Database MySQL**
   - Menggunakan `JDBC` dengan konektor `mysql-connector-j.jar`.
   - Otomatis membuat koneksi ke database `cafe_db`.

2. **Implementasi Multithreading**
   - `AddOrderThread` --> Menyimpan pesanan ke database.
   - `ShowOrdersThread` --> Menampilkan daftar pesanan setiap beberapa detik.

3. **Konsep Paralelisme**
   - Thread berjalan secara bersamaan (*parallel execution*).
   - Mengilustrasikan bagaimana dua proses dapat berjalan bersamaan tanpa saling menunggu.

---

## **Struktur Folder**
```
PBO-THREAD-AND-DATABASE/
│
├── .vscode
├── bin
├── lib/
│ └── mysql-connector-j-9.5.0.jar
│
└── src/
    ├── App.java --> kelas utama
    ├── DBConnection.java --> kelas pengghubung program dengan database
    ├── AddOrderThread.java --> Thread untuk menambahkan pesanan ke tabel orders
    └── ShowOrdersThread.java --> Thread untuk menampilkan data pesanan secara periodik
```

## Database
```sql
CREATE DATABASE cafe_db;
USE cafe_db;

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(50),
    menu_item VARCHAR(50),
    quantity INT,
    status VARCHAR(20)
);
```
## Penjelasan Program
### 1. DBConnection.java

Pertama, program membuat koneksi ke database menggunakan JDBC (Java Database Connectivity).
Kelas `DBConnection` memiliki method statis `getConnection()` yang akan:
- Memuat driver `com.mysql.cj.jdbc.Driver`.
- Menghubungkan ke database `cafe_db` di `localhost`.
- Mengembalikan objek `Connection` agar bisa digunakan oleh thread lain.

Jika koneksi gagal, program akan menampilkan pesan error seperti:

```
Koneksi gagal: com.mysql.cj.jdbc.Driver
```

Tujuannya agar setiap thread bisa menggunakan koneksi ini untuk melakukan operasi ke MySQL.


### 2. AddOrderThread.java

Kelas ini merupakan Thread pertama, berfungsi untuk menyimpan data pesanan baru ke dalam tabel `orders`. Setiap objek `AddOrderThread` memiliki atribut:

- `customer` --> nama pelanggan,
- `item` --> menu yang dipesan,
- `qty` --> jumlah pesanan.

Ketika thread dijalankan, langkah-langkah yang terjadi:
1. Membuka koneksi ke database melalui DBConnection.getConnection().
2. Menjalankan perintah SQL INSERT INTO orders (...) VALUES (...).
3. Menampilkan pesan konfirmasi di terminal seperti:
  ```
  [Thread 1] Pesanan disimpan dalam Database:
      ---> Hilal - Kopi Hitam
  ```
4. Setelah menyimpan data, thread berhenti setelah Thread.sleep(1000) (1 detik jeda).

Karena dalam program utama terdapat 3 thread penambah pesanan (t1, t2, t3), maka akanada tiga data baru yang dimasukkan ke database secara paralel.

### 3. ShowOrdersThread.java

Kelas ini adalah Thread kedua yang bertugas menampilkan isi tabel orders setiap 1 detik sebanyak 5 kali. Langkah-langkahnya:

1. Membuka koneksi ke database.

2. Melakukan query SELECT * FROM orders.

3. Menampilkan hasilnya ke terminal dengan format:
  ```
  [Thread 2] Daftar Pesanan Café:
  - Hilal memesan Kopi Hitam x2 (Pending)
  - Ijat memesan Teh Tarik x1 (Pending)
  - Abdi memesan Cappuccino x3 (Pending)
  -------------------------------------
  ```
4. Melakukan jeda 1 detik (Thread.sleep(1000)) sebelum mengulang kembali.

5. Setelah menampilkan hasil sebanyak 5 kali, thread menutup koneksi dan menampilkan pesan:
  ```
  [Thread 2] Pemantauan Database selesai
  ```
Dengan demikian, thread ini berperan seperti layar monitoring untuk memantau pesanan yang baru masuk ke sistem.

### 4. App.java

Kelas App menjadi pengendali utama seluruh thread. Di dalamnya terdapat:

- Tiga thread AddOrderThread untuk pelanggan berbeda (Hilal, Ijat, Abdi).

- Satu thread ShowOrdersThread untuk menampilkan hasil monitoring database.

Kode utamanya:
```
AddOrderThread t1 = new AddOrderThread("Hilal", "Kopi Hitam", 2);
AddOrderThread t2 = new AddOrderThread("Ijat", "Teh Tarik", 1);
AddOrderThread t3 = new AddOrderThread("Abdi", "Cappuccino", 3);
ShowOrdersThread monitor = new ShowOrdersThread();

t1.start();
t2.start();
t3.start();
monitor.start();
```
Keempat thread ini berjalan bersamaan (concurrent), sehingga urutan tampil di terminal tidak selalu sama.
Kadang monitoring (Thread 2) tampil duluan sebelum pesanan selesai disimpan, karena setiap thread dijalankan paralel oleh Thread Scheduler milik sistem operasi.

## Output dan Database
1. Screenshot Output:

  <img width="417" height="754" alt="image" src="https://github.com/user-attachments/assets/f161f9b0-3897-4346-8901-217ea70dd581" />
  
2. Database

  <img width="829" height="193" alt="image" src="https://github.com/user-attachments/assets/f869fadd-9969-4d55-81a7-2e337ba8590f" />


## Kesimpulan
Program ini menunjukkan bahwa Java mampu menjalankan beberapa proses secara bersamaan menggunakan konsep multithreading, di mana setiap thread memiliki tugasnya masing-masing seperti menambah dan memantau data pesanan dalam database. Melalui koneksi JDBC, data yang diolah dapat tersimpan secara permanen di MySQL. Hasil akhirnya menggambarkan bagaimana sistem pemesanan sederhana dapat berjalan secara paralel dan dinamis layaknya aplikasi nyata.

## Note
Dikarenakan program ini membutuhkan library eksternal untuk menjalankan Program ini bisa dengan perintah `java -cp ".;../lib/mysql-connector-j-9.5.0.jar" App`
