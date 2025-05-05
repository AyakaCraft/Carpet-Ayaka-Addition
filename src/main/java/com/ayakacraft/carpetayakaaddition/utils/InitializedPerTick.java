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

package com.ayakacraft.carpetayakaaddition.utils;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaServer;

@FunctionalInterface
public interface InitializedPerTick {

    void init();

    default TickTask getInitTask(CarpetAyakaServer modServer) {
        return new CounterInitTask(modServer, this);
    }

    class CounterInitTask extends TickTask {

        private final InitializedPerTick initializedPerTick;

        public CounterInitTask(CarpetAyakaServer modServer, InitializedPerTick initializedPerTick) {
            super(modServer);
            this.initializedPerTick = initializedPerTick;
        }

        @Override
        public void cancel() {
            super.cancel();
            initializedPerTick.init();
        }

        @Override
        public void tick() {
            initializedPerTick.init();
        }

    }

}
