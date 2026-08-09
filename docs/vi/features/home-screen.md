# Màn hình home

`/home` không kèm tên sẽ mở nó, `/homes` cũng vậy. Nếu client của bạn có cài mod thì còn bấm được phím **H** hoặc nút hình ngôi nhà cạnh túi đồ; xem
[Mở màn hình](/vi/docs/opening-the-screen).

## Mỗi home một dòng

| Nút | Tác dụng |
|---|---|
| Nút rộng | Dịch chuyển tới đó và đóng màn hình |
| Icon cây viết | Bắt đầu đổi tên |
| `X` | Xoá, sau cú bấm thứ hai |

Nút rộng chỉ hiện tên, nên tên dài không bị bóp lại. Rê chuột vào để xem home nằm ở thế giới nào
và toạ độ bao nhiêu.

## Đổi tên

Bấm cây viết. Tên hiện tại được nạp vào ô bên dưới, icon sáng lên, và nút **Đặt nhà** đổi thành
**Đổi tên**. Sửa tên, bấm Đổi tên, xong.

Bấm cây viết lần nữa là huỷ. Đóng màn hình cũng vậy.

Đổi tên giữ nguyên home ở đúng chỗ, cả trong thế giới lẫn trong danh sách. Đổi trùng tên với một
home khác sẽ bị từ chối chứ không âm thầm ghi đè.

## Xoá

Cú bấm đầu tiên vào `X` sẽ kích hoạt và chuyển nút sang màu đỏ, kèm một dòng bên dưới cho biết
home nào sắp bị xoá. Bấm lần thứ hai là xoá. Bấm chỗ khác hoặc lật trang sẽ huỷ.

## Thêm home

Nhập tên vào ô bên dưới rồi bấm **Đặt nhà**. Nó lưu tại chỗ bạn đang đứng, giống hệt `/sethome`.

Để trống ô nhập thì dùng tên mặc định trong config.

## Phân trang

`<` và `>` xuất hiện khi bạn có nhiều home hơn một trang chứa được. `guiEntriesPerPage` quyết định
con số đó.

Khi chỉ có một trang, màn hình co lại vừa đúng số home bạn có thay vì để trống các dòng thừa.

## Khi client không cài mod

Màn hình không mở ra, và không có gì hỏng. Server nhận biết được và in danh sách ra chat. Đổi tên
là thứ duy nhất chỉ có ở màn hình.
