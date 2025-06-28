# Hashed Passport
## Yêu cầu:

* Dữ liệu mã hóa trước khi lưu vào database (có thể mã hóa nhiều trường, dữ liệu nhạy cảm).
*  Khi cần xem thì giải mã để trả về FrontEnd


## Thuật toán mã hóa đối xứng (Symmetric Encryption)
* __Ưu điểm:__ Nhanh, phù hợp mã hóa/giải mã nhiều dữ liệu lớn.
* __Nhược điểm:__ Chìa khóa mã hóa và giải mã giống nhau, cần bảo vệ tốt key này.

### Ví dụ thuật toán phổ biến:
* AES (Advanced Encryption Standard), thường dùng AES-256
* ChaCha20 (nhanh, an toàn, ít tốn tài nguyên)

### Áp dụng:
Server có một secret key (ví dụ lưu trong environment variable hoặc vault)
<br>
Trước khi lưu dữ liệu nhạy cảm, server mã hóa với key đó rồi lưu vào DB
<br>
Khi truy vấn dữ liệu, server lấy ra, giải mã với cùng key, trả về FrontEnd

### Lưu ý
* Key phải được giữ bí mật, không hardcode như ví dụ trên, bạn có thể dùng biến môi trường hoặc secret manager.
* Bạn phải lưu đồng thời 3 trường encryptedData, iv, authTag trong DB.
* Không nên truyền trực tiếp key cho Frontend, chỉ gửi dữ liệu đã giải mã.


####  1. Không nên dùng chung *iv*
* IV phải là duy nhất cho mỗi lần mã hóa.<br>
* Nếu bạn dùng cùng một IV nhiều lần với cùng key, mã hóa AES-GCM sẽ mất an toàn, có thể bị tấn công làm lộ dữ liệu hoặc làm giả dữ liệu.
* IV thường được tạo ngẫu nhiên mỗi lần mã hóa (ví dụ crypto.randomBytes(12) cho GCM) và lưu cùng bản ghi dữ liệu (cùng hàng với dữ liệu mã hóa).

#### 2. authTag là gì?
* authTag là mã xác thực (authentication tag) do AES-GCM sinh ra sau khi mã hóa, giúp phát hiện dữ liệu bị thay đổi.
* authTag là phần của dữ liệu mã hóa, mỗi lần mã hóa dữ liệu khác nhau thì authTag cũng khác.
* Vì vậy, authTag không thể dùng chung cho nhiều bản ghi, phải lưu riêng theo từng bản ghi.

