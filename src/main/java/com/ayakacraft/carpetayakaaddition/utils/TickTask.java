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
import net.minecraft.server.MinecraftServer;

public abstract class TickTask {

    protected final CarpetAyakaServer modServer;

    protected final MinecraftServer mcServer;

    private boolean finished;

    private boolean cancelled;

    public TickTask(CarpetAyakaServer modServer) {
        this.modServer = modServer;
        this.mcServer = modServer.mcServer;
    }

    /**
     * Called internally when the process is done
     */
    protected void finish() {
        this.finished = true;
    }

    public void start() {
    }

    public void cancel() {
        this.cancelled = true;
    }

    public abstract void tick();

    public boolean isFinished() {
        return this.finished || this.cancelled;
    }

    public static abstract class CountDownTask extends TickTask implements Runnable {

        private final int endTick;

        public CountDownTask(CarpetAyakaServer modServer, int ticksToCount) {
            super(modServer);
            this.endTick = mcServer.getTicks() + ticksToCount;
        }

        @Override
        public void tick() {
            if (mcServer.getTicks() >= endTick) {
                this.run();
                this.finish();
            }
        }

    }

    public static abstract class FrequentTask extends TickTask implements Runnable {

        private final int frequency;

        public FrequentTask(CarpetAyakaServer modServer, int frequency) {
            super(modServer);
            this.frequency = frequency;
        }

        @Override
        public void tick() {
            if (mcServer.getTicks() % frequency == 0) {
                this.run();
            }
        }

    }

    public static class RunPerTickTask extends TickTask {

        private static final Runnable EMPTY_TASK = () -> {
        };

        private final Runnable task;

        private final boolean runOnCancel;

        public RunPerTickTask(CarpetAyakaServer modServer, Runnable task, boolean runOnCancel) {
            super(modServer);
            this.task = task == null ? EMPTY_TASK : task;
            this.runOnCancel = runOnCancel;
        }

        @Override
        public void cancel() {
            if (runOnCancel) {
                task.run();
            }
            super.cancel();
        }

        @Override
        public void tick() {
            task.run();
        }

    }

}
