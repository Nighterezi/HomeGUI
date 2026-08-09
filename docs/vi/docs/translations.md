# Bản dịch

HomeGUI có sẵn tiếng Anh và tiếng Việt. Mỗi người chơi thấy mod bằng đúng ngôn ngữ game của mình
mà không ai phải cấu hình gì.

## Ngôn ngữ được chọn thế nào

Có hai cơ chế chạy song song.

**Màn hình** hỏi client, và client tự tra chữ từ resource pack của nó. Đây là cách vanilla vẫn
làm, và nó đi theo mục chọn ngôn ngữ trong phần cài đặt game.

**Tin nhắn chat** lại được dịch ở server, dùng ngôn ngữ mà client báo lên lúc kết nối. Nhờ vậy
người chơi trên client vanilla, vốn không hề có file ngôn ngữ nào, vẫn nhận được tin nhắn bằng
ngôn ngữ của họ.

Ngôn ngữ mod không có sẵn sẽ lùi về hợp lý: `en_gb` nhận tiếng Anh, và các mã lạ khác cũng nhận
tiếng Anh.

## Các file

```
src/main/resources/assets/homegui/lang/
  en_us.json
  vi_vn.json
```

`en_us.json` là bản gốc để đối chiếu. Key thiếu ở file khác sẽ lùi về bản tiếng Anh, nên bản dịch
làm dở vẫn chạy được chứ không hiện ra key thô.

## Thêm một ngôn ngữ

Bỏ thêm một file vào thư mục đó, đặt tên theo mã ngôn ngữ, ví dụ `de_de.json`, rồi chép nội dung
`en_us.json` vào trước khi dịch. Không cần sửa gì thêm: mod quét thư mục lúc khởi động và nhận
mọi file có trong đó.

`/homegui reload` đọc lại các file, nên bạn sửa và kiểm tra được mà không cần khởi động lại.

## Màu trong tin nhắn

Tin nhắn chat dùng mã `&`, cộng thêm `&#RRGGBB` cho màu bất kỳ:

```json
{
  "homegui.message.home_deleted": "Đã xoá home &a%s&r.",
  "homegui.message.home_not_found": "&#FF4444Bạn không có home nào tên &f%s&#FF4444."
}
```

Chữ trong màn hình thì không chứa mã màu. Màu ở đó do mod quyết định, nên người dịch chỉ cần lo
phần chữ nghĩa.

## Placeholder

Giá trị được chèn vào từng vị trí `%s`, theo đúng thứ tự xuất hiện. Hãy giữ đúng số lượng và đúng
thứ tự như bản tiếng Anh, nếu không tin nhắn sẽ ra sai.

Chỉ dùng `%s`. Các định dạng khác như `%d` không được hỗ trợ.
