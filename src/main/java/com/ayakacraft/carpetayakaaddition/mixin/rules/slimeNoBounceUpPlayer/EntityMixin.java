package com.ayakacraft.carpetayakaaddition.mixin.rules.slimeNoBounceUpPlayer;

import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.ayakacraft.carpetayakaaddition.utils.mixin.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=26.2"))
@Mixin(DummyClass.class)
public class EntityMixin {

    // Implementation in 26.2

}
