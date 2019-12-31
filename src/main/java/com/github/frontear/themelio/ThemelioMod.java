package com.github.frontear.themelio;

import com.github.frontear.themelio.mixins.IMinecraftClient;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public final class ThemelioMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ((IMinecraftClient) MinecraftClient.getInstance()).getLogger().info("Hello Fabric!");
    }
}
