package com.ayakacraft.carpetayakaaddition.mixin.rules.fakePlayerResidentBackupFix;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.mods.GcaHelper;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "save", at = @At("RETURN"))
    private void save(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAyakaSettings.fakePlayerResidentBackupFix && ModUtils.isGCALoaded()) {
            GcaHelper.storeFakesIfNeeded((MinecraftServer) (Object) this);
        }
    }

}
