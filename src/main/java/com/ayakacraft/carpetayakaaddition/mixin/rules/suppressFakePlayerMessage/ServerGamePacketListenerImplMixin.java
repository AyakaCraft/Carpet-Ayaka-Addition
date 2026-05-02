/*
 * This file is part of the null project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition.mixin.rules.suppressFakePlayerMessage;

import com.ayakacraft.carpetayakaaddition.helpers.rules.FakePlayerMessageHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @WrapWithCondition(
            method = "onDisconnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                    remap = false
            )
    )
    public boolean suppressLogOnLeave(Logger instance, String s, Object o, Object o2) {
        return FakePlayerMessageHelper.shouldBroadcast(player);
    }

    @WrapWithCondition(
            //#if MC>12001
            method = "removePlayerFromWorld",
            //#else
            //$$ method = "onDisconnect",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC>=11900
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
                    //#elseif MC>=11600
                    //$$ target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"
                    //#else
                    //$$ target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"
                    //#endif
            )
    )
    private boolean suppressBroadcastOnLeave(
            PlayerList instance, Component message
            //#if MC>=11900
            , boolean bypassHiddenChat
            //#elseif MC>=11600
            //$$ , net.minecraft.network.chat.ChatType chatType, java.util.UUID uuid
            //#endif
    ) {
        return FakePlayerMessageHelper.shouldBroadcast(player);
    }

}
