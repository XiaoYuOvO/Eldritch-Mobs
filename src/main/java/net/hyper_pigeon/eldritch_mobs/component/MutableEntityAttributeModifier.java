package net.hyper_pigeon.eldritch_mobs.component;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.UUID;

public class MutableEntityAttributeModifier extends EntityAttributeModifier {
    private static final Logger LOGGER = LogUtils.getLogger();
    private double value;
    public MutableEntityAttributeModifier(String pName, double pValue, Operation pOperation) {
        super(pName, pValue, pOperation);
        this.value = pValue;
    }


    @Override
    public double getValue() {
        return value;
    }

    public NbtCompound toNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("Name", this.getName());
        nbtCompound.putDouble("Amount", this.value);
        nbtCompound.putInt("Operation", this.getOperation().getId());
        nbtCompound.putUuid("UUID", this.getId());
        return nbtCompound;
    }

    public String toString() {
        double var10000 = this.value;
        return "AttributeModifier{amount=" + var10000 + ", operation=" + this.getOperation() + ", name='" + this.getName() + "', id=" + this.getId() + "}";
    }


//    @Nullable
//    public static EntityAttributeModifier fromNbt(NbtCompound nbt) {
//        try {
//            UUID uUID = nbt.getUuid("UUID");
//            Operation operation = EntityAttributeModifier.Operation.fromId(nbt.getInt("Operation"));
//            return new EntityAttributeModifier(uUID, nbt.getString("Name"), nbt.getDouble("Amount"), operation);
//        } catch (Exception var3) {
//            LOGGER.warn("Unable to create attribute: {}", var3.getMessage());
//            return null;
//        }
//    }

    public void setValue(double value) {
        this.value = value;
    }
}

