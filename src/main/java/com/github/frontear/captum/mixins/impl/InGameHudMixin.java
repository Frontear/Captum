package com.github.frontear.captum.mixins.impl;

import java.awt.Color;
import lombok.val;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow
    protected abstract void renderHotbarItem(final int i, final int j, final float f,
        final PlayerEntity playerEntity, final ItemStack itemStack);

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE",
        target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V",
        shift = Shift.AFTER))
    private void renderHotbar(final float partialTicks, final CallbackInfo info) {
        //noinspection ConstantConditions
        val armor = client.player.inventory.armor;

        // the armor list is backwards, helmet -> 3, boots -> 0
        renderItem(true, scaledHeight - 36, partialTicks, armor.get(3));
        renderItem(true, scaledHeight - 18, partialTicks, armor.get(2));
        renderItem(false, scaledHeight - 34, partialTicks, armor.get(1));
        renderItem(false, scaledHeight - 16, partialTicks, armor.get(0));

        renderItem(false, scaledHeight - 54, partialTicks, client.player.inventory.getInvStack(client.player.inventory.selectedSlot));
        renderItem(true, scaledHeight - 54, partialTicks, client.player.getOffHandStack());
    }

    private void renderItem(final boolean left, final int y, final float partialTicks, final ItemStack item) {
        if (item.isEmpty()) return;

        val text = client.textRenderer;
        val durability = getDurability(item);
        val x = scaledWidth / 2 + (left ? -110 : +96);

        renderHotbarItem(x, y, partialTicks, client.player, item);
        if ("0/0 (0%)".equals(durability)) return;
        text.draw(durability, x + (left ? -text.getStringWidth(durability) - 2 : +18), y + 5, Color.WHITE.getRGB());
    }

    private String getDurability(final ItemStack item) {
        val max = item.getMaxDamage();
        val cur = item.getDamage();
        val per = (int) ((((double) (max - cur) / max) * 100) + 0.5); // performs accurate rounding

        return (max - cur) + "/" + max + " " + "(" + per + "%" + ")";
    }
}
