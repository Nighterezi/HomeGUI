# Cài đặt

## Tải về

Lấy bản mới nhất trên Modrinth:

[Tải HomeGUI trên Modrinth](https://modrinth.com/mod/homeguimod)

Chọn file đúng với phiên bản Minecraft của bạn và tải `homegui-<phiên bản>.jar`. Không có trình
cài đặt nào cả, file jar chính là toàn bộ mod.

## Yêu cầu

| Thành phần | Phiên bản |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.19.3 trở lên |
| Fabric API | bắt buộc |
| Java | 25 |

## Đặt mod ở đâu

Bỏ file `homegui-<phiên bản>.jar` vào thư mục `mods` của **server**, hoặc của game nếu bạn chơi
một mình. Đó là nơi home được lưu và nơi việc dịch chuyển diễn ra, nên phía server mới là phía
quan trọng.

Cài thêm ở client là tuỳ chọn nhưng nên làm. Có mod ở client thì `/home` mở màn hình. Không có
thì `/home` vẫn chạy và in danh sách ra chat.

## Mod Menu

Nếu bạn cài Mod Menu, HomeGUI sẽ có nút Config trong danh sách Mods để chỉnh cấu hình ngay trong
game. Đây hoàn toàn là tuỳ chọn, không cài mod vẫn chạy tốt.

![HomeGUI trong danh sách Mod Menu, nút Config nằm ở góc trên bên phải](/screenshots/mod-menu.png)

## Lần chạy đầu tiên

Khởi động game một lần. HomeGUI sẽ tạo `config/homegui.json` với giá trị mặc định, và tạo thư mục
`homegui` trong thư mục world ngay khi có người lưu home đầu tiên.
