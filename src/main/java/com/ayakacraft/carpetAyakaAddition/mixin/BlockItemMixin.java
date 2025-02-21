package com.ayakacraft.carpetAyakaAddition.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ayakacraft.carpetAyakaAddition.stats.Statistics.USED_ALL;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(at = @At("RETURN"), method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;")
    public void onBlockItemPlaced(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted()) {
            PlayerEntity player = context.getPlayer();
            if (player != null && !(player instanceof EntityPlayerMPFake)) {
                player.increaseStat(USED_ALL, 1);
            }
        }
    }

}
