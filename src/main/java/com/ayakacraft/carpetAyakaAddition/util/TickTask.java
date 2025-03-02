package com.ayakacraft.carpetAyakaAddition.util;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaAddition;
import net.minecraft.server.MinecraftServer;

public abstract class TickTask {

    protected final CarpetAyakaAddition modServer;

    protected final MinecraftServer mcServer;

    private boolean finished;

    public TickTask(CarpetAyakaAddition modServer) {
        this.modServer = modServer;
        this.mcServer = modServer.mcServer;
    }

    public void start() {
    }

    protected void finish() {
        this.finished = true;
    }

    public abstract void tick();

    public boolean isFinished() {
        return finished;
    }

}
