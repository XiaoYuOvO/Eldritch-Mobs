package net.hyper_pigeon.eldritch_mobs.register;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.hyper_pigeon.eldritch_mobs.command.SummonRankedCommand;

public class EldritchMobsCommands {

    public static void init(){
//        CommandRegistrationCallback.EVENT.register(SummonEliteCommand::register);
//        CommandRegistrationCallback.EVENT.register(SummonUltraCommand::register);
        CommandRegistrationCallback.EVENT.register(SummonRankedCommand::register);
    }


}
