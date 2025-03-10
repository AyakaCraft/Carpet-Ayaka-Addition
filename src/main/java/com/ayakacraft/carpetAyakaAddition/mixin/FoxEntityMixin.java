package com.ayakacraft.carpetayakaaddition.mixin;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoxEntity.class)
public class FoxEntityMixin {

    @Inject(method = "canPickupItem", at = @At("RETURN"), cancellable = true)
    public void onPickupItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && CarpetAyakaSettings.disableFoxPickup) {
            cir.setReturnValue(false);
        }
    }

}
