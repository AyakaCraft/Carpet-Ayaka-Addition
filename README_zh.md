# Carpet Ayaka Addition

[![License](https://img.shields.io/static/v1?label=license&message=gpl-v3.0&color=red)](http://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth](https://img.shields.io/modrinth/dt/carpet-ayaka-addition?label=Modrinth%20Downloads)](https://modrinth.com/mod/carpet-ayaka-addition)
[![CurseForge](https://cf.way2muchnoise.eu/full_carpet-ayaka-addition_CurseForge%20Downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)
[![GitHub downloads](https://img.shields.io/github/downloads/AyakaCraft/Carpet-Ayaka-Addition/total?label=Github%20downloads&logo=github)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases)
[![MC Versions](https://cf.way2muchnoise.eu/versions/MC%20Version_carpet-ayaka-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)

[简体中文](README_zh.md) ｜ English

Copyright (c) 2025

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>

## 综述

一个为 Ayakacraft 服务器设计的 [fabric-carpet](https://github.com/gnembon/fabric-carpet/) 扩展

## EOL

### 当前支持的版本

目前，以下 Minecraft 版本正在积极地获得新功能和错误修复的支持

| Minecraft 版本 | **支持至**                 |
|--------------|-------------------------|
| 1.14.4       | ✔️ 长期支持                 |
| 1.15.2       | ✔️ 长期支持                 |
| 1.16.5       | ✔️ 长期支持                 |
| 1.17.1       | ✔️ 长期支持                 |
| 1.18.2       | ✔️ 长期支持                 |
| 1.19.4       | ✔️ 长期支持                 |
| 1.20.1       | ✔️ 长期支持                 |
| 1.20.6       | ✔️ 长期支持                 |
| 1.21.1       | 🕒 Minecraft 1.22 发布时   |
| 1.21.4       | 🕒 Minecraft 1.23 快照发布时 |

### 已结束支持的版本

以下 Minecraft 版本已不在支持范围内。 除非出现了严重的漏洞，否则这些 Minecraft 版本将不再获得支持

| Minecraft 版本 | **最后的版本**                                                                         | **发布日期**       |
|--------------|-----------------------------------------------------------------------------------|----------------|
| 1.19.2       | [v0.3.1](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases/tag/v0.3.1) | 2025 年 3 月 2 日 |

### 计划支持的版本

以下 Minecraft 版本计划在未来的版本中得到支持

| Minecraft 版本 | 支持时间                    |
|--------------|-------------------------|
| 1.21.5       | 🕒 Minecraft 1.21.6 发布时 |

## 功能

### 命令

- /killitem
    - *一键清除掉落物*
    - cancel
        - *取消计划的清除掉落物任务*
- /gohome
    - *将你传送回出生点/重生点*
- /tpt \<player>
    - *将你传送至服务器内其他玩家*
- /c
    - *将你的游戏模式切换回生存模式/到旁观者模式*
- /waypoint
    - *服务端的路径点*
    - reload
        - *重新加载路径点*
    - list
        - *列出所有路径点*
        - \<dim>
            - *列出指定维度中的路径点*
    - set \<id> \<dim> \<pos> \<desc (optional)>
        - *添加一个新的路径点或者修改已有的路径点*
    - remove \<id>
        - *移除指定路径点*
    - detail \<id>
        - *显示指定路径点的详细信息*
    - tp \<id>
        - *将你传送到路径点*
    - rename \<oldId> \<id>
        - *重命名路径点并覆盖已有的*
    - desc \<id> \<desc>
        - *设置或修改路径点的描述*

### Carpet 记录器

#### 加载区块数 (loadedChunks)
显示选择的维度加载的区块数 (实验性)
- 类型: HUD 记录器
- 选项: `dynamic`, `all`, `overworld`, `the_nether`, `the_end`
- 默认: `dynamic`

#### 移动方块数 (movingBlocks)
显示“移动的方块”的数量 (实验性)
- 类型: HUD 记录器
- 选项: 不适用
- 默认: 不适用

### 规则

#### 更好的opPlayerNoCheat选项 (betterOpPlayerNoCheat)
阻止管理员玩家使用`/kill`，`/clear`，`/effect`，`/item` 和 `/difficulty` 指令

仅在加载了 [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition) 且 `opPlayerNoCheat` 为 `true` 时生效
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `SURVIVAL`, `CHEAT`

#### 旁观者模式切换命令开关 (commandC)
启用 `/c` 命令以在 生存模式 和 旁观者模式 中切换
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 回程命令开关 (commandGoHome)
启用 `/gohome` 命令以传送到重生点
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 清除掉落物命令开关 (commandKillItem)
启用 `/killitem` 命令以清除加载范围内的掉落物
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `COMMAND`

#### 传送到玩家命令开关 (commandTpt)
启用 `/tpt` 命令以传送到其他玩家
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 全局路径点命令开关 (commandWaypoint)
启用 `/waypoint` 命令以操作并传送到服务器全局路径点
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 禁用蝙蝠生成 (disableBatSpawning)
禁用蝙蝠的自然生成
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`

#### 备份不保留假人修复 (fakePlayerResidentBackupFix)
修复回档后假人不会重新加入世界的问题

仅在加载了 [GCA](https://github.com/Gu-ZT/gugle-carpet-addition) 且 `fakePlayerResident` 为 `true` 时生效
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `EXPERIMENTAL`, `BUGFIX`

#### 0t强制更新植物状态 (forceTickPlantsReintroduce)
重新引入 1.15.2 及更早版本中仙人掌、竹子、甘蔗在计划刻中（错误地）触发随机刻的特性
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`

#### 禁用狐狸叼起物品 (foxNoPickupItem)
阻止狐狸叼起掉落物，但仍会吸引它
- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`

#### 掉落物消失刻 (itemDiscardAge)
修改掉落物自然消失前的游戏刻数

设置为 `0` 或 `6000` 以使用原版值

最大值为 `72000` (一小时)
- 类型: `int`
- 默认值: `6000`
- 参考选项: `0`, `3000`, `3600`, `6000`, `12000`, `72000`
- 范围: `[0,72000]`
- 分类: `AYAKA`, `EXPERIMENTAL`

#### 清除掉落物执行延迟 (killItemAwaitSeconds)
清理掉落物之前的等待秒数
- 类型: `int`
- 默认值: `5`
- 参考选项: `0`, `5`, `10`, `30`
- 范围: `[0,)`
- 分类: `AYAKA`, `COMMAND`
