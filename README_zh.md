# Carpet Ayaka Addition

[简体中文](README_zh.md) ｜ English

## 综述

一个为 Ayakacraft 服务器设计的 [fabric-carpet](https://github.com/gnembon/fabric-carpet/) 扩展

## 功能

### 命令

- /killitem
    - 一键清除掉落物
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
