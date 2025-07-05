/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;
import carpet.settings.Validator;
import com.ayakacraft.carpetayakaaddition.mixin.settings.ParsedRuleAccessor;
import com.ayakacraft.carpetayakaaddition.utils.translation.AyakaLanguage;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class AyakaRule implements carpet.settings.Rule {

    public static final Translator RULE_TR = Translator.CARPET.resolve("rule");

    private final Field field;

    private final String name;

    private final Rule rule;

    private final SettingsManager settingsManager;

    private final Translator tr;

    public AyakaRule(Field field, SettingsManager settingsManager) {
        this.field = field;
        this.name = field.getName();
        this.rule = field.getAnnotation(Rule.class);
        this.settingsManager = settingsManager;
        this.tr = RULE_TR.resolve(this.name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return tr.translate(AyakaLanguage.getServerLanguage(), "desc");
    }

    @Override
    public String[] extra() {
        AyakaLanguage serverLang = AyakaLanguage.getServerLanguage();
        String       x;
        List<String> extras = new LinkedList<>();
        int          i      = 0;
        while ((x = tr.translateWithoutFallback(serverLang, "extra." + i)) != null) {
            extras.add(x);
            i++;
        }
        return extras.toArray(new String[0]);
    }

    @Override
    public String[] category() {
        return rule.categories();
    }

    @Override
    public String[] options() {
        return rule.options();
    }

    @Override
    public boolean strict() {
        return rule.strict();
    }

    //#if MC>=11600
    @Override
    public String appSource() {
        return rule.appSource();
    }
    //#endif

    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Validator>[] validate() {
        return rule.validators();
    }

    //#if MC>=11600
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends carpet.settings.Condition>[] condition() {
        return new Class[0];
    }
    //#endif

    @Override
    public Class<? extends Annotation> annotationType() {
        return carpet.settings.Rule.class;
    }

    public ParsedRule<?> constructCarpetRule() {
        ParsedRule<?> parsedRule;
        try {
            parsedRule = ParsedRuleAccessor.invokeConstructor(field, this
                    //#if MC>=11600
                    , settingsManager
                    //#endif
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parsedRule;
    }

}
