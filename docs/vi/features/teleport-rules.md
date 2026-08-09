# Luật dịch chuyển

Mặc định có hai luật: bạn được giữ ba home, và mỗi lần dịch chuyển phải đứng yên ba giây trước.
Những thứ còn lại trong trang này đều phải tự bật.

## Được bao nhiêu home

`maxHomes`, mặc định `3`. Chạm giới hạn không chặn `/sethome` lên một tên bạn đã có, chỉ chặn tên
mới.

OP vượt qua được khi `opBypassLimits` đang bật.

## Thời gian chờ

`teleportCooldownSeconds` là khoảng chờ giữa hai lần dịch chuyển. Tin nhắn cho người chơi biết còn
bao nhiêu giây.

Thời gian chờ bắt đầu tính từ lúc dịch chuyển xảy ra chứ không phải lúc yêu cầu, nên phần đếm
ngược không bị tính vào đó.

## Đếm ngược

`teleportWarmupSeconds`, mặc định ba giây, giữ người chơi đứng yên trước khi dịch chuyển. Đặt `0`
để dịch chuyển ngay khi gõ lệnh. Số giây còn lại hiện trên
action bar và giảm mỗi giây, kèm một tiếng động mỗi lần.

![Đồng hồ đếm ngược trên action bar trong lúc chờ dịch chuyển](/screenshots/teleport-warmup.png)

Khi `cancelWarmupOnMove` bật, đi khỏi chỗ là huỷ. `warmupMoveTolerance` quyết định bao xa thì bị
tính, tính bằng block; mặc định `0.5` đủ để bỏ qua mấy cái xê dịch nhỏ khi bạn đứng trên slab hoặc
bị mob đẩy.

Xoay người không huỷ. Chỉ di chuyển mới huỷ.

## Thế giới

`allowCrossDimension`, mặc định bật, cho phép đi lại giữa Overworld, Nether, End và mọi thế giới
do mod thêm vào.

Tắt nó đi không làm ẩn những home đó. Người chơi vẫn thấy chúng trong danh sách; chỉ là khi họ ở
nơi khác thì việc dịch chuyển bị từ chối kèm thông báo.

Home thuộc một thế giới không còn tồn tại, vì datapack hay mod đã bị gỡ, vẫn được giữ lại và sẽ
báo là thế giới đã biến mất.

## OP

`opBypassLimits` bỏ qua giới hạn số home, thời gian chờ và đếm ngược cùng lúc. Riêng luật về thế
giới thì áp dụng cho tất cả.
