---
outline: [2, 3]
---

# Lệnh

Tên home có thể chứa khoảng trắng, nên tên luôn là phần cuối cùng của dòng lệnh. Cứ gõ
`/home nhà mùa hè`, không cần dấu ngoặc kép.

Tên cũng được gợi ý bằng phím Tab, và gợi ý hiện tên không kèm mã màu.

## Lệnh cho người chơi

### /sethome

```
/sethome [tên]
```

Lưu một home tại chỗ bạn đang đứng, giữ nguyên hướng nhìn. Không nhập tên thì dùng
`defaultHomeName` trong config, mặc định là `home`.

Lưu trùng tên sẽ ghi đè, trừ khi bạn tắt `allowOverwrite`.

### /home

```
/home [tên]
```

Có tên thì dịch chuyển thẳng tới đó.

Không có tên thì mở [màn hình home](/vi/features/home-screen). Nếu client của bạn không cài mod,
lệnh sẽ đưa bạn về home mặc định, hoặc in danh sách nếu bạn chưa có home đó.

### /delhome

```
/delhome [tên]
```

Xoá một home. Từ chat không có bước xác nhận; bước xác nhận nằm ở màn hình.

### /homes

```
/homes
```

Mở màn hình, hoặc in danh sách ra chat với client không cài mod.

## Lệnh quản trị

### /homegui reload

```
/homegui reload
```

Đọc lại `config/homegui.json` và các file ngôn ngữ. Không có gì phải khởi động lại, người chơi
vẫn giữ nguyên home và màn hình đang mở.

Cần `opPermissionLevel`, mặc định là permission level 2. Xem [Quyền](/vi/docs/permissions).
