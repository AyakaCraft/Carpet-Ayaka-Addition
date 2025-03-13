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

## Conclusion

A [fabric-carpet](https://github.com/gnembon/fabric-carpet/) extension designed for AyakaCraft server.

## Functions

### Commands

- /killitem
    - *Clears dropped items with one shot*
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
    - set \<id> \<dim> \<pos>
        - *Adds a new waypoint or modify an existing one*
    - remove \<id>
        - *Removes the specific waypoint*
    - detail \<id>
        - *Shows the detail of the specific waypoint*
    - tp \<id>
        - *Teleports you to the specific waypoint*

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
- Command rules