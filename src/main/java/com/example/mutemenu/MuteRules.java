package com.example.mutemenu;

import java.util.List;

public final class MuteRules {
    private MuteRules() {}
    public record Rule(String id, String reason, String duration) {}
    public static final List<Rule> ALL = List.of(
        new Rule("3.1", "Оскорбление игроков", "1h"),
        new Rule("3.2", "Более 70% CAPS", "30m"),
        new Rule("3.3", "Упоминание родных", "3d"),
        new Rule("3.4", "Флуд или спам", "1h"),
        new Rule("3.5", "Реклама других проектов", "14d"),
        new Rule("3.6", "Критика сервера/администрации", "1d"),
        new Rule("3.7", "Выдавать себя за администрацию", "1d"),
        new Rule("3.8", "Политическая агитация, межнациональная рознь", "7d"),
        new Rule("3.9", "Провокация на нарушение правил", "7d"),
        new Rule("3.10", "Попрошайничество", "1h")
    );
}
