package net.hyper_pigeon.eldritch_mobs.component.interfaces;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.hyper_pigeon.eldritch_mobs.ability.Ability;
import net.hyper_pigeon.eldritch_mobs.rank.MobRankLevel;
import net.minecraft.entity.boss.ServerBossBar;

import java.util.List;

public interface ModifierComponent extends Component, ServerTickingComponent {

    void randomlySetRank();
    void randomlySetModifiers();
    List<Ability> getModifiers();
    void clearModifiers();
    void setTitle();
    MobRankLevel getRank();
    void setRank(MobRankLevel mobRank);
    void increaseHealth();
    ServerBossBar getBossBar();
}
