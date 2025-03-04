package com.ayakacraft.carpetayakaaddition.mixin;

//#if MC<11900
//$$ import java.nio.file.Path;
//#endif

import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelStorage.Session.class)
public interface LevelStorageSessionAccessor {

    //#if MC>=11900
    @Accessor
    LevelStorage.LevelSave getDirectory();
    //#else
    //$$ @Accessor
    //$$ Path getDirectory();
    //#endif

}
