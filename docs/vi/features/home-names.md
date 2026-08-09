# Tên home

## Khoảng trắng

Tên có thể chứa khoảng trắng:

```
/sethome nhà mùa hè
/home nhà mùa hè
```

Không cần dấu ngoặc kép. Tên luôn là phần cuối cùng của dòng lệnh, đó là lý do cách này chạy được.

## Màu sắc

Tên có thể tô màu bằng đúng những mã người chơi đã quen:

| Cú pháp | Ví dụ | Kết quả |
|---|---|---|
| `&` kèm mã | `&anhà mùa hè` | Xanh lá |
| `§` kèm mã | `§cnhà mùa hè` | Đỏ |
| `&#RRGGBB` | `&#55FFAAnhà mùa hè` | Màu bất kỳ |

Mã định dạng cũng dùng được: `&l` in đậm, `&o` in nghiêng, `&r` trở về bình thường.

Hex là hex thật, không phải làm tròn về màu gần nhất trong mười sáu màu vanilla.

## Tên bạn gõ khác với tên bạn thấy

Lúc tra cứu, mod bỏ qua hoàn toàn phần màu. Một home lưu dưới dạng `&#55FFAAbase` vẫn tìm ra bằng:

```
/home base
```

Gợi ý phím Tab cũng hiện tên trần vì lý do đó. Điều này cũng có nghĩa hai home không thể chỉ khác
nhau ở màu; với mod thì chúng là cùng một home.

## Độ dài

`maxHomeNameLength` đếm ký tự hiển thị, nên trang trí tên không bao giờ làm bạn mất chỗ. Giới hạn
24 ký tự nghĩa là 24 chữ cái, bọc bao nhiêu mã màu cũng được.

## Tắt tính năng này

Đặt `allowColorsInHomeNames` thành `false`. Khi đó mã màu bị lược bỏ lúc lưu chứ không bị từ chối,
nên người chơi gõ `&abase` sẽ nhận được một home tên `base`.

## Những gì không được phép

Ngoài tên rỗng và các ký tự không font nào vẽ được thì không còn hạn chế nào khác. Một dấu `§` sót
lại mà không thuộc mã hợp lệ sẽ bị từ chối, nên không ai lách được tuỳ chọn ở trên.
