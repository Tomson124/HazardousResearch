package tomson124.hazardousresearch.power.tesla;

import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import tomson124.hazardousresearch.config.HazardousResearchCoreConfig;
import tomson124.hazardousresearch.tile.base.TileEntityPowerAcceptor;

public class TeslaPowerManager implements ITeslaPowerManager {

    AdvancedTeslaContainer container;

    @Override
    public void readFromNBT(NBTTagCompound compound, TileEntityPowerAcceptor powerAcceptor) {
        this.container = new AdvancedTeslaContainer(compound.getTag("TeslaContainer"), powerAcceptor);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound, TileEntityPowerAcceptor powerAcceptor) {
        compound.setTag("TeslaContainer", this.container.writeNBT());
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, TileEntityPowerAcceptor powerAcceptor) {
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER && powerAcceptor.canAcceptEnergy(facing)){
            this.container.tile = powerAcceptor;
            return (T) this.container;
        } else if (capability == TeslaCapabilities.CAPABILITY_PRODUCER && powerAcceptor.canProvideEnergy(facing)){
            this.container.tile = powerAcceptor;
            return (T) this.container;
        }
        if (capability == TeslaCapabilities.CAPABILITY_HOLDER){
            this.container.tile = powerAcceptor;
            return (T) this.container;
        }
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, TileEntityPowerAcceptor powerAcceptor) {
        if(capability == TeslaCapabilities.CAPABILITY_CONSUMER && powerAcceptor.canAcceptEnergy(facing)){
            return true;
        } else if (capability == TeslaCapabilities.CAPABILITY_PRODUCER && powerAcceptor.canProvideEnergy(facing)){
            return true;
        }
        if (capability == TeslaCapabilities.CAPABILITY_HOLDER)
            return true;
        return false;
    }

    @Override
    public void update(TileEntityPowerAcceptor powerAcceptor) {
        if (powerAcceptor.canProvideEnergy(null)) {
            //TeslaUtils.distributePowerEqually(powerAcceptor.getWorld(), powerAcceptor.getPos(), (long) powerAcceptor.getMaxOutput(), false);
        }
    }

    @Override
    public void created(TileEntityPowerAcceptor powerAcceptor) {
        this.container = new AdvancedTeslaContainer(powerAcceptor);
    }

    @Override
    public String getDisplayableTeslaCount(long tesla) {
        return TeslaUtils.getDisplayableTeslaCount(tesla / HazardousResearchCoreConfig.euPerRF);
    }

    public static ITeslaPowerManager getPowerManager() {
        return new TeslaPowerManager();
    }
}
