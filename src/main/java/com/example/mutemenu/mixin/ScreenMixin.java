
package com.example.mutemenu.mixin;

import com.example.mutemenu.MuteMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(
        method = "handleClickEvent(Lnet/minecraft/text/ClickEvent;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void muteMenu$openMenu(
            ClickEvent clickEvent,
            MinecraftClient client,
            Screen screenAfterRun,
            CallbackInfo ci
    ) {
        if (clickEvent instanceof ClickEvent.SuggestCommand suggest
                && suggest.command().startsWith("/muteui ")) {
            String player = suggest.command()
                    .substring("/muteui ".length()).trim();

            if (player.matches("[A-Za-z0-9_]{1,16}")) {
                client.setScreen(new MuteMenuScreen(player));
                ci.cancel();
            }
        }
    }
}
