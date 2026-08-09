# Âm thanh

HomeGUI phát hai âm thanh, và chỉ phát cho đúng người chơi kích hoạt chúng. Người đứng gần không
nghe thấy tiếng đếm ngược của người khác.

## Tuỳ chọn

| Key | Mặc định | Phát khi nào |
|---|---|---|
| `warmupTickSound` | `minecraft:block.note_block.hat` | Mỗi giây trong lúc đếm ngược |
| `warmupTickSoundVolume` | `0.6` | |
| `warmupTickSoundPitch` | `1.4` | |
| `teleportSound` | `minecraft:entity.enderman.teleport` | Khi tới nơi |
| `teleportSoundVolume` | `0.7` | |
| `teleportSoundPitch` | `1.0` | |

Âm lượng từ `0` tới `4`. Trên `1` thì tiếng không to hơn mà chỉ vang xa hơn. Cao độ từ `0.5` tới
`2`; giá trị ngoài khoảng này sẽ bị kéo về trong khoảng.

## Tắt một âm thanh

Đặt id thành chuỗi rỗng:

```json
{
  "warmupTickSound": "",
  "teleportSound": "minecraft:entity.enderman.teleport"
}
```

Đếm ngược vẫn chạy và vẫn hiện trên action bar, chỉ là không có tiếng.

## Chọn âm thanh khác

Mọi id âm thanh game biết đều dùng được, kể cả từ resource pack. Vài gợi ý hợp với việc này:

| Id | Tính chất |
|---|---|
| `minecraft:block.note_block.hat` | Tiếng tách ngắn, khô |
| `minecraft:block.note_block.pling` | Sáng hơn, dễ chú ý hơn |
| `minecraft:ui.button.click` | Rất trung tính |
| `minecraft:entity.enderman.teleport` | Tiếng dịch chuyển kinh điển |
| `minecraft:block.beacon.activate` | Dài và trang trọng |
| `minecraft:entity.player.levelup` | Vui tai nhưng nghe mãi sẽ chán |

Gõ sai id cũng không sao. Id lạ sẽ được ghi vào log một lần rồi bỏ qua, nên đếm ngược chỉ đơn giản
là im tiếng.
