# Final Proyek Pemrograman Berorientasi Obyek 2
<ul>
  <li>Mata Kuliah: Pemrograman Berorientasi Obyek 2</li>
  <li>Dosen Pengampu: <a href="https://github.com/Muhammad-Ikhwan-Fathulloh">Muhammad Ikhwan Fathulloh</a></li>
</ul>

## Profil
<ul>
  <li>Nama: Syaiful Fathur Rozaq, Muhammad Rifki Fahrosy, Farhandhika Nurrohman</li>
  <li>NIM: 23552011282, 23552011334, 23552011203</li>
  <li>Studi Kasus: Masak Cuyy</li>
</ul>

## Judul Studi Kasus
<p>Masak Cuy (Resep Masakan Web System)</p>

## Penjelasan Studi Kasus
<p>Sebuah aplikasi resep masakan modern berbasis Java, dibangun menggunakan Spring Boot, Thymeleaf, dan Spring Security. Mendukung autentikasi pengguna, tambah resep, dan tampilan modern dengan Bootstrap 5.</p>

## Penjelasan 4 Pilar OOP dalam Studi Kasus

### 1. Inheritance (Pewarisan)
<p>Inheritance diterapkan secara ekstensif dalam penggunaan Spring Data JPA Repositories. Antarmuka (interface) seperti <code>UserRepository</code>, <code>RecipeRepository</code>, dan <code>FavoriteRepository</code> mewarisi fungsionalitas dasar CRUD (Create, Read, Update, Delete) dari antarmuka <code>JpaRepository</code> yang disediakan oleh Spring Data JPA. Contohnya, <code>public interface UserRepository extends JpaRepository&lt;User, Long&gt;</code> menunjukkan bahwa <code>UserRepository</code> secara otomatis mendapatkan metode-metode seperti <code>save()</code>, <code>findById()</code>, dan <code>findAll()</code> tanpa perlu implementasi manual. Ini secara signifikan mengurangi boilerplate code dan mempromosikan reusable code.</p>

### 2. Encapsulation (Enkapsulasi)
<p>Encapsulation terlihat jelas pada kelas-kelas model (entitas) seperti <code>User.java</code>, <code>Recipe.java</code>, dan <code>Favorite.java</code>. Semua atribut (properti) dalam kelas-kelas ini, misalnya <code>id</code>, <code>username</code>, <code>email</code>, dan <code>password</code> di kelas <code>User</code>, dideklarasikan sebagai <code>private</code>. Akses ke atribut-atribut ini dikontrol ketat melalui metode <code>public</code> getter (misalnya, <code>getUsername()</code>) dan setter (misalnya, <code>setPassword()</code>). Pendekatan ini melindungi integritas data internal objek dari akses atau modifikasi langsung yang tidak terkontrol, memastikan bahwa perubahan data hanya dapat dilakukan melalui antarmuka yang didefinisikan dengan baik.</p>

### 3. Polymorphism (Polimorfisme)
<p>Polymorphism diwujudkan dalam beberapa aspek proyek. Salah satu contoh utamanya adalah implementasi antarmuka <code>UserDetailsService</code> oleh kelas <code>CustomUserDetailsService</code>. Metode <code>loadUserByUsername()</code> di-override (ditimpa) untuk menyediakan logika otentikasi kustom yang mengambil detail pengguna dari <code>UserRepository</code>. Ketika Spring Security membutuhkan detail pengguna, ia memanggil metode ini melalui referensi <code>UserDetailsService</code> (tipe antarmuka), tetapi pada saat runtime, implementasi spesifik dari <code>CustomUserDetailsService</code>-lah yang dieksekusi. Selain itu, penggunaan generics pada <code>JpaRepository&lt;T, ID&gt;</code> juga menunjukkan polymorphism parametrik, di mana satu antarmuka (<code>JpaRepository</code>) dapat beroperasi pada berbagai tipe entitas (misalnya <code>User</code> atau <code>Recipe</code>) secara generik.</p>

### 4. Abstraction (Abstraksi)
<p>Abstraksi diterapkan pada berbagai lapisan proyek. Pada lapisan data, antarmuka Repository (<code>UserRepository</code>, <code>RecipeRepository</code>, <code>FavoriteRepository</code>) menyediakan abstraksi dari detail implementasi database yang kompleks. Anda cukup memanggil metode seperti <code>save()</code> atau <code>findByUsername()</code> tanpa perlu tahu bagaimana query SQL yang sebenarnya dibangun atau bagaimana koneksi database dikelola. Pada lapisan layanan (service layer), kelas <code>RecipeService</code> dan <code>FavoriteService</code> mengabstraksi logika bisnis. Controller berinteraksi dengan service layer (misalnya, <code>recipeService.saveRecipe()</code>) tanpa perlu mengetahui detail internal bagaimana operasi data atau validasi dilakukan, memisahkan perhatian dan membuat kode lebih mudah dikelola.</p>

## Demo Proyek
<ul>
  <li>Github: <a href="https://github.com/syafaro1011/UAS_PBO2_TIF-K-23A_23552011203-23552011334-23552011282">Github</a></li>
  <li>Youtube: <a href="https://youtu.be/Ukhlj2lu4Ag">Youtube</a></li>
</ul>
