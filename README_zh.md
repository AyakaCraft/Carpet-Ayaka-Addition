# Carpet Ayaka Addition

[![许可证](https://img.shields.io/static/v1?label=License&message=gpl-v3.0&color=red&logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth 下载量](https://img.shields.io/modrinth/dt/carpet-ayaka-addition?label=Modrinth%20Downloads&logo=modrinth)](https://modrinth.com/mod/carpet-ayaka-addition)
[![GitHub 下载量](https://img.shields.io/github/downloads/AyakaCraft/Carpet-Ayaka-Addition/total?label=Github%20Downloads&logo=github)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases)
[![CurseForge 下载量](https://img.shields.io/curseforge/dt/1220026?label=CurseForge%20Downloads&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)
[![MC 版本](https://cf.way2muchnoise.eu/versions/MC%20Versions_carpet-ayaka-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)
[![Build & Publish](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/actions/workflows/release.yml/badge.svg)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/actions/workflows/release.yml)
[![Release](https://img.shields.io/github/v/release/AyakaCraft/Carpet-Ayaka-Addition?label=Release&include_prereleases)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases)

简体中文 ｜ [English](README.md)

Copyright (c) 2025 Calboot and contributors

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

一个为 Ayakacraft 服务器设计的 [fabric-carpet](https://github.com/gnembon/fabric-carpet) 扩展

大部分基于 [Fallen Breath 的模板](https://github.com/Fallen-Breath/fabric-mod-template)

## 链接

- [Github](https://github.com/AyakaCraft/Carpet-Ayaka-Addition)
- [Discord](https://discord.com/channels/1360172405485469796/1360256724392476774)
- [Modrinth](https://modrinth.com/mod/carpet-ayaka-addition)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)

## 功能

### 命令

- /address | /ad
    - *推荐五个最常用的路径点*
    - reload
        - *重新加载路径点*
    - list
        - *列出所有路径点*
        - dim \<dim>
            - *列出指定维度中的路径点*
        - radius \<radius>
            - *列出制定**区块**半径内的路径点*
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
    - xaero \<id>
        - *分享为 Xaero 路径点*
- /c
    - *将你的游戏模式在 生存模式 和 旁观者模式 之间切换*
- /gohome
    - *将你传送回出生点/重生点*
- /killitem
    - *一键清除掉落物*
    - cancel
        - *取消计划的清除掉落物任务*
- /tpt \<player>
    - *将你传送至服务器内其他玩家*

### Carpet 记录器

#### 加载区块数 (loadedChunks)

显示选择的维度加载的区块数

- 类型: HUD
- 选项: `dynamic`, `all`, `overworld`, `the_nether`, `the_end`
- 默认: `dynamic`

#### 移动方块数 (movingBlocks)

在方块移动结束时进行记录

- 类型: 控制台
- 选项: `full`, `brief`
- 默认: `full`

#### poi

记录兴趣点的变更 (实验性)

- 类型: 控制台
- 选项:
    - 不适用 (1.18.2-)
    - `all`, `village`, `bee_home`, `acquirable_job_site` (1.19.4+)
- 默认
    - 不适用 (1.18.2-)
    - `all` (1.19.4+)

### 规则

#### 索引

- [bedrockNoBlastResistance](#基岩无爆炸抗性-bedrocknoblastresistance)
- [betterMobCap](#更好的刷怪上限-bettermobcap)
- [betterOpPlayerNoCheat](#更好的op玩家不准作弊-betteropplayernocheat)
- [blockDropStackVelocityMultiple](#方块掉落物速度乘数-blockdropstackvelocitymultiple)
- [commandAddress](#全局路径点命令开关-commandaddress)
- [commandC](#旁观者模式切换命令开关-commandc)
- [commandGoHome](#回程命令开关-commandgohome)
- [commandKillItem](#清除掉落物命令开关-commandkillitem)
- [commandTpt](#传送到玩家命令开关-commandtpt)
- [disableBatSpawning](#禁用蝙蝠生成-disablebatspawning)
- [dragonEggFallDelay](#龙蛋下落延迟-dragoneggfalldelay)
- [fakePlayerForceOffline](#假人强制离线-fakeplayerforceoffline)
- [fakePlayerResidentBackupFix](#备份不保留假人修复-fakeplayerresidentbackupfix)
- [forceTickPlantsReintroduce](#0t强制更新植物状态-forcetickplantsreintroduce-116)
- [foxNoPickupItem](#禁用狐狸叼起物品-foxnopickupitem)
- [frostWalkerNoFreezing](#冰霜行者无冰冻-frostwalkernofreezing)
- [giveLimit](#give命令限制-givelimit-117)
- [itemDiscardAge](#掉落物消失刻-itemdiscardage)
- [killItemAwaitSeconds](#清除掉落物执行延迟-killitemawaitseconds)
- [legacyHoneyBlockSliding](#老式蜂蜜块滑落-legacyhoneyblocksliding-1214)
- [maxPlayersOverwrite](#最大玩家数量重写-maxplayersoverwrite)
- [tickFluids](#流体更新-tickfluids)

#### 基岩无爆炸抗性 (bedrockNoBlastResistance)

使得基岩无法阻挡爆炸，尽管其自身不会被破坏

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `CREATIVE`, `TNT`

#### 更好的刷怪上限 (betterMobCap)

使灾厄巡逻队和幻翼的生成受刷怪上限影响

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`, `EXPERIMENTAL`

#### 更好的op玩家不准作弊 (betterOpPlayerNoCheat)

为 opPlayerNoCheat 添加更多影响的指令

影响的指令列表：`/kill`，`/clear`，`/effect`，`/item` 和 `/difficulty`

仅在加载了 [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition) 且 `opPlayerNoCheat` 为 `true` 时生效

~~你也不想失去刷铁机里面的掠夺者，对吧~~

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `SURVIVAL`, `CHEAT`

#### 方块掉落物速度乘数 (blockDropStackVelocityMultiple)

方块被破坏时生成的掉落物的速度的乘数

- 类型: `double`
- 默认值: `1d`
- 参考选项: `0`, `0.5`, `1`, `2`
- 范围: `[0,)`
- 分类: `AYAKA`

#### 全局路径点命令开关 (commandAddress)

启用 `/address` 和 `/ad` 命令以操作并传送到服务器全局路径点

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 旁观者模式切换命令开关 (commandC)

启用 `/c` 命令以在 生存模式 和 旁观者模式 中切换

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 回程命令开关 (commandGoHome)

启用 `/gohome` 命令以传送到重生点

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 清除掉落物命令开关 (commandKillItem)

启用 `/killitem` 命令以清除加载范围内的掉落物

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AYAKA`, `COMMAND`, `CREATIVE`

#### 传送到玩家命令开关 (commandTpt)

启用 `/tpt` 命令以传送到其他玩家

- 类型: `String`
- 默认值: `false`
- 参考选项: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- 分类: `AYAKA`, `COMMAND`, `CHEAT`

#### 禁用蝙蝠生成 (disableBatSpawning)

禁用蝙蝠的自然生成

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`

#### 龙蛋下落延迟 (dragonEggFallDelay)

龙蛋下落前延迟的游戏刻数

设置为2以匹配沙子等其他重力方块，设置为0或5以使用原版值

- 类型: `int`
- 默认值: `5`
- 参考选项: `0`, `2`, `5`
- 范围: `[0,)`
- 分类: `AYAKA`

#### 假人强制离线 (fakePlayerForceOffline)

强制假人以离线模式生成

(1.16+) 仅在 allowSpawningOfflinePlayers 为 true 时生效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `EXPERIMENTAL`, `BOT`

#### 备份不保留假人修复 (fakePlayerResidentBackupFix)

修复回档后假人不会重新加入世界的问题

仅在加载了 [GCA](https://github.com/Gu-ZT/gugle-carpet-addition) 且 `fakePlayerResident` 为 `true` 时生效

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `EXPERIMENTAL`, `BUGFIX`, `BOT`

#### 0t强制更新植物状态 (forceTickPlantsReintroduce) (1.16+)

重新引入 1.15.2 及更早版本中仙人掌、竹子、紫颂花、甘蔗在计划刻中（错误地）触发随机刻的特性

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`, `REINTRODUCE`

#### 禁用狐狸叼起物品 (foxNoPickupItem)

阻止狐狸叼起掉落物，尽管它仍会被吸引

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`

#### 冰霜行者无冰冻 (frostWalkerNoFreezing)

阻止附魔冰霜行者的靴子冻住水源

在 1.21+ 中可能导致意料外的行为

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `FEATURE`, `EXPERIMENTAL`

#### give命令限制 (giveLimit) (1.17+)

give 命令的限制乘数

实际限制值 = 值 * 最大堆叠数

- 类型: `int`
- 默认值: `5`
- 参考选项: `0`, `1`, `10`, `100`, `1000`
- 范围: `[0,)`
- 分类: `AYAKA`, `COMMAND`, `CREATIVE`

#### 掉落物消失刻 (itemDiscardAge)

修改掉落物自然消失前的游戏刻数

设置为 `0` 或 `6000` 以使用原版值

最大值为 `72000` (一小时)

- 类型: `int`
- 默认值: `0`
- 参考选项: `0`, `3000`, `3600`, `6000`, `12000`, `72000`
- 范围: `[0,72000]`
- 分类: `AYAKA`, `CREATIVE`

#### 清除掉落物执行延迟 (killItemAwaitSeconds)

清理掉落物之前的等待秒数

- 类型: `int`
- 默认值: `5`
- 参考选项: `0`, `5`, `10`, `30`
- 范围: `[0,)`
- 分类: `AYAKA`, `COMMAND`, `CREATIVE`

#### 老式蜂蜜块滑落 (legacyHoneyBlockSliding) (1.21.4+)

将非生物实体在蜂蜜块上滑落时的速度计算替换为 1.21.1 及更低版本中的方法

参见 [MC-278572](https://bugs.mojang.com/browse/MC/issues/MC-278572) 和 [MC-275537](https://bugs.mojang.com/browse/MC/issues/MC-275537)

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `BUGFIX`, `EXPERIMENTAL`, `REINTRODUCE`

#### 最大玩家数量重写 (maxPlayersOverwrite)

重写服务器最大玩家数量

设置为0以使用原版值

- 类型: `int`
- 默认值: `0`
- 参考选项: `0`, `8`, `10`, `20`, `50`, `100`
- 范围: `[0,)`
- 分类: `AYAKA`

#### 流体更新 (tickFluids)

流体是否更新

影响流体计划刻和随机刻

- 类型: `boolean`
- 默认值: `false`
- 参考选项: `false`, `true`
- 分类: `AYAKA`, `CREATIVE`, `EXPERIMENTAL`

## 生命周期计划

### 当前支持的版本

目前，以下 Minecraft 版本正在积极地获得新功能和错误修复的支持

| Minecraft 版本     | **支持至**                 |
|------------------|-------------------------|
| 1.14.4           | ✔️ 长期支持                 |
| 1.15.2           | ✔️ 长期支持                 |
| 1.16.5           | ✔️ 长期支持                 |
| 1.17.1           | ✔️ 长期支持                 |
| 1.18.2           | ✔️ 长期支持                 |
| 1.19.4           | ✔️ 长期支持                 |
| 1.20.1           | ✔️ 长期支持                 |
| 1.20.6           | ✔️ 长期支持                 |
| 1.21.1, 1.21.4-7 | 🕒 Minecraft 1.23 快照发布时 |
| 1.21.8           | 🕒 待定                   |

### 已结束支持的版本

以下 Minecraft 版本已不在支持范围内。 除非出现了严重的漏洞，否则这些 Minecraft 版本将不再获得支持

| Minecraft 版本 | **最后的版本**                                                                         | **发布日期**       |
|--------------|-----------------------------------------------------------------------------------|----------------|
| 1.19.2       | [v0.3.1](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases/tag/v0.3.1) | 2025 年 3 月 2 日 |

### 计划支持的版本

以下 Minecraft 版本计划在未来的版本中得到支持

| Minecraft 版本 | 支持时间 |
|--------------|------|

## 使用的开源库

- Minecraft使用的库
- [Fabric Loader](https://github.com/FabricMC/fabric-loader)、[fabric-loom](https://github.com/FabricMC/fabric-loom) 和 [yarn映射](https://github.com/FabricMC/yarn)
- [preprocessor](https://github.com/ReplayMod/preprocessor) (或 [Fallen的版本](https://github.com/Fallen-Breath/preprocessor))
- [shadow](https://github.com/GradleUp/shadow)、[license-gradle](https://github.com/hierynomus/license-gradle-plugin)、[modpublisher](https://github.com/firstdarkdev/modpublisher)
- [conditional-mixin](https://github.com/Fallen-Breath/conditional-mixin)
- [fabric-carpet](https://github.com/gnembon/fabric-carpet)
- [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition) 和 [GCA](https://github.com/Gu-ZT/gugle-carpet-addition)
