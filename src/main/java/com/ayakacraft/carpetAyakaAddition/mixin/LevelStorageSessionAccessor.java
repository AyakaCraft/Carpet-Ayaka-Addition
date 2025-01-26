package com.ayakacraft.carpetAyakaAddition.mixin;

import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelStorage.Session.class)
public interface LevelStorageSessionAccessor {

    @Accessor
    LevelStorage.LevelSave getDirectory();

}
