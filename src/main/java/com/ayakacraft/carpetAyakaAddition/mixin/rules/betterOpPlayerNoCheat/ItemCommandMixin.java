package com.ayakacraft.carpetayakaaddition.mixin.rules.betterOpPlayerNoCheat;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.mods.TISHelper;
//#if MC>=11700
import net.minecraft.server.command.ItemCommand;
//#else
//$$ import net.minecraft.server.command.ReplaceItemCommand;
//#endif
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC>=11700
@Mixin(ItemCommand.class)
//#else
//$$ @Mixin(ReplaceItemCommand.class)
//#endif
public class ItemCommandMixin {

    @Inject(
            //#if MC>=11700
            method = "method_32710",
            //#else
            //$$ method = "method_13545",
            //#endif
            remap = false, at = @At("RETURN"), cancellable = true)
    private static void checkIfAllowCheating_itemCommand(ServerCommandSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && CarpetAyakaSettings.betterOpPlayerNoCheat && ModUtils.isTISLoaded() && TISHelper.cannotCheat(source)) { // DO NOT change the order of the conditions
            cir.setReturnValue(false);
        }
    }

}
