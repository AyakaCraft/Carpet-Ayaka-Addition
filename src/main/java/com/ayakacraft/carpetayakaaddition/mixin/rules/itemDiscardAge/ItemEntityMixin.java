package com.ayakacraft.carpetayakaaddition.mixin.rules.itemDiscardAge;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow
    private int itemAge;

    @Unique
    private int getDiscardAge() {
        return CarpetAyakaSettings.itemDiscardAge == 0 ? 6000 : CarpetAyakaSettings.itemDiscardAge;
    }

    @Inject(method = "setDespawnImmediately", at = @At("TAIL"))
    private void setDespawnImmediately_mix(CallbackInfo ci) {
        itemAge = getDiscardAge() - 1;
    }

    @ModifyConstant(method = {"tick", "canMerge()Z"}, constant = @Constant(intValue = 6000))
    private int injected(int value) {
        return getDiscardAge();
    }

}
