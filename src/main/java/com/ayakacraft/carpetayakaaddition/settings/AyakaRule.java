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

import carpet.api.settings.CarpetRule;
import carpet.api.settings.SettingsManager;
import carpet.api.settings.Validator;
import org.jetbrains.annotations.Contract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

//Do not remove the lines below
//TODO update in 1.18.2
@SuppressWarnings("ClassExplicitlyAnnotation")
public class AyakaRule implements carpet.api.settings.Rule {

    private final Field field;

    private final Rule rule;

    private final SettingsManager settingsManager;

    public AyakaRule(Field field, SettingsManager settingsManager) {
        this.field = field;
        this.rule = field.getAnnotation(Rule.class);
        this.settingsManager = settingsManager;
    }

    @Contract(pure = true)
    @Override
    public String[] categories() {
        return rule.categories();
    }

    @Contract(pure = true)
    @Override
    public String[] options() {
        return rule.options();
    }

    @Contract(pure = true)
    @Override
    public boolean strict() {
        return rule.strict();
    }

    @Contract(pure = true)
    @Override
    public String appSource() {
        return rule.appSource();
    }

    @Contract(pure = true)
    @SuppressWarnings("rawtypes")
    @Override
    public Class<? extends Validator>[] validators() {
        return rule.validators();
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Condition>[] conditions() {
        return new Class[0];
    }

    @Contract(pure = true)
    @Override
    public Class<? extends Annotation> annotationType() {
        return carpet.api.settings.Rule.class;
    }

    @Contract(pure = true)
    public CarpetRule<?> constructCarpetRule() {
        CarpetRule<?> carpetRule;
        try {
            Class<?>       ruleAnnotationClass = Class.forName("carpet.settings.ParsedRule$RuleAnnotation");
            Constructor<?> ctr1                = ruleAnnotationClass.getDeclaredConstructors()[0];
            ctr1.setAccessible(true);
            Object ruleAnnotation = ctr1.newInstance(false, null, null, null, categories(), options(), strict(), appSource(), validators());

            Class<?>       parsedRuleClass = Class.forName("carpet.settings.ParsedRule");
            Constructor<?> ctr2            = parsedRuleClass.getDeclaredConstructor(Field.class, ruleAnnotationClass, SettingsManager.class);
            ctr2.setAccessible(true);
            carpetRule = (CarpetRule<?>) ctr2.newInstance(field, ruleAnnotation, settingsManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return carpetRule;
    }

}
