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

Mặc định `0`, tức là tắt. Đếm ngược trước khi dịch chuyển, hiển thị trên action bar.

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

## OP

### opPermissionLevel

Mặc định `2`. Xem [Quyền](/vi/docs/permissions).

### opBypassLimits

Mặc định `true`. OP có bỏ qua giới hạn số home và thời gian chờ hay không.

## Âm thanh

Có trang riêng: [Âm thanh](/vi/docs/sounds).
