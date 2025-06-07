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

package com.ayakacraft.carpetayakaaddition.network;

import java.net.Proxy;
import java.net.SocketAddress;
import java.util.function.Supplier;

public class DynamicProxy extends Proxy {

    private final Supplier<Type> type;

    private final Supplier<SocketAddress> sa;

    public DynamicProxy(Supplier<Type> type, Supplier<SocketAddress> sa) {
        super(Type.HTTP, sa.get()); // Just a placeholder
        this.type = type;
        this.sa = sa;
    }

    @Override
    public Type type() {
        return type.get();
    }

    @Override
    public SocketAddress address() {
        return type() == Type.DIRECT ? null : sa.get();
    }

}
