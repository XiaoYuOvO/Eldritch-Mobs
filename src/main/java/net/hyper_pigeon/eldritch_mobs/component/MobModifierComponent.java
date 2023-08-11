package net.hyper_pigeon.eldritch_mobs.component;

import net.hyper_pigeon.eldritch_mobs.EldritchMobsMod;
import net.hyper_pigeon.eldritch_mobs.ability.Ability;
import net.hyper_pigeon.eldritch_mobs.ability.AbilityHelper;
import net.hyper_pigeon.eldritch_mobs.component.interfaces.ModifierComponent;
import net.hyper_pigeon.eldritch_mobs.persistent_state.SoothingLanternPersistentState;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankCategory;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankLevel;
import net.hyper_pigeon.eldritch_mobs.register.EldritchMobTagKeys;
import net.hyper_pigeon.eldritch_mobs.util.NormalSampler;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MobModifierComponent implements ModifierComponent {

    private MobRankLevel rank = MobRankLevel.UNDECIDED;
    private List<Ability> modifiers = new ArrayList<>();
    private final MobEntity provider;
    private int numMaxAbilities = 0;
    private final ServerBossBar bossBar;
    private boolean healthIncreased = false;
    private boolean checkedIfSpawnedInSoothingLanternChunk = false;
    private boolean titleSet = false;
    private final MutableEntityAttributeModifier healthModifier = new MutableEntityAttributeModifier("eldritch.health_boost", 1.0d, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    private final NormalSampler<MobRankLevel> levelSampler;

    public MobModifierComponent(MobEntity provider){
        this.provider = provider;
        this.bossBar = new ServerBossBar(provider.getDisplayName(), BossBar.Color.GREEN, BossBar.Style.PROGRESS);
        this.levelSampler = new NormalSampler<>(Arrays.stream(MobRankLevel.values()).toList(), MobRankLevel.NONE, this.provider.getRandom(),2);
        if(canBeBuffed(provider)){
            randomlySetRank();
            randomlySetModifiers();
        }
        else {
            this.rank = MobRankLevel.NONE;
        }

    }

    public boolean canBeBuffed(MobEntity mobEntity){
        return this.rank.getCategory() == MobRankCategory.UNDECIDED && !(mobEntity.hasCustomName() && EldritchMobsMod.ELDRITCH_MOBS_CONFIG.ignoreNamedMobs)
                && (mobEntity.getType().isIn(EldritchMobTagKeys.ALLOWED) &&
                !mobEntity.getType().isIn(EldritchMobTagKeys.BLACKLIST));
    }

    @Override
    public void randomlySetRank() {
        if(rank.getCategory() == MobRankCategory.UNDECIDED && this.provider instanceof HostileEntity) {
            long day = getDay();
            this.setRank(this.levelSampler.sample((int) Math.min(this.levelSampler.samplerSize(), day / 48),-0.012 * Math.exp(0.01 * day) + 0.9));
        }
    }

    private int getDay(){
        return (int) (this.provider.getWorld().getLevelProperties().getTimeOfDay()/ 24000L);
    }

    @Override
    public void randomlySetModifiers() {
        if(rank.isBuffed()) {
            modifiers = AbilityHelper.pickNRandomForEntity(AbilityHelper.ALL_ABILITIES, numMaxAbilities, provider.getType());
        }
    }

    public void setTitle(){
        if(!provider.hasCustomName() && !EldritchMobsMod.ELDRITCH_MOBS_CONFIG.turnOffTitles && rank.getCategory() != MobRankCategory.NONE) {
            provider.setCustomName(getTitle());
        }
    }

    public Text getTitle(){
        LiteralText output = new LiteralText("");
        MutableText rankText = new TranslatableText(this.rank.getTranslationKey()).formatted(this.rank.getNameFormatting(), Formatting.UNDERLINE);
        Text displayName = provider.getDisplayName();
        if(EldritchMobsMod.ELDRITCH_MOBS_CONFIG.genericTitles) {
            output.append(rankText).append(" ").append(displayName);
        }
        else {
            for (Ability ability: modifiers){
                output.append(new TranslatableText("text.autoconfig.eldritch_mobs.option." + ability.getName().toLowerCase() + "Config").formatted(getAbilityFormatting(ability))).append(" ");
            }
            output.append(rankText);
            output.append(displayName);
        }
        return output;
    }

    private Formatting getAbilityFormatting(Ability ability){
        return switch (ability.getAbilityType()) {
            case ACTIVE -> Formatting.RED;
            case PASSIVE -> Formatting.GREEN;
        };
    }


    @Override
    public List<Ability> getModifiers() {
        return modifiers;
    }

    @Override
    public void clearModifiers() {
        modifiers.clear();
        numMaxAbilities = 0;
    }

    public MobRankLevel getRank(){
        return rank;
    }

    public void setRank(MobRankLevel mobRank){
        this.rank = mobRank;
        numMaxAbilities = mobRank.getSkillBonus();
        this.bossBar.setColor(mobRank.getColor());
//        if(mobRank == MobRankCategory.ELITE){
//            numMaxAbilities = AbilityHelper.random.nextInt(EldritchMobsMod.ELDRITCH_MOBS_CONFIG.EliteMinModifiers,EldritchMobsMod.ELDRITCH_MOBS_CONFIG.EliteMaxModifiers+1);
//            this.bossBar.setColor(BossBar.Color.YELLOW);
//        }
//        else if(mobRank == MobRankCategory.ULTRA){
//            numMaxAbilities = AbilityHelper.random.nextInt(EldritchMobsMod.ELDRITCH_MOBS_CONFIG.UltraMinModifiers,EldritchMobsMod.ELDRITCH_MOBS_CONFIG.UltraMaxModifiers+1);
//            this.bossBar.setColor(BossBar.Color.RED);
//        }
//        else if(mobRank == MobRankCategory.ELDRITCH){
//            numMaxAbilities = AbilityHelper.random.nextInt(EldritchMobsMod.ELDRITCH_MOBS_CONFIG.EldritchMinModifiers,EldritchMobsMod.ELDRITCH_MOBS_CONFIG.EldritchMaxModifiers+1);
//            this.bossBar.setColor(BossBar.Color.PURPLE);
//        }
    }

    public void increaseHealth(){
        if(!healthIncreased){
            EntityAttributeInstance entityAttributeInstance = provider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            assert entityAttributeInstance != null;
            if (!entityAttributeInstance.hasModifier(healthModifier)) {
                healthModifier.setValue(this.rank.getHealthBonus());
                entityAttributeInstance.addPersistentModifier(healthModifier);
            }
            provider.setHealth((float)provider.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
//            if(this.rank == MobRankCategory.ELITE){
//                EntityAttributeInstance entityAttributeInstance = provider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
//                assert entityAttributeInstance != null;
//                if(!entityAttributeInstance.hasModifier(EldritchMobsAttributeModifiers.ELITE_HEALTH_BOOST)) {
//                    entityAttributeInstance.addPersistentModifier(EldritchMobsAttributeModifiers.ELITE_HEALTH_BOOST);
//                }
//                provider.setHealth((float)provider.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
//            }
//            else if(this.rank == MobRankCategory.ULTRA){
//                EntityAttributeInstance entityAttributeInstance = provider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
//                assert entityAttributeInstance != null;
//                if(!entityAttributeInstance.hasModifier(EldritchMobsAttributeModifiers.ULTRA_HEALTH_BOOST)) {
//                    entityAttributeInstance.addPersistentModifier(EldritchMobsAttributeModifiers.ULTRA_HEALTH_BOOST);
//                }
//                provider.setHealth((float)provider.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
//            }
//            else if(this.rank == MobRankCategory.ELDRITCH){
//                EntityAttributeInstance entityAttributeInstance = provider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
//                assert entityAttributeInstance != null;
//                if(!entityAttributeInstance.hasModifier(EldritchMobsAttributeModifiers.ELDRITCH_HEALTH_BOOST)){
//                    entityAttributeInstance.addPersistentModifier(EldritchMobsAttributeModifiers.ELDRITCH_HEALTH_BOOST);
//                }
//                provider.setHealth((float)provider.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
//            }
            healthIncreased = true;
        }
    }

    @Override
    public ServerBossBar getBossBar() {
        return bossBar;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

        healthIncreased = tag.getBoolean("healthIncreased");
        numMaxAbilities = tag.getInt("numMaxAbilities");
        checkedIfSpawnedInSoothingLanternChunk = tag.getBoolean("checkedIfSpawnedInSoothingLanternChunk");
        titleSet = tag.getBoolean("titleSet");
        if (tag.contains("RankLevel")) {
            this.rank = MobRankLevel.valueOf(tag.getString("RankLevel").toUpperCase());
            if (rank.isBuffed()){
                this.bossBar.setColor(rank.getColor());
            }
        }

//        if(numMaxAbilities == 0){
//            rank = MobRankCategory.NONE;
//        }
//        else if(numMaxAbilities <= 4 && numMaxAbilities >= 1){
//            rank = MobRankCategory.ELITE;
//            this.bossBar.setColor(BossBar.Color.YELLOW);
//        }
//        else if(numMaxAbilities <= 8 && numMaxAbilities >= 5){
//            rank = MobRankCategory.ULTRA;
//            this.bossBar.setColor(BossBar.Color.RED);
//        }
//        else if(numMaxAbilities <= 12 && numMaxAbilities >= 9){
//            rank = MobRankCategory.ULTRA;
//            this.bossBar.setColor(BossBar.Color.PURPLE);
//        }

        NbtCompound abilities = tag.getCompound("abilities");

        if(modifiers != null) {
            modifiers.clear();
        }

        for (String ability : abilities.getKeys()){
            modifiers.add(AbilityHelper.ABILITY_NAMES.get(ability));
        }

    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("healthIncreased", healthIncreased);
        tag.putInt("numMaxAbilities",numMaxAbilities);
        tag.putBoolean("checkedIfSpawnedInSoothingLanternChunk",checkedIfSpawnedInSoothingLanternChunk);
        tag.putBoolean("titleSet",titleSet);
        tag.putString("RankLevel", rank.name().toLowerCase());
        NbtCompound mobAbilities = new NbtCompound();

        if(modifiers != null){
            for (Ability ability : modifiers) {
                mobAbilities.putString(ability.getName(), ability.getName());
            }
        }

        tag.put("abilities",mobAbilities);

    }

    public void makeMobNormal(){
        this.rank = MobRankLevel.NONE;
        clearModifiers();
        provider.setCustomName(null);
        this.bossBar.clearPlayers();
    }

    boolean isPlayerStaring(ServerPlayerEntity player) {
        Vec3d vec3d = player.getEyePos();
        Vec3d vec3d2 = player.getRotationVec(1.0F);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 100.0D, vec3d2.y * 100.0D, vec3d2.z * 100.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityCollision(player.world, player, vec3d, vec3d3, (new Box(vec3d, vec3d3)).expand(1.0D), (entityx) -> !entityx.isSpectator() && entityx instanceof MobEntity, 0.0F);
        if (entityHitResult != null && entityHitResult.getType() == HitResult.Type.ENTITY) {
            MobEntity mobEntity = (MobEntity) entityHitResult.getEntity();
            return mobEntity.equals(provider);
        }
        return false;
    }



    @Override
    public void serverTick() {
        if(!checkedIfSpawnedInSoothingLanternChunk)
        {
            if(this.rank.getCategory() != MobRankCategory.NONE && !provider.getEntityWorld().isClient() &&
                    SoothingLanternPersistentState.get((ServerWorld) provider.getEntityWorld()).containsChunk(provider.getChunkPos())){
                makeMobNormal();
            }
            checkedIfSpawnedInSoothingLanternChunk = true;
        }

        if(getRank().getCategory() != MobRankCategory.NONE) {
            if(!provider.hasCustomName() && !titleSet &&!EldritchMobsMod.ELDRITCH_MOBS_CONFIG.turnOffTitles){
                setTitle();
                titleSet = true;
            }

            if(!EldritchMobsMod.ELDRITCH_MOBS_CONFIG.turnOffBossBars) {
                if(!provider.hasCustomName()) {
                    this.bossBar.setName(getTitle());
                }
                else {
                    this.bossBar.setName(provider.getCustomName());
                }
                this.bossBar.setPercent(provider.getHealth() / provider.getMaxHealth());

                if(EldritchMobsMod.ELDRITCH_MOBS_CONFIG.crosshairBossBars) {
                    List<ServerPlayerEntity> bossBarPlayers = bossBar.getPlayers().stream().toList();
                    bossBarPlayers.forEach(player -> {
                        if(!isPlayerStaring(player)) {
                            bossBar.removePlayer(player);
                        }
                    });
                }

            }
            increaseHealth();
        }
    }
}
