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

package com.ayakacraft.carpetayakaaddition.mixin.carpet;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.logging.AyakaExtensionLogger;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<1.15"))
@Mixin(value = LoggerRegistry.class, remap = false)
public abstract class LoggerRegistryMixin {

    @Inject(method = "initLoggers", at = @At("RETURN"))
    private static void registerToCarpet(CallbackInfo ci) {
        AyakaLoggerRegistry.ayakaLoggers.forEach(it -> registerLogger(it.getLogName(), it));
    }

    @Inject(method = "setAccess", at = @At("HEAD"), cancellable = true)
    private static void onSetAccess(Logger logger, CallbackInfo ci) {
        if (logger instanceof AyakaExtensionLogger) {
            try {
                ((AyakaExtensionLogger) logger).getField().setBoolean(null, logger.hasOnlineSubscribers());
            } catch (IllegalAccessException e) {
                CarpetAyakaAddition.LOGGER.warn("Cannot change logger quick access field, logger might be disabled", e);
            }
            ci.cancel();
        }
    }

    @Shadow
    //#if MC>=11500
    public
    //#else
    //$$ private
    //#endif
    static void registerLogger(String name, Logger logger) {
    }

}
