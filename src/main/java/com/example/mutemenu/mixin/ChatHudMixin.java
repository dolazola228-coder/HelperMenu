package com.example.mutemenu.mixin;

import com.example.mutemenu.MuteMenuClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Text muteMenu$makePlayerNamesClickable(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null || message == null) return message;
        List<String> names = new ArrayList<>();
        client.getNetworkHandler().getPlayerList().forEach(entry -> names.add(entry.getProfile().getName()));
        names.sort(Comparator.comparingInt(String::length).reversed());
        String raw = message.getString();
        Pattern pattern = Pattern.compile("(?<![A-Za-z0-9_])(" + names.stream().map(Pattern::quote).reduce((a,b) -> a + "|" + b).orElse("(?!)") + ")(?![A-Za-z0-9_])");
        Matcher matcher = pattern.matcher(raw);
        MutableText result = Text.empty();
        int last = 0;
        boolean found = false;
        while (matcher.find()) {
            found = true;
            if (matcher.start() > last) result.append(Text.literal(raw.substring(last, matcher.start())));
            String name = matcher.group(1);
            Style style = Style.EMPTY.withColor(0x55FFFF).withUnderline(true)
                .withClickEvent(new ClickEvent.SuggestCommand("/muteui " + name));
            result.append(Text.literal(name).setStyle(style));
            last = matcher.end();
        }
        if (!found) return message;
        if (last < raw.length()) result.append(Text.literal(raw.substring(last)));
        return result;
    }
}
