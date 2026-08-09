# Mở màn hình

Có ba cách. Cả ba đều hỏi server lấy danh sách rồi mở cùng một màn hình, nên chúng hoạt động y
hệt nhau.

| Cách | Cần gì |
|---|---|
| `/home` không kèm tên | Không cần gì. Client nào cũng chạy. |
| Phím **H** | Mod ở client |
| Nút cạnh túi đồ | Mod ở client |

Phím và nút không làm gì trên server không cài HomeGUI. Trong trường hợp đó nút không được vẽ ra
luôn, chứ không phải hiện ra rồi bấm không ăn.

## Phím tắt

Mặc định là **H**. Đổi phím trong **Options > Controls > Miscellaneous > Mở danh sách nhà**,
giống mọi phím khác.

Không có tuỳ chọn nào trong config cho phím này. Muốn tắt thì xoá phím ngay tại màn hình đó, vì
đó cũng là nơi bạn sẽ tìm khi cần đổi phím.

## Nút trong túi đồ

Mặc định tắt. Bật bằng `showInventoryButton`, khi đó một nút hình ngôi nhà nhỏ hiện ra bên phải
khung túi đồ lúc bạn mở nó. Nút bám theo khung, nên mở sổ công thức cũng không làm nó lạc chỗ.

![Nút hình ngôi nhà cạnh túi đồ sinh tồn, kèm chú thích](/screenshots/inventory-button.png)

Thay đổi có hiệu lực ngay lần mở túi đồ kế tiếp, không cần khởi động lại.

Chỉ túi đồ chế độ sinh tồn mới có nút này. Menu creative thì không.

## Tuỳ chọn nào nằm ở đâu

`showInventoryButton` mô tả **giao diện của chính bạn**, nên nó được đọc từ file
`config/homegui.json` của bạn. Server cũng giữ nó trong bản của mình, nhưng ở đó nó không có tác
dụng gì. Riêng phím tắt thì không nằm trong file config; Minecraft lưu nó cùng các phím khác.

Mọi thứ còn lại, kể cả số home bạn được phép có, đều lấy từ server.
