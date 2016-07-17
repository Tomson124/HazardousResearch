package tomson124.hazardousresearch.power.tesla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import tomson124.hazardousresearch.tile.base.TileEntityPowerAcceptor;

public interface ITeslaPowerManager {

    void readFromNBT (NBTTagCompound compound, TileEntityPowerAcceptor powerAcceptor);

    void writeToNBT (NBTTagCompound compound, TileEntityPowerAcceptor powerAcceptor);

    <T> T getCapability (Capability<T> capability, EnumFacing facing, TileEntityPowerAcceptor powerAcceptor);

    boolean hasCapability (Capability<?> capability, EnumFacing facing, TileEntityPowerAcceptor powerAcceptor);

    void update(TileEntityPowerAcceptor acceptor);

    void created(TileEntityPowerAcceptor acceptor);

    String getDisplayableTeslaCount (long tesla);
}
