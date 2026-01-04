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

package com.ayakacraft.carpetayakaaddition.mixin.rules.fakePlayerResidentBackupFix;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.helpers.mods.GcaHelper;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(ModUtils.GCA_ID))
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            //#if MC>=11600
            method = "runServer",
            //#else
            //$$ method = "run",
            //#endif
            //#if MC>=11700
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;getNanos()J")
            //#else
            //$$ at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J")
            //#endif
    )
    private void saveFakePlayersOnStartup(CallbackInfo ci) {
        if (CarpetAyakaSettings.fakePlayerResidentBackupFix) {
            GcaHelper.storeFakesIfNeeded((MinecraftServer) (Object) this);
        }
    }

    @Inject(method = "saveAllChunks", at = @At("RETURN"))
    private void save(CallbackInfoReturnable<Boolean> cir) {
        if (CarpetAyakaSettings.fakePlayerResidentBackupFix) {
            GcaHelper.storeFakesIfNeeded((MinecraftServer) (Object) this);
        }
    }

}
