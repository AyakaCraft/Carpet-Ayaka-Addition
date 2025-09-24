# Carpet Ayaka Addition

[![License](https://img.shields.io/static/v1?label=License&message=gpl-v3.0&color=red&logo=gnu)](https://www.gnu.org/licenses/gpl-3.0.txt)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/carpet-ayaka-addition?label=Modrinth%20Downloads&logo=modrinth)](https://modrinth.com/mod/carpet-ayaka-addition)
[![GitHub Downloads](https://img.shields.io/github/downloads/AyakaCraft/Carpet-Ayaka-Addition/total?label=Github%20Downloads&logo=github)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1220026?label=CurseForge%20Downloads&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)
[![MC Versions](https://cf.way2muchnoise.eu/versions/MC%20Versions_carpet-ayaka-addition_all.svg)](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)
[![Build & Publish](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/actions/workflows/release.yml/badge.svg)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/actions/workflows/release.yml)
[![Release](https://img.shields.io/github/v/release/AyakaCraft/Carpet-Ayaka-Addition?label=Release&include_prereleases)](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases)

[简体中文](README_zh.md) ｜ English

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

## Conclusion

A [fabric-carpet](https://github.com/gnembon/fabric-carpet/) extension designed for AyakaCraft server.

Mostly based on [Fallen-Breath's template](https://github.com/Fallen-Breath/fabric-mod-template)

## Links

- [Github](https://github.com/AyakaCraft/Carpet-Ayaka-Addition)
- [Discord](https://discord.com/channels/1360172405485469796/1360256724392476774)
- [Modrinth](https://modrinth.com/mod/carpet-ayaka-addition)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/carpet-ayaka-addition)

## Functions

### Commands

- /address | /ad
    - *Suggests five most-used server-side waypoints*
    - reload
        - *Reloads the waypoints*
    - list
        - *Lists the waypoints*
        - dim \<dim>
            - *Lists the waypoints in a specific dimension*
        - radius \<radius>
            - *Lists the waypoints in a certain radius of **chunks***
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
    - xaero \<id>
        - *Share as Xaero waypoint*
- /c
    - *Switches your gamemode between `spectator` and `survival`*
- /gohome
    - *Teleport you right back to your spawn point*
- /killitem
    - *Clears dropped items with one shot*
    - cancel
        - *Cancels the scheduled kill-item tasks*
- /tpt \<player>
    - *Teleports you to another player in your server*

### Carpet Loggers

#### loadedChunks

Shows the count of loaded chunks for selected dimension

- Type: HUD
- Options: `dynamic`, `all`, `overworld`, `the_nether`, `the_end`
- Default: `dynamic`

#### movingBlocks

Logs block movements at their ends

- Type: Console
- Options: `full`, `brief`
- Default: `full`

#### poi

Logs the change of points-of-interest (experimental)

- Type: Console
- Options:
    - N/A for 1.18.2-
    - `all`, `village`, `bee_home`, `acquirable_job_site` for 1.19.4+
- Default
    - N/A for 1.18.2-
    - `all` for 1.19.4+

### Rules

#### Index

- [bedrockNoBlastResistance](#bedrocknoblastresistance)
- [betterMobCap](#bettermobcap)
- [betterOpPlayerNoCheat](#betteropplayernocheat)
- [blockDropStackVelocityMultiple](#blockdropstackvelocitymultiple)
- [commandAddress](#commandaddress)
- [commandC](#commandc)
- [commandGoHome](#commandgohome)
- [commandKillItem](#commandkillitem)
- [commandTpt](#commandtpt)
- [disableBatSpawning](#disablebatspawning)
- [dragonEggFallDelay](#dragoneggfalldelay)
- [fakePlayerForceOffline](#fakeplayerforceoffline)
- [fakePlayerResidentBackupFix](#fakeplayerresidentbackupfix)
- [forceTickPlantsReintroduce](#forcetickplantsreintroduce-116)
- [foxNoPickupItem](#foxnopickupitem)
- [frostWalkerNoFreezing](#frostwalkernofreezing)
- [giveLimit](#givelimit-117)
- [itemDiscardAge](#itemdiscardage)
- [killItemAwaitSeconds](#killitemawaitseconds)
- [legacyHoneyBlockSliding](#legacyhoneyblocksliding-1214)
- [maxPlayersOverwrite](#maxplayersoverwrite)
- [tickFluids](#tickfluids)

#### bedrockNoBlastResistance

Makes bedrocks unable to block explosions, though themselves won't be destroyed

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `CREATIVE`, `TNT`

#### betterMobCap

Makes mob cap effect pillager patrol and phantom spawning

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `FEATURE`, `EXPERIMENTAL`

#### betterOpPlayerNoCheat

More commands for opPlayerNoCheat

Influenced commands: `/kill`, `/clear`, `/effect`, `/item` and `/difficulty`

Only active when [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition) is loaded and `opPlayerNoCheat` is set to `true`

~~You don't want to lose your pillagers, do you?~~

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `SURVIVAL`, `CHEAT`

#### blockDropStackVelocityMultiple

Multiple of velocity of item stacks dropped by a broken block

- Type: `double`
- Default value: `1d`
- Suggested options: `0`, `0.5`, `1`, `2`
- Range: `[0,)`
- Categories: `AYAKA`

#### commandAddress

Enables `/address` and `/ad` to manipulate and teleport to shared waypoints

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AYAKA`, `COMMAND`, `CHEAT`

#### commandC

Enables `/c` to switch your gamemode between `spectator` and `survival`

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AYAKA`, `COMMAND`, `CHEAT`

#### commandGoHome

Enables `/gohome` to teleport right back to your spawn point

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AYAKA`, `COMMAND`, `CHEAT`

#### commandKillItem

Enables `/killitem` to clear dropped items with one shot

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AYAKA`, `COMMAND`, `CREATIVE`

#### commandTpt

Enables `/tpt` to teleport to another player in your server

- Type: `String`
- Default value: `false`
- Suggested options: `false`, `true`, `ops`, `0`, `1`, `2`, `3`, `4`
- Categories: `AYAKA`, `COMMAND`, `CHEAT`

#### disableBatSpawning

Disables natual spawning of bats

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `FEATURE`

#### dragonEggFallDelay

Delay ticks before dragon egg falls

Set to 2 to match sand and other falling blocks, 0 or 5 to use vanilla option

- Type: `int`
- Default value: `5`
- Suggested options: `0`, `2`, `5`
- Range: `[0,)`
- Categories: `AYAKA`

#### fakePlayerForceOffline

Forces fake players to spawn in offline mode

(1.16+) Only active when allowSpawningOfflinePlayers is set to true

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `EXPERIMENTAL`, `BOT`

#### fakePlayerResidentBackupFix

Fixes the bug that fake players are not reconnected after retracements

Only active when [GCA](https://github.com/Gu-ZT/gugle-carpet-addition) is loaded and `fakePlayerResident` is set to `true`

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `EXPERIMENTAL`, `BUGFIX`, `BOT`

#### forceTickPlantsReintroduce (1.16+)

Reintroduces the feature of cactuses, bamboos, chorus flowers and sugarcane being (wrongly) random-ticked on scheduled ticks in Minecraft 1.15.2 and lower

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `FEATURE`, `REINTRODUCE`

#### foxNoPickupItem

Stops foxes from picking up dropped items, though they will still be attracted

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `FEATURE`

#### frostWalkerNoFreezing

Prevents frost walker shoes from freezing water

Might cause unexpected behaviour in 1.21+

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `FEATURE`, `EXPERIMENTAL`

#### giveLimit (1.17+)

Limit factor for give command

limit = value * max_count_per_stack

- Type: `int`
- Default value: `5`
- Suggested options: `0`, `1`, `10`, `100`, `1000`
- Range: `[0,)`
- Categories: `AYAKA`, `COMMAND`, `CREATIVE`

#### itemDiscardAge

Modifies the ticks before an item entity is discarded

Set to `0` (or `6000`) to use vanilla option

Max value `72000` (an hour)

- Type: `int`
- Default value: `0`
- Suggested options: `0`, `3000`, `3600`, `6000`, `12000`, `72000`
- Range: `[0,72000]`
- Categories: `AYAKA`, `CREATIVE`

#### killItemAwaitSeconds

Seconds to wait before clearing the items

- Type: `int`
- Default value: `5`
- Suggested options: `0`, `5`, `10`, `30`
- Range: `[0,)`
- Categories: `AYAKA`, `COMMAND`, `CREATIVE`

#### legacyHoneyBlockSliding (1.21.4+)

Changes the way sliding velocity of non-living entities is calculated back to the original way in 1.21.1 and below

See [MC-278572](https://bugs.mojang.com/browse/MC/issues/MC-278572) and [MC-275537](https://bugs.mojang.com/browse/MC/issues/MC-275537)

- Type: `boolean`
- Default value: `false`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `BUGFIX`, `EXPERIMENTAL`, `REINTRODUCE`

#### maxPlayersOverwrite

Overwrites the max player count in a server

Set to 0 to use vanilla value

Disabled for dedicated servers in 1.21.9+

- Type: `int`
- Default value: `0`
- Suggested options: `0`, `8`, `10`, `20`, `50`, `100`
- Range: `[0,)`
- Categories: `AYAKA`

#### tickFluids

Whether fluids are ticked

Influences both scheduled tick and random tick

- Type: `boolean`
- Default value: `true`
- Suggested options: `false`, `true`
- Categories: `AYAKA`, `CREATIVE`, `EXPERIMENTAL`

## EOL

### Currently supported versions

Currently, the following Minecraft versions are actively supported with new features and bug fixes

| Minecraft Version | Support Until                               |
|-------------------|---------------------------------------------|
| 1.14.4            | ✔️ Long Term Support                        |
| 1.15.2            | ✔️ Long Term Support                        |
| 1.16.5            | ✔️ Long Term Support                        |
| 1.17.1            | ✔️ Long Term Support                        |
| 1.18.2            | ✔️ Long Term Support                        |
| 1.19.4            | ✔️ Long Term Support                        |
| 1.20.1            | ✔️ Long Term Support                        |
| 1.20.6            | ✔️ Long Term Support                        |
| 1.21.1, 1.21.4-8  | 🕒 When Minecraft 1.23 snapshot is released |
| 1.21.9            | 🕒 To be determined                         |

### End-of-life versions

The following Minecraft versions are out of the support range. There's no support for these Minecraft versions, unless some critical bugs occur

| Minecraft Version | Last Version                                                                      | Release Date |
|-------------------|-----------------------------------------------------------------------------------|--------------|
| 1.19.2            | [v0.3.1](https://github.com/AyakaCraft/Carpet-Ayaka-Addition/releases/tag/v0.3.1) | Mar 2, 2025  |

### Scheduled to be supported versions

The following Minecraft versions are scheduled to be supported in the later versions

| Minecraft Version | Support Starts |
|-------------------|----------------|

## Open-source libs

- Dependencies of Minecraft
- [Fabric Loader](https://github.com/FabricMC/fabric-loader), [fabric-loom](https://github.com/FabricMC/fabric-loom) and [yarn mappings](https://github.com/FabricMC/yarn)
- [preprocessor](https://github.com/ReplayMod/preprocessor)(or [Fallen's version](https://github.com/Fallen-Breath/preprocessor))
- [shadow](https://github.com/GradleUp/shadow), [license-gradle](https://github.com/hierynomus/license-gradle-plugin), [modpublisher](https://github.com/firstdarkdev/modpublisher)
- [conditional-mixin](https://github.com/Fallen-Breath/conditional-mixin)
- [fabric-carpet](https://github.com/gnembon/fabric-carpet)
- [Carpet TIS Addition](https://github.com/TISUnion/Carpet-TIS-Addition) and [GCA](https://github.com/Gu-ZT/gugle-carpet-addition)
