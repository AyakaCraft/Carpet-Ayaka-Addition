/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.mixin.utils;

import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import com.ayakacraft.carpetayakaaddition.utils.translation.WithClientLanguage;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<=1.20.1"))
@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements WithClientLanguage {

    @Unique
    private String clientLanguage = AyakaLanguage.getServerLanguage().code();

    @Inject(method = "updateOptions", at = @At("RETURN"))
    private void catchClientLanguage(
            //#if MC>=12006
            net.minecraft.server.level.ClientInformation clientOptions,
            //#else
            //$$ net.minecraft.network.protocol.game.ServerboundClientInformationPacket clientOptions,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC>=11800
        clientLanguage = clientOptions.language();
        //#elseif MC>=11600
        //$$ clientLanguage = ((ServerboundClientInformationPacketAccessor) clientOptions).getLanguage$Ayaka();
        //#else
        //$$ clientLanguage = clientOptions.getLanguage();
        //#endif
    }


    @Override
    @Unique
    public String getClientLanguage$Ayaka() {
        return clientLanguage;
    }

}
