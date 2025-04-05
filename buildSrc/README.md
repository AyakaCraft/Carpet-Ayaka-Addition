# CurseGradle

Forked from https://github.com/matthewprenger/CurseGradle
Developing by myself as the original project is publicly archived

[![Build Status](https://travis-ci.org/matthewprenger/CurseGradle.svg?branch=master)](https://travis-ci.org/matthewprenger/CurseGradle)

A gradle plugin for publishing artifacts to [CurseForge](http://minecraft.curseforge.com/).

* IRC: `#matthewprenger` on [EsperNet](http://esper.net/)
* [Wiki](https://github.com/matthewprenger/CurseGradle/wiki/)
* [Changelog](https://github.com/matthewprenger/CurseGradle/releases)

## Simple Quickstart with ForgeGradle

If you're using ForgeGradle, which you probably are, the following script is a bare-minimum. For more details about customization, check out the [Wiki](https://github.com/matthewprenger/CurseGradle/wiki).

To find out which versions are available, check [HERE](https://plugins.gradle.org/plugin/com.matthewprenger.cursegradle).

```gradle
plugins {
    id 'net.minecraftforge.gradle.forge' version '2.0.2'
    id 'com.matthewprenger.cursegradle' version '<VERSION>'
}

curseforge {
  apiKey = '123-456' // This should really be in a gradle.properties file
  project {
    id = '12345'
    changelog = 'Changes' // A file can also be set using: changelog = file('changelog.txt')
    releaseType = 'beta'
  }
}
```
