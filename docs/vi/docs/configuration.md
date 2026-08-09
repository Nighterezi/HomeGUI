---
outline: [2, 3]
---

# Cấu hình chính

`config/homegui.json` được tạo trong lần chạy đầu tiên. Sau khi sửa hãy chạy `/homegui reload`,
hoặc dùng **Mods > HomeGUI > Config** nếu bạn có Mod Menu.

::: tip
Màn hình Mod Menu sửa file config **của bạn**. Trên server riêng thì bản của server mới là bản có
hiệu lực, nên sửa ở client sẽ không thay đổi gì với người chơi khác.
:::

![Màn hình cấu hình trong Mod Menu, trang một](/screenshots/config-page-1.png)

![Màn hình cấu hình trong Mod Menu, trang hai](/screenshots/config-page-2.png)

## Home

### maxHomes

Mặc định `3`. Mỗi người chơi được giữ bao nhiêu home.

OP có thể vượt qua mức này khi `opBypassLimits` đang bật.

### defaultHomeName

Mặc định `home`. Tên dùng khi chạy lệnh không kèm tên, nên `/sethome` không tham số sẽ lưu một
home tên `home`.

### allowOverwrite

Mặc định `true`. `/sethome` có được ghi đè lên home trùng tên hay không. Khi tắt, người chơi phải
xoá home cũ trước.

### maxHomeNameLength

Mặc định `24`. Đếm theo ký tự hiển thị, nên mã màu không ăn vào hạn mức.

### allowColorsInHomeNames

Mặc định `true`. Xem [Tên home](/vi/features/home-names). Khi tắt, mã màu bị lược bỏ lúc lưu home.

## Dịch chuyển

### teleportCooldownSeconds

Mặc định `0`, tức là tắt. Thời gian người chơi phải chờ giữa hai lần dịch chuyển.

### teleportWarmupSeconds

Mặc định `3`. Đếm ngược trước khi dịch chuyển, hiển thị trên action bar. Đặt `0` để dịch chuyển
ngay lập tức.

### cancelWarmupOnMove

Mặc định `true`. Di chuyển trong lúc đếm ngược có huỷ dịch chuyển hay không.

### warmupMoveTolerance

Mặc định `0.5`. Người chơi được xê dịch bao nhiêu block trước khi bị tính là đã di chuyển. Giá trị
nhỏ thì khắt khe; tăng lên nếu người chơi than bị huỷ dù đang đứng yên.

### allowCrossDimension

Mặc định `true`. Người chơi có được dịch chuyển tới home ở thế giới khác hay không.

## Màn hình

### guiEntriesPerPage

Mặc định `6`, tối đa `10`. Mỗi trang màn hình hiện bao nhiêu home.

### openGuiOnBareHomeCommand

Mặc định `true`. `/home` không kèm tên có mở màn hình hay không. Khi tắt, `/home` luôn đưa về home
mặc định.

## Tin nhắn

### showMessagePrefix

Mặc định `false`. Tin nhắn trong chat có kèm tên mod ở đầu dòng hay không.

Tắt thì chat gọn gàng, thường là điều bạn muốn trên server nơi người chơi gõ `/home` liên tục.
Dòng đếm ngược trên action bar không bao giờ có tiền tố, dù bật hay tắt, vì chỗ đó quá hẹp.

Bản thân tiền tố là một key dịch bình thường, `homegui.message.prefix`, nên bạn đổi được chữ và
màu riêng cho từng ngôn ngữ. Xem [Bản dịch](/vi/docs/translations).

## Giao diện của riêng bạn

### showInventoryButton

Mặc định `false`. Có thêm nút hình ngôi nhà cạnh túi đồ hay không. Mặc định tắt để mod không tự ý
đổi giao diện túi đồ của bạn.

Tuỳ chọn này được đọc từ file config của bạn chứ không phải của server. Xem
[Mở màn hình](/vi/docs/opening-the-screen).

Không có tuỳ chọn nào cho phím mở nhanh, vì nó nằm trong **Options > Controls** cùng với mọi phím
khác. Xoá phím ở đó là tắt.

## OP

### opPermissionLevel

Mặc định `2`. Xem [Quyền](/vi/docs/permissions).

### opBypassLimits

Mặc định `true`. OP có bỏ qua giới hạn số home và thời gian chờ hay không.

## Âm thanh

Có trang riêng: [Âm thanh](/vi/docs/sounds).
