package net.hyper_pigeon.eldritch_mobs.persistent_state;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Iterator;

public class SoothingLanternPersistentState extends PersistentState {

    private final HashMap<String, ChunkPos> soothingLanternChunks = new HashMap<>();
    public final String key;

    public SoothingLanternPersistentState(String key) {
        super();
        this.key = key;
    }

    public SoothingLanternPersistentState(){
        this("SoothingLanternChunks");
    }

    public static SoothingLanternPersistentState readNbt(NbtCompound tag) {

        SoothingLanternPersistentState soothingLanternPersistentState = new SoothingLanternPersistentState();
        NbtCompound compoundTag = tag.getCompound("contents");

        for (String string : compoundTag.getKeys()) {
            soothingLanternPersistentState.soothingLanternChunks.put(string, new ChunkPos(compoundTag.getLong(string)));
        }
        soothingLanternPersistentState.markDirty();

        return soothingLanternPersistentState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtCompound compoundTag = new NbtCompound();

        this.soothingLanternChunks.forEach((string, chunkPos) -> {
            compoundTag.putLong(string,chunkPos.toLong());
        });
        tag.put("contents", compoundTag);
        return tag;
    }

    public void addChunkPos(ChunkPos chunkPos, BlockPos blockPos){
        soothingLanternChunks.put(blockPos.toString(),chunkPos);
        this.markDirty();
    }

    public void removeChunkPos(BlockPos pos){
        if(soothingLanternChunks.containsKey(pos.toString())) {
            soothingLanternChunks.remove(pos.toString());
            this.markDirty();
        }

    }

    public boolean containsChunk(ChunkPos chunkPos){
        return soothingLanternChunks.containsValue(chunkPos);
    }

    public int getSize(){
        return soothingLanternChunks.size();
    }

    public static SoothingLanternPersistentState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(SoothingLanternPersistentState::readNbt,SoothingLanternPersistentState::new, "SoothingLanternChunks");
    }

    public void printSoothingLanternChunks(){
        for (String blockPosString : soothingLanternChunks.keySet()){
            System.out.println(soothingLanternChunks.get(blockPosString));
        }
    }

    public ChunkPos getChunkPos(ChunkPos chunkPos){
        for (String blockPosString : soothingLanternChunks.keySet()){
            if(chunkPos.equals(soothingLanternChunks.get(blockPosString))){
                return chunkPos;
            }
        }
        return null;
    }
}
