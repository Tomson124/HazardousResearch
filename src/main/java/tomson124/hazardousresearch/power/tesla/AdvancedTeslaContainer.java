package tomson124.hazardousresearch.power.tesla;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import tomson124.hazardousresearch.config.HazardousResearchCoreConfig;
import tomson124.hazardousresearch.tile.base.TileEntityPowerAcceptor;

public class AdvancedTeslaContainer implements ITeslaConsumer, ITeslaHolder, ITeslaProducer {

    public TileEntityPowerAcceptor tile;

    public AdvancedTeslaContainer(TileEntityPowerAcceptor tile) {
        this.tile = tile;
    }

    public AdvancedTeslaContainer(NBTBase nbt, TileEntityPowerAcceptor tile) {
        this.tile = tile;
        this.readNBT(nbt);
    }

    public long getStoredPower() {
        return (long)tile.getEnergy() / HazardousResearchCoreConfig.euPerRF;
    }

    //Receive
    public long givePower(long tesla, boolean simulated) {
        return (long) (tile.addEnergy(tesla / HazardousResearchCoreConfig.euPerRF) * HazardousResearchCoreConfig.euPerRF);
    }

    //Take power out
    public long takePower(long tesla,boolean simulated) {
        return (int)(tile.useEnergy(tesla / HazardousResearchCoreConfig.euPerRF) * HazardousResearchCoreConfig.euPerRF);
    }

    public long getCapacity() {
        return (long)tile.getMaxPower() / HazardousResearchCoreConfig.euPerRF;
    }

    public long getInputRate() {
        return (long)tile.getMaxInput() / HazardousResearchCoreConfig.euPerRF;
    }

    public long getOutputRate() {
        return (long)tile.getMaxOutput()/ HazardousResearchCoreConfig.euPerRF;
    }

    public NBTBase writeNBT() {
        NBTTagCompound dataTag = new NBTTagCompound();
        return dataTag;
    }

    public void readNBT(NBTBase nbt) {
    }

    public boolean isInputSide() {
        return true;
    }

    public boolean isOutputSide() {
        return true;
    }

}
