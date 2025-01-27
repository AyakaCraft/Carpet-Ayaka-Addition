package com.ayakacraft.carpetAyakaAddition.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
//
//import static com.ayakacraft.carpetAyakaAddition.stats.Statistics.USED_ALL;

public class UseBlockHandler implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
//        ItemPlacementContext itemPlacementContext = new ItemPlacementContext(world, player, hand, player.getStackInHand(hand), hitResult);
//        if(!player.isSpectator() && !player.getStackInHand(hand).isEmpty() && itemPlacementContext.canPlace()) {
//            player.increaseStat(USED_ALL, 1);
//        }
        return ActionResult.PASS;
    }

}
