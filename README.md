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

## Conclusion

A [fabric-carpet](https://github.com/gnembon/fabric-carpet/) extension designed for AyakaCraft server.

## EOL

### Current supported versions

Currently, the following Minecraft versions are actively supported with new features and bug fixes

| Minecraft Version | Support Until                               |
|-------------------|---------------------------------------------|
| 1.16.5            | ✔️ Long Term Support                        |
| 1.17.1            | ✔️ Long Term Support                        |
| 1.18.2            | ✔️ Long Term Support                        |
| 1.19.4            | ✔️ Long Term Support                        |
| 1.20.1            | ✔️ Long Term Support                        |
| 1.20.6            | ✔️ Long Term Support                        |
| 1.21.1            | 🕒 When Minecraft 1.23 snapshot is released |
| 1.21.4            | 🕒 When Minecraft 1.23 snapshot is released |

### End-of-life versions

The following Minecraft versions are out of the support range. There's no support for these Minecraft versions, unless some critical bugs occur

| Minecraft Version | Last Version                                                                      | Release Date |
|-------------------|-----------------------------------------------------------------------------------|--------------|
| 1.19.2            | [v0.3.1](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases/tag/v0.3.1) | Mar 2, 2025  |

### Scheduled to be supported versions

The following Minecraft versions are scheduled to be supported in the later versions

| Minecraft Version | Support When                      |
|-------------------|-----------------------------------|
| 1.14.4            | Any time                          |
| 1.15.2            | Any time                          |
| 1.21.5            | 🕒 When Minecraft 1.21.6 released |

## Functions

### Commands

- /killitem
    - *Clears dropped items with one shot*
    - cancel
        - *Cancels the scheduled kill-item tasks*
- /gohome
    - *Teleport you right back to your spawn point*
- /tpt \<player>
    - *Teleports you to another player in your server*
- /c
    - *Switches your gamemode between spectator and survival*
- /waypoint
    - *Server-side waypoints*
    - reload
        - *Reloads the waypoints*
    - list
        - *Lists the waypoints*
        - \<dim>
            - *Lists the waypoints in specific dimension*
    - set \<id> \<dim> \<pos> \<desc (optional)>
        - *Adds a new waypoint or modify an existing one*
    - remove \<id>
        - *Removes the specific waypoint*
    - detail \<id>
        - *Shows the detail of the specific waypoint*
    - tp \<id>
        - *Teleports you to the specific waypoint*
    - rename \<oldId> \<id>
        - *Renames the waypoint, removes the existing one*
    - desc \<id> \<desc>
        - *Sets the description of the waypoint*

### Rules

- disableBatSpawning
    - Disables natual spawning of bats
- killItemAwaitSeconds
    - Seconds to wait before clearing the items
- foxNoPickupItem
    - Stops foxes from picking up dropped items
- betterOpPlayerNoCheat
    - Stops operators from using /kill, /clear, /effect and /item
    - Only active when [Carpet Tis Addition](https://github.com/TISUnion/Carpet-TIS-Addition) is loaded and opPlayerNoCheat is set to true
- fakePlayerResidentBackupFix
    - Fixes the bug that fake players are not reconnected after retracements
    - Only active when [Gca](https://github.com/Gu-ZT/gugle-carpet-addition) is loaded and fakePlayerResident is set to true
- itemDiscardAge
    - Modifies the ticks before an item entity is discarded
    - Max value 72000 (an hour)
    - Set to 0 to use vanilla option
- forceTickPlantsReintroduce
    - Reintroduces the feature of cactuses, bamboos and sugarcanes being (wrongly) random-ticked on scheduled ticks in Minecraft 1.15.2 and lower
- Command rules are omitted here
