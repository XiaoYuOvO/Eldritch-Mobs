package net.hyper_pigeon.eldritch_mobs.util;

import java.util.List;
import java.util.Random;

public class NormalSampler<T> {
    private final Random random;
    private final int range;
    private final T failItem;
    private final List<T> itemList;

    public NormalSampler(List<T> itemList, T failItem, Random random, int range) {
        this.itemList = itemList;
        this.failItem = failItem;
        this.random = random;
        this.range = range;
    }

    public int samplerSize(){
        return itemList.size();
    }

    public T sample(int centerIndex, double failChance) {
        int startIndex = Math.max(0, centerIndex - range / 2);
        int endIndex = Math.min(itemList.size() - 1, centerIndex + range / 2);
        double stdDev = (endIndex - startIndex) / (float)range;
        if (random.nextDouble() < failChance) {
            return this.failItem;
        }
        double sampleValue = centerIndex + stdDev * random.nextGaussian();
        int sampledIndex = (int) Math.round(sampleValue);
        sampledIndex = Math.max(startIndex, Math.min(endIndex, sampledIndex));
        return itemList.get(sampledIndex);
    }
}
