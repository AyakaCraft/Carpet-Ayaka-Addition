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

import carpet.logging.HUDController;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<1.16"))
@Mixin(HUDController.class)
public class HUDControllerMixin {

    //#if MC<11600
    //$$ @org.spongepowered.asm.mixin.injection.Inject(
    //$$         method = "update_hud",
    //$$         at = @org.spongepowered.asm.mixin.injection.At(
    //$$                 value = "INVOKE",
    //$$                 target = "Ljava/util/Map;keySet()Ljava/util/Set;"
    //$$         ),
    //$$         remap = false
    //$$ )
    //$$ private static void updateAyakaHUDLoggers(net.minecraft.server.MinecraftServer server, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci)
    //$$ {
    //$$     com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry.updateHUD();
    //$$ }
    //#endif

}
