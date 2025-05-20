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

import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = "<1.16"))
@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {

    //#if MC<11600
    //$$ @org.spongepowered.asm.mixin.injection.Inject(method = "loadWorld", at = @org.spongepowered.asm.mixin.injection.At("TAIL"))
    //$$ private void onLoadWorld(org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
    //$$     com.ayakacraft.carpetayakaaddition.CarpetAyakaServer.INSTANCE.onServerLoadedWorlds$Ayaka();
    //$$ }
    //#endif

}
