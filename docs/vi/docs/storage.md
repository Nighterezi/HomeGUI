# Lưu trữ

## Home nằm ở đâu

```
<thư mục world>/homegui/<uuid người chơi>.json
```

Mỗi người chơi một file, nằm trong thư mục world. Nghĩa là mỗi thế giới có bộ home riêng, và sao
chép thế giới sẽ mang theo cả home.

Một file trông như sau:

```json
{
  "base": {
    "name": "base",
    "dimension": "minecraft:overworld",
    "x": 82.5,
    "y": 69.0,
    "z": -87.3,
    "yaw": 178.4,
    "pitch": 2.1,
    "createdAt": 1786312400000
  }
}
```

Key là tên viết thường và đã bỏ mã màu, đó là lý do `/home Base` và `/home base` tìm ra cùng một
home.

## Khi nào ghi file

Ngay sau mỗi lần thay đổi. Server sập cũng không mất home đã lưu xong.

File được đọc một lần cho mỗi người chơi và giữ trong bộ nhớ khi họ còn online, rồi giải phóng
khi họ thoát.

## Sao lưu và sửa tay

Cứ copy thư mục `homegui` là xong phần sao lưu. Sửa file bằng tay cũng được, nhưng hãy làm khi
server đã tắt: người chơi đang online có dữ liệu nằm trong bộ nhớ và sẽ ghi đè bản sửa của bạn ở
lần thay đổi kế tiếp.

## Chuyển người chơi giữa các thế giới

Home không đi theo. Hãy copy file `<uuid>.json` sang thư mục `homegui` của thế giới kia khi server
đang tắt.
