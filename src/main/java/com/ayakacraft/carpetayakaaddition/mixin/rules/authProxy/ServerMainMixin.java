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

package com.ayakacraft.carpetayakaaddition.mixin.rules.authProxy;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.network.DynamicProxy;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

//Do not remove the lines below
//TODO update in 1.15.2
@Mixin(Main.class)
public class ServerMainMixin {

    @Unique
    private static String hostCache = CarpetAyakaSettings.authProxyHost;

    @Unique
    private static int portCache = CarpetAyakaSettings.authProxyPort;

    @Unique
    private static InetSocketAddress addressCache = new InetSocketAddress(hostCache, portCache);

    @ModifyArg(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/authlib/yggdrasil/YggdrasilAuthenticationService;<init>(Ljava/net/Proxy;)V",
                    remap = false
            ),
            index = 0
    )
    private static Proxy useDynamicProxy(Proxy proxy) {
        if (proxy instanceof DynamicProxy) {
            return proxy;
        }
        return new DynamicProxy(
                () -> CarpetAyakaSettings.authProxyType,
                () -> {
                    if (!Objects.equals(CarpetAyakaSettings.authProxyHost, hostCache) || CarpetAyakaSettings.authProxyPort != portCache) {
                        hostCache = CarpetAyakaSettings.authProxyHost;
                        portCache = CarpetAyakaSettings.authProxyPort;
                        addressCache = new InetSocketAddress(hostCache, portCache);
                    }
                    return addressCache;
                }
        );
    }

}
