package com.ayakacraft.carpetayakaaddition.utils;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaServer;
import net.minecraft.server.MinecraftServer;

public abstract class TickTask {

    protected final CarpetAyakaServer modServer;

    protected final MinecraftServer mcServer;

    private boolean finished;

    public TickTask(CarpetAyakaServer modServer) {
        this.modServer = modServer;
        this.mcServer = modServer.mcServer;
    }

    public void start() {
    }

    protected void finish() {
        this.finished = true;
    }

    public void cancel() {
    }

    public abstract void tick();

    public boolean isFinished() {
        return finished;
    }

}
