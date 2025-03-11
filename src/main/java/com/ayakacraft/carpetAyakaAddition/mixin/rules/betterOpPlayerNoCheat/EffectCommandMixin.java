package com.ayakacraft.carpetayakaaddition.mixin.rules.betterOpPlayerNoCheat;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.mods.TISHelper;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectCommand.class)
public class EffectCommandMixin {

    @Inject(method = "method_13235", remap = false, at = @At("RETURN"), cancellable = true)
    private static void checkIfAllowCheating_effectCommand(ServerCommandSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && CarpetAyakaSettings.betterOpPlayerNoCheat && ModUtils.isTISLoaded() && TISHelper.cannotCheat(source)) { // DO NOT change the order of the conditions
            cir.setReturnValue(false);
        }
    }

}
