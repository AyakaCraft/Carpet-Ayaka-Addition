package com.ayakacraft.carpetAyakaAddition.data;

import lombok.Getter;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Getter
public class Waypoint {

    public String id;

    public String dim;

    public Vec3d pos;

    public Waypoint(String id, RegistryKey<World> dim, Vec3d pos) {
        this.id = id;
        this.dim = dim.getValue().toString();
        this.pos = pos;
    }

    public RegistryKey<World> getDimension() {
        return RegistryKey.of(RegistryKeys.WORLD, new Identifier(dim));
    }

    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public double getZ() {
        return pos.getZ();
    }

}
