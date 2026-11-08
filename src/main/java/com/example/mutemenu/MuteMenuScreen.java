package com.example.mutemenu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MuteMenuScreen extends Screen {
    private final String playerName;
    private int top;
    private int rowHeight;

    public MuteMenuScreen(String playerName) {
        super(Text.literal("Мут игрока: " + playerName));
        this.playerName = playerName;
    }

    @Override protected void init() {
        this.top = 38;
        this.rowHeight = Math.max(22, Math.min(30, (this.height - 70) / MuteRules.ALL.size()));
        int buttonWidth = Math.min(420, this.width - 30);
        int left = (this.width - buttonWidth) / 2;
        for (int i = 0; i < MuteRules.ALL.size(); i++) {
            MuteRules.Rule rule = MuteRules.ALL.get(i);
            int y = top + i * rowHeight;
            addDrawableChild(ButtonWidget.builder(
                Text.literal(rule.id() + "  |  " + rule.reason() + "  —  " + rule.duration()),
                button -> applyRule(rule)
            ).dimensions(left, y, buttonWidth, rowHeight - 2).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Отмена"), button -> close())
            .dimensions((this.width - 100) / 2, Math.min(this.height - 28, top + MuteRules.ALL.size() * rowHeight + 5), 100, 20).build());
    }

    private void applyRule(MuteRules.Rule rule) {
        if (client != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendChatCommand("tempmute " + playerName + " " + rule.duration() + " " + rule.id());
        }
        close();
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Выбери правило для " + playerName), width / 2, 16, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
}
