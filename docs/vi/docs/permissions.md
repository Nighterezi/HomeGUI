# Quyền

HomeGUI không thêm node quyền nào. Mọi thứ người chơi cần đều mở cho tất cả, và lệnh duy nhất bị
giới hạn là `/homegui reload`.

## Ai được tính là OP

Hai key trong config quyết định điều này:

| Key | Mặc định | Tác dụng |
|---|---|---|
| `opPermissionLevel` | `2` | Permission level được coi là OP |
| `opBypassLimits` | `true` | OP có bỏ qua giới hạn số home và thời gian chờ hay không |

Permission level là của vanilla, từ 0 tới 4. Mức 2 là mức `/op` cấp mặc định và là mức đa số lệnh
quản trị yêu cầu.

```json
{
  "opPermissionLevel": 2,
  "opBypassLimits": true
}
```

Nếu muốn OP vẫn chịu chung giới hạn mà vẫn reload được config, cứ để nguyên `opPermissionLevel`
và đặt `opBypassLimits` thành `false`.

## Dùng plugin phân quyền

Không cần đấu nối gì cả. Nếu hệ thống của bạn quản lý permission level của Minecraft, chỉ cần cấp
đúng mức là HomeGUI sẽ coi người đó là OP.
