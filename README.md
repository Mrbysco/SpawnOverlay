[![build](https://github.com/Mrbysco/SpawnOverlay/actions/workflows/build.yml/badge.svg)](https://github.com/Mrbysco/SpawnOverlay/actions/workflows/build.yml) 
[![](http://cf.way2muchnoise.eu/versions/1660163.svg)](https://www.curseforge.com/minecraft/mc-mods/spawn-overlay)

# Spawn Overlay #

## About ##
**Spawn Overlay** is a mod that adds multiple types of overlays to visualize where mobs can spawn.
There's base types of overlays in the mod, spawn overlays and optimizer overlays.

### Spawning overlay:
There are currently 4 different types of spawning overlays available to show when toggling the Spawn Overlay with **F7** (Default keybind):
* **Always Spawning** - Shows spaces where mobs can spawn regardless of daytime
* **Night Spawning** - Shows spaces where mobs can spawn only during nighttime
* **Phantom Spawning** - Shows spaces where phantoms can spawn above
* **Prevent Spawning** - Shows spaces where spawning is obstructed by light
By default it's set to **Night Spawning**, to change it open the in-game config's `Toggles` section.
The mod hides spawning positions in peaceful biomes by default (like Mushroom Fields) but can be disabled in the config.
You can also change the type of overlay rendering in the config, switching between **OUTLINE**, **INNER_SQUARE** and **FULL**.

### Optimizer Overlay:
There are currently 2 types of optimizer overlays:
* **Spider** - Shows the "optimal" slab positioning to prevent regular Spiders from spawning
* **Ghast** - Shows the "optimal" slab positioning to prevent Ghasts from spawning


### Config
The following options are available in the config:
* **Poll Interval** - The amount of milliseconds between updating the spawning overlay
* **Chunk Radius** - The radius of chunks around the players to display the spawning overlay
* **Overlay Type** - The type of overlay rendering (Available are: **OUTLINE**, **INNER_SQUARE** and **FULL**)
* **Ignore Peaceful Biomes** - Ignore peaceful biomes when displaying the overlay
* **Toggles:**
  * **Show Always Spawning** - Display the spots mobs can spawn regardless of daytime
  * **Show Night Spawning** - Display the spots mobs can spawn only during nighttime
  * **Show Phantom Spawning** - Display the spots phantoms can spawn above
  * **Show Prevent Spawning** - Display the spots where spawning is obstructed by light
* **Color:** (The colors are Hex values, 6 characters for the hex color and the last 2 for the opacity. For example: <code style="color:#FF000080">#FF000080</code> (red with 50% opacity))
  * **Always Spawning Color** - The color used by the `always spawning` overlay
  * **Night Spawning Color** - The color used by the `night spawning` overlay
  * **Phantom Spawning Color** - The color used by the `phantom spawning` overlay
  * **Prevent Spawning Color** - The color used by the `prevent spawning` overlay
  * **Optimizer Color** - The color used to show the fake slabs for the optimizer overlay
* **Optimizer Type** - The type of mob to optimize slab positioning for (Available are: **SPIDER** and **GHAST**)

### Keybinds
The mod uses the following default keybinds (Editable in-game)
* **Toggle Render** - **F7** - Displays the Spawning overlay
* **Toggle Optimizer** - **F8** - Displays the Optimizer overlay
* **Toggle Structure Mode** - **F9** - Toggles Structure Only mode, which makes the Spawning Overlay only show locations within nearby structures¹

**¹ Structure Mode requires the mod to be installed server-side! Clients normally know nothing about structures**

## License ##
* Spawn Overlay is licensed under the MIT License
  - (c) 2026 Mrbysco, ShyNieke
  - [![License](https://img.shields.io/badge/License-MIT-red.svg?style=flat)](http://opensource.org/licenses/MIT)

## Downloads ##
Downloads will be located on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/spawn-overlay)
