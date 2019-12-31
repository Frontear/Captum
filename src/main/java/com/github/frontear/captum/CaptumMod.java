package com.github.frontear.captum;

import com.github.frontear.captum.mixins.IMinecraftClient;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public final class CaptumMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ((IMinecraftClient) MinecraftClient.getInstance()).getLogger().info("Hello Fabric!");
    }
}
