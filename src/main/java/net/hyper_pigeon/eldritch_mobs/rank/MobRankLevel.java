package net.hyper_pigeon.eldritch_mobs.rank;

import net.hyper_pigeon.eldritch_mobs.EldritchMobsMod;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum MobRankLevel {
    UNDECIDED(MobRankCategory.UNDECIDED,0,0,0,0, BossBar.Color.WHITE, Formatting.WHITE),
    NONE(MobRankCategory.NONE,0,0,0,0, BossBar.Color.WHITE, Formatting.WHITE),
    ELITE_I(MobRankCategory.ELITE,1,1,1,1, BossBar.Color.WHITE, Formatting.WHITE),
    ELITE_II(MobRankCategory.ELITE,2,2,2,2, BossBar.Color.WHITE, Formatting.WHITE),
    ELITE_III(MobRankCategory.ELITE,3,3,3,3, BossBar.Color.GREEN, Formatting.GREEN),
    ULTRA_I(MobRankCategory.ULTRA,4,4,4,4, BossBar.Color.BLUE, Formatting.BLUE),
    ULTRA_II(MobRankCategory.ULTRA,5,5,5,5, BossBar.Color.YELLOW, Formatting.YELLOW),
    ULTRA_III(MobRankCategory.ULTRA,6,6,6,6, BossBar.Color.PINK, Formatting.LIGHT_PURPLE),
    ELDRITCH_I(MobRankCategory.ELDRITCH,7,7,7,7, BossBar.Color.PURPLE, Formatting.DARK_PURPLE),
    ELDRITCH_II(MobRankCategory.ELDRITCH,8,8,8,8, BossBar.Color.RED, Formatting.RED),
    ELDRITCH_III(MobRankCategory.ELDRITCH,9,9,9,9, BossBar.Color.RED, Formatting.RED);
    private final MobRankCategory category;
    private final int xpBonus;
    private final float healthBonus;
    private final float damageBonus;
    private final int skillBonus;
    private final String translationKey;
    private final BossBar.Color color;
    private final Formatting nameFormatting;

    MobRankLevel(MobRankCategory category, int xpBonus, float healthBonus, float damageBonus, int skillBonus, BossBar.Color color, Formatting nameFormatting) {
        this.category = category;
        this.xpBonus = xpBonus;
        this.healthBonus = healthBonus;
        this.damageBonus = damageBonus;
        this.skillBonus = skillBonus;
        this.color = color;
        this.nameFormatting = nameFormatting;
        this.translationKey = "entity.eldritch_mobs.level." + name().toLowerCase();
    }

    public Formatting getNameFormatting() {
        return nameFormatting;
    }

    public float getDamageBonus() {
        return damageBonus;
    }

    public float getHealthBonus() {
        return healthBonus;
    }

    public int getSkillBonus() {
        return skillBonus;
    }

    public MobRankCategory getCategory() {
        return category;
    }

    public int getXpBonus() {
        return xpBonus;
    }

    public BossBar.Color getColor() {
        return color;
    }

    public static void registerRanks(){
        for (MobRankLevel value : MobRankLevel.values()) {
            Registry.register(EldritchMobsMod.MOB_RANK_LEVELS, new Identifier("eldritch", value.name().toLowerCase()), value);
        }
    }

    public String getTranslationKey(){
        return translationKey;
    }

    public boolean isBuffed(){
        return this.category != MobRankCategory.NONE && this.category != MobRankCategory.UNDECIDED;
    }
}
