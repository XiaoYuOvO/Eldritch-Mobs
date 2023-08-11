package net.hyper_pigeon.eldritch_mobs.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.mixin.registry.sync.AccessorRegistry;
import net.hyper_pigeon.eldritch_mobs.EldritchMobsMod;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankLevel;
import net.minecraft.command.argument.*;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Optional;

public class SummonRankedCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.summon_ranked.failed"));
    private static final SimpleCommandExceptionType FAILED_UUID_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.summon_ranked.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("commands.summon_ranked.invalidPosition"));
    private static final DynamicCommandExceptionType INVALID_LEVEL_EXCEPTION = new DynamicCommandExceptionType((id) -> new TranslatableText("commands.summon_ranked.invalidRank", id));

    public SummonRankedCommand(){

    }
    private static <T> RegistryPredicateArgumentType.RegistryPredicate<T> getPredicate(CommandContext<ServerCommandSource> context, String name, RegistryKey<Registry<T>> registryRef) throws CommandSyntaxException {
        RegistryPredicateArgumentType.RegistryPredicate<?> registryPredicate = (RegistryPredicateArgumentType.RegistryPredicate)context.getArgument(name, RegistryPredicateArgumentType.RegistryPredicate.class);
        Optional<RegistryPredicateArgumentType.RegistryPredicate<T>> optional = registryPredicate.tryCast(registryRef);
        return optional.orElseThrow(() -> INVALID_LEVEL_EXCEPTION.create(registryPredicate));
    }

    private static RegistryPredicateArgumentType.RegistryPredicate<MobRankLevel> getMobRankLevel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return getPredicate(context, "level", ((AccessorRegistry<MobRankLevel>)EldritchMobsMod.MOB_RANK_LEVELS).getRegistryKey());
    }


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("summon_ranked")
                .requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("level", RegistryPredicateArgumentType.registryPredicate(EldritchMobsMod.MOB_RANK_LEVELS.getKey()))
                        .then((CommandManager.argument("entity", EntitySummonArgumentType.entitySummon())
                                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .executes((commandContext) -> execute(commandContext.getSource(),
                                        getMobRankLevel(commandContext),
                                        EntitySummonArgumentType.getEntitySummon(commandContext, "entity"),
                                        commandContext.getSource().getPosition(),
                                        new NbtCompound(),
                                        true))).
                                then((CommandManager.argument("pos", Vec3ArgumentType.vec3())
                                        .executes((commandContext) -> execute(commandContext.getSource(),
                                                getMobRankLevel(commandContext),
                                                EntitySummonArgumentType.getEntitySummon(commandContext, "entity"),
                                                Vec3ArgumentType.getVec3(commandContext, "pos"),
                                                new NbtCompound(),
                                                true))).
                                        then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                                                .executes((commandContext) -> execute(commandContext.getSource(),
                                                        getMobRankLevel(commandContext),
                                                        EntitySummonArgumentType.getEntitySummon(commandContext, "entity"),
                                                        Vec3ArgumentType.getVec3(commandContext, "pos"),
                                                        NbtCompoundArgumentType.getNbtCompound(commandContext, "nbt"),
                                                        false)))
                                )
                        )));
    }

    private static int execute(ServerCommandSource source, RegistryPredicateArgumentType.RegistryPredicate<MobRankLevel> rankLevel, Identifier entity, Vec3d pos, NbtCompound nbt, boolean initialize) throws CommandSyntaxException {

        BlockPos blockPos = new BlockPos(pos);
        if (!World.isValid(blockPos)) {
            throw INVALID_POSITION_EXCEPTION.create();
        } else {
            NbtCompound nbtCompound = nbt.copy();
            nbtCompound.putString("id", entity.toString());
            ServerWorld serverWorld = source.getWorld();
            Entity entity2 = EntityType.loadEntityWithPassengers(nbtCompound, serverWorld, (entityx) -> {
                entityx.refreshPositionAndAngles(pos.x, pos.y, pos.z, entityx.getYaw(), entityx.getPitch());
                return entityx;
            });
            if (entity2 == null) {
                throw FAILED_EXCEPTION.create();
            } else {
                if (initialize && entity2 instanceof MobEntity) {
                    ((MobEntity)entity2).initialize(source.getWorld(), source.getWorld().getLocalDifficulty(entity2.getBlockPos()), SpawnReason.COMMAND,
                            null,
                            null);
                    if(EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).getModifiers() != null) {
                        EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).setRank(MobRankLevel.NONE);
                        EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).clearModifiers();
                        entity2.setCustomName(null);
                    }
                    rankLevel.getKey().ifLeft(registryKey -> {
                        EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).setRank(EldritchMobsMod.MOB_RANK_LEVELS.get(registryKey));
                        EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).randomlySetModifiers();
                        EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).setTitle();
                    });
                    //EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).increaseHealth();
                    //EldritchMobsMod.ELDRITCH_MODIFIERS.get(entity2).setTitle();
                }

                if (!serverWorld.spawnNewEntityAndPassengers(entity2)) {
                    throw FAILED_UUID_EXCEPTION.create();
                } else {
                    source.sendFeedback(new TranslatableText("commands.summon_ranked.success",
                            entity2.getDisplayName()), true);
                    return 1;
                }
            }
        }


    }
}
