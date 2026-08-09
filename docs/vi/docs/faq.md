# Câu hỏi thường gặp

## Người chơi có bắt buộc phải cài mod không?

Không. Mọi thứ đều dùng được qua lệnh chat trên client vanilla. Mod ở client chỉ thêm màn hình.

## Home có đặt được ở Nether hay End không?

Được, và mặc định là cho phép đi xuyên thế giới. Đặt `allowCrossDimension` thành `false` nếu bạn
muốn giữ người chơi trong thế giới họ đang đứng.

## Home ở một thế giới không còn tồn tại thì sao?

Home vẫn nằm trong file, và khi dùng sẽ báo là thế giới đã biến mất. Dùng `/delhome` để xoá nếu
bạn không cần nữa.

## OP có được nhiều home hơn không?

Mặc định là có: `opBypassLimits` cho phép người ở mức `opPermissionLevel` trở lên bỏ qua cả giới
hạn số lượng lẫn thời gian chờ. Tắt nó đi nếu bạn muốn mọi người như nhau.

## Home có dùng chung giữa các thế giới không?

Không. Home nằm trong thư mục world, nên mỗi bản save có bộ home riêng. Xem
[Lưu trữ](/vi/docs/storage).

## Tôi sửa config mà không thấy gì thay đổi

Chạy `/homegui reload`. Lệnh này đọc lại config và các file ngôn ngữ mà không cần khởi động lại.

## Tên hiện ra là `&aBase` chứ không đổi màu

Mã màu chỉ được áp dụng khi `allowColorsInHomeNames` đang bật. Khi tắt, mã màu bị lược bỏ lúc lưu
home chứ không được hiển thị.
