package com.example.mutemenu.mixin;

import com.example.mutemenu.MuteMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    private void muteMenu$openMenu(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style == null || style.getClickEvent() == null) return;
        ClickEvent event = style.getClickEvent();
        if (event instanceof ClickEvent.SuggestCommand suggest && suggest.command().startsWith("/muteui ")) {
            String player = suggest.command().substring("/muteui ".length()).trim();
            if (player.matches("[A-Za-z0-9_]{1,16}")) {
                MinecraftClient.getInstance().setScreen(new MuteMenuScreen(player));
                cir.setReturnValue(true);
            }
        }
    }
}
