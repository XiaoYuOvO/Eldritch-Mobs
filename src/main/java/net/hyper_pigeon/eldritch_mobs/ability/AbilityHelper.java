package net.hyper_pigeon.eldritch_mobs.ability;

import net.hyper_pigeon.eldritch_mobs.EldritchMobsMod;
import net.hyper_pigeon.eldritch_mobs.ability.active.defensive.SprinterAbility;
import net.hyper_pigeon.eldritch_mobs.ability.active.offensive.*;
import net.hyper_pigeon.eldritch_mobs.ability.passive.defensive.*;
import net.hyper_pigeon.eldritch_mobs.ability.passive.offensive.*;
import net.minecraft.entity.EntityType;

import java.util.*;

public class AbilityHelper {

    public static final List<Ability> ALL_ABILITIES =
            Arrays.asList(new AlchemistAbility(), new BlindingAbility(),
                    new BurningAbility(), new DrainingAbility(), new DrowningAbility(),
                    new DuplicatorAbility(), new GhastlyAbility(), new GravityAbility(),
                    new LethargicAbility(), new RustAbility(),
                    new StarvingAbility(), new StormyAbility(), new WeaknessAbility(), new WebslingingAbility(),
                    new CloakedAbility(), new DeflectorAbility(), new EnderAbility(),
                    new UndyingAbility(), new RegeneratingAbility(), new ResistantAbility(),
                    new SprinterAbility()
                    ,new ThornyAbility(),
                    new ToxicAbility(), new WitheringAbility(), new BerserkAbility(), new LifestealAbility(),
                    new SpeedsterAbility(), new YeeterAbility());

    public static final HashMap<String, Ability> ABILITY_NAMES = new HashMap<>();
    public static final HashMap<Ability, Boolean> ABILITY_STATUS = new HashMap<>();

    public static final HashMap<String, List<EntityType<?>>> abilityBlacklist = new HashMap<>();

    static {
        ABILITY_NAMES.put(new AlchemistAbility().getName(),new AlchemistAbility());
        ABILITY_NAMES.put(new BlindingAbility().getName(),new BlindingAbility());
        ABILITY_NAMES.put(new BurningAbility().getName(), new BurningAbility());
        ABILITY_NAMES.put(new DrainingAbility().getName(), new DrainingAbility());
        ABILITY_NAMES.put(new DrowningAbility().getName(), new DrowningAbility());
        ABILITY_NAMES.put(new DuplicatorAbility().getName(), new DuplicatorAbility());
        ABILITY_NAMES.put(new GhastlyAbility().getName(), new GhastlyAbility());
        ABILITY_NAMES.put(new GravityAbility().getName(), new GravityAbility());
        ABILITY_NAMES.put(new LethargicAbility().getName(), new LethargicAbility());
        ABILITY_NAMES.put(new RustAbility().getName(), new RustAbility());
        ABILITY_NAMES.put(new StarvingAbility().getName(), new StarvingAbility());
        ABILITY_NAMES.put(new StormyAbility().getName(), new StormyAbility());
        ABILITY_NAMES.put(new WeaknessAbility().getName(), new WeaknessAbility());
        ABILITY_NAMES.put(new WebslingingAbility().getName(), new WebslingingAbility());
        ABILITY_NAMES.put(new CloakedAbility().getName(), new CloakedAbility());
        ABILITY_NAMES.put(new DeflectorAbility().getName(), new DeflectorAbility());
        ABILITY_NAMES.put(new EnderAbility().getName(),new EnderAbility());
        ABILITY_NAMES.put(new UndyingAbility().getName(), new UndyingAbility());
        ABILITY_NAMES.put(new RegeneratingAbility().getName(), new RegeneratingAbility());
        ABILITY_NAMES.put(new ResistantAbility().getName(), new ResistantAbility());
        ABILITY_NAMES.put(new SprinterAbility().getName(), new SprinterAbility());
        ABILITY_NAMES.put(new ThornyAbility().getName(), new ThornyAbility());
        ABILITY_NAMES.put(new ToxicAbility().getName(), new ToxicAbility());
        ABILITY_NAMES.put(new WitheringAbility().getName(), new WitheringAbility());
        ABILITY_NAMES.put(new BerserkAbility().getName(), new BerserkAbility());
        ABILITY_NAMES.put(new LifestealAbility().getName(), new LifestealAbility());
        ABILITY_NAMES.put(new SpeedsterAbility().getName(), new SpeedsterAbility());
        ABILITY_NAMES.put(new YeeterAbility().getName(), new YeeterAbility());

        ABILITY_STATUS.put(new AlchemistAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.alchemistConfig.disabled);
        ABILITY_STATUS.put(new BlindingAbility(),EldritchMobsMod.ELDRITCH_MOBS_CONFIG.blindingConfig.disabled);
        ABILITY_STATUS.put(new BurningAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.burningConfig.disabled);
        ABILITY_STATUS.put(new DrainingAbility(),EldritchMobsMod.ELDRITCH_MOBS_CONFIG.drainingConfig.disabled);
        ABILITY_STATUS.put(new DrowningAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.drowningConfig.disabled);
        ABILITY_STATUS.put(new DuplicatorAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.duplicatorConfig.disabled);
        ABILITY_STATUS.put(new GhastlyAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.ghastlyConfig.disabled);
        ABILITY_STATUS.put(new GravityAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.gravityConfig.disabled);
        ABILITY_STATUS.put(new LethargicAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.lethargicConfig.disabled);
        ABILITY_STATUS.put(new RustAbility(),  EldritchMobsMod.ELDRITCH_MOBS_CONFIG.rustConfig.disabled);
        ABILITY_STATUS.put(new StarvingAbility(),  EldritchMobsMod.ELDRITCH_MOBS_CONFIG.speedsterConfig.disabled);
        ABILITY_STATUS.put(new StormyAbility(),  EldritchMobsMod.ELDRITCH_MOBS_CONFIG.stormyConfig.disabled);
        ABILITY_STATUS.put(new WeaknessAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.weaknessConfig.disabled);
        ABILITY_STATUS.put(new WebslingingAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.webslingingConfig.disabled);
        ABILITY_STATUS.put(new CloakedAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.cloakedConfig.disabled);
        ABILITY_STATUS.put(new DeflectorAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.deflectorConfig.disabled);
        ABILITY_STATUS.put(new EnderAbility(),EldritchMobsMod.ELDRITCH_MOBS_CONFIG.enderConfig.disabled);
        ABILITY_STATUS.put(new UndyingAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.undyingConfig.disabled);
        ABILITY_STATUS.put(new RegeneratingAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.regeneratingConfig.disabled);
        ABILITY_STATUS.put(new ResistantAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.resistantConfig.disabled);
        ABILITY_STATUS.put(new SprinterAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.sprinterConfig.disabled);
        ABILITY_STATUS.put(new StarvingAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.starvingConfig.disabled);
        ABILITY_STATUS.put(new ThornyAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.thornyConfig.disabled);
        ABILITY_STATUS.put(new ToxicAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.toxicConfig.disabled);
        ABILITY_STATUS.put(new WitheringAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.witheringConfig.disabled);
        ABILITY_STATUS.put(new BerserkAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.berserkConfig.disabled);
        ABILITY_STATUS.put(new LifestealAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.lifestealConfig.disabled);
        ABILITY_STATUS.put(new SpeedsterAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.speedsterConfig.disabled);
        ABILITY_STATUS.put(new YeeterAbility(), EldritchMobsMod.ELDRITCH_MOBS_CONFIG.yeeterConfig.disabled);

    }



    public static final Random random = new Random();

    public static List<Ability> pickNRandom(List<Ability> lst, int n) {
        List<Ability> copy = new ArrayList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    public static List<Ability> pickNRandomForEntity(List<Ability> lst, int n, EntityType<?> entityType){
        List<Ability> copy = new ArrayList<>(lst);
        copy.removeIf(ability -> abilityBlacklist.containsKey(ability.getName()) && abilityBlacklist.get(ability.getName()).contains(entityType));
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);

    }

    public static void addAbility(Ability ability){
        ALL_ABILITIES.add(ability);
        ABILITY_NAMES.put(ability.getName(), ability);
    }

    public static void addBlacklist(String name, List<EntityType<?>> entityTypeList) {
        abilityBlacklist.put(name,entityTypeList);
    }

    public static void removeAbility(Ability ability){
        ALL_ABILITIES.removeIf(abilityElement -> abilityElement.getName().equals(ability.getName()));
    }

    public static void removeAbilityByName(String name){
        ALL_ABILITIES.remove(ABILITY_NAMES.get(name));
    }

    public static void removeDisabledAbilities(){
        for(Ability ability : ABILITY_STATUS.keySet()){
            if(ABILITY_STATUS.get(ability)){
                removeAbility(ability);
            }
        }
    }

}
