package net.hyper_pigeon.eldritch_mobs;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.hyper_pigeon.eldritch_mobs.ability.Ability;
import net.hyper_pigeon.eldritch_mobs.ability.AbilityHelper;
import net.hyper_pigeon.eldritch_mobs.component.interfaces.ModifierComponent;
import net.hyper_pigeon.eldritch_mobs.config.EldritchMobsConfig;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankCategory;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankLevel;
import net.hyper_pigeon.eldritch_mobs.register.EldritchMobsBlocks;
import net.hyper_pigeon.eldritch_mobs.register.EldritchMobsCommands;
import net.hyper_pigeon.eldritch_mobs.register.EldritchMobsDataRegistry;
import net.hyper_pigeon.eldritch_mobs.register.EldritchMobsEventListeners;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EldritchMobsMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("eldritch_mobs");

	public static final EldritchMobsConfig ELDRITCH_MOBS_CONFIG = AutoConfig.register(EldritchMobsConfig.class, JanksonConfigSerializer::new).getConfig();

	public static final ComponentKey<ModifierComponent> ELDRITCH_MODIFIERS =
			ComponentRegistry.getOrCreate(new Identifier("eldritch_mobs:eldritch_modifiers"), ModifierComponent.class);
	public static final Registry<MobRankLevel> MOB_RANK_LEVELS = FabricRegistryBuilder.createSimple(MobRankLevel.class, new Identifier("eldritch_mobs","mob_ranks")).buildAndRegister();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Channelling eldritch energies...");
		EldritchMobsEventListeners.init();
		EldritchMobsCommands.init();
		EldritchMobsDataRegistry.init();
		EldritchMobsBlocks.init();
		MobRankLevel.registerRanks();
		AbilityHelper.removeDisabledAbilities();
	}

	public static List<Ability> getModifiers(ComponentProvider componentProvider){
		return ELDRITCH_MODIFIERS.get(componentProvider).getModifiers();
	}

	public static MobRankLevel getLevel(ComponentProvider componentProvider){
		return ELDRITCH_MODIFIERS.get(componentProvider).getRank();
	}

}
