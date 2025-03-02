# Carpet Ayaka Addition

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

## 功能

### 命令

- /killitem
    - 一键清除掉落物
    - killItemAwaitSeconds
        - 清理掉落物之前等待的秒数
- /gohome
    - 将你传送回出生点/重生点
- /tpt \<player>
    - 将你传送至服务器内其他玩家
- /c
    - 将你的游戏模式切换回生存模式/到旁观者模式
- /waypoint
    - 服务端的路径点
    - reload
        - 重新加载路径点
    - list
        - 列出所有路径点
        - \<dim>
            - 列出指定维度中的路径点
    - set \<id> \<dim> \<pos>
        - 添加一个新的路径点或者修改已有的路径点
    - remove \<id>
        - 移除指定路径点
    - detail \<id>
        - 显示指定路径点的详细信息
    - tp \<id>
        - 将你传送到路径点

### 游戏特性

- disableBatSpawning
    - 禁用蝙蝠的自然生成
