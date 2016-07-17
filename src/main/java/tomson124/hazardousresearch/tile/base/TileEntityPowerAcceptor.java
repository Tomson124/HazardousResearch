package tomson124.hazardousresearch.tile.base;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.*;
import ic2.api.info.Info;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;
import tomson124.hazardousresearch.api.power.IEnergyInterfaceTile;
import tomson124.hazardousresearch.api.IListInfoProvider;
import tomson124.hazardousresearch.api.power.IPowerConfig;
import tomson124.hazardousresearch.config.HazardousResearchCoreConfig;
import tomson124.hazardousresearch.power.PowerSystem;
import tomson124.hazardousresearch.power.tesla.TeslaManager;

import java.util.List;

@Optional.InterfaceList(value = {@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2")})
public abstract class TileEntityPowerAcceptor extends TileEntityRFProvider implements IEnergyReceiver, IEnergyProvider, // Cofh
        IEnergyInterfaceTile, IListInfoProvider, // TechReborn
        IEnergyTile, IEnergySink, IEnergySource // IC2
{
    public int tier;
    private double energy;

    public TileEntityPowerAcceptor(int tier) {
        this.tier = tier;
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            TeslaManager.manager.created(this);
        }
    }

    // IC2

    protected boolean addedToEnet;

    @Override
    @Optional.Method(modid = "IC2")
    public void update() {
        super.update();
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            TeslaManager.manager.update(this);
        }
        onLoaded();
    }

    @Optional.Method(modid = "IC2")
    public void onLoaded() {
        if (getPowerConfig().eu() && !addedToEnet &&
                !FMLCommonHandler.instance().getEffectiveSide().isClient() &&
                Info.isIc2Available()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

            addedToEnet = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        onChunkUnload();
    }

    @Override
    @Optional.Method(modid = "IC2")
    public void onChunkUnload() {
        super.onChunkUnload();
        if (getPowerConfig().eu()) {
            if (addedToEnet && Info.isIc2Available()) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

                addedToEnet = false;
            }
        }
    }

    @Override
    @Optional.Method(modid = "IC2")
    public double getDemandedEnergy() {
        if (!HazardousResearchCoreConfig.getHazardousResearchPower().eu())
            return 0;
        return Math.min(getMaxPower() - getEnergy(), getMaxInput());
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int getSinkTier() {
        return tier;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        setEnergy(getEnergy() + amount);
        return 0;
    }

    @Override
    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
        if (!HazardousResearchCoreConfig.getHazardousResearchPower().eu())
            return false;
        return canAcceptEnergy(enumFacing);
    }

    @Override
    @Optional.Method(modid = "IC2")
    public boolean emitsEnergyTo(IEnergyAcceptor iEnergyAcceptor, EnumFacing enumFacing) {
        if (!HazardousResearchCoreConfig.getHazardousResearchPower().eu())
            return false;
        return canProvideEnergy(enumFacing);
    }

    @Override
    @Optional.Method(modid = "IC2")
    public double getOfferedEnergy() {
        if (!HazardousResearchCoreConfig.getHazardousResearchPower().eu())
            return 0;
        return Math.min(getEnergy(), getMaxOutput());
    }

    @Override
    @Optional.Method(modid = "IC2")
    public void drawEnergy(double amount) {
        useEnergy((int) amount);
    }

    @Override
    @Optional.Method(modid = "IC2")
    public int getSourceTier() {
        return tier;
    }
    // END IC2

    // COFH
    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        if (!getPowerConfig().rf())
            return false;
        return canAcceptEnergy(from) || canProvideEnergy(from);
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (!getPowerConfig().rf())
            return 0;
        if (!canAcceptEnergy(from)) {
            return 0;
        }
        maxReceive *= HazardousResearchCoreConfig.euPerRF;
        int energyReceived = Math.min(getMaxEnergyStored(null) - getEnergyStored(null),
                Math.min((int) this.getMaxInput() * HazardousResearchCoreConfig.euPerRF, maxReceive));

        if (!simulate) {
            setEnergy(getEnergy() + energyReceived);
        }
        return energyReceived / HazardousResearchCoreConfig.euPerRF;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        if (!getPowerConfig().rf())
            return 0;
        return ((int) getEnergy() / HazardousResearchCoreConfig.euPerRF);
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        if (!getPowerConfig().rf())
            return 0;
        return ((int) getMaxPower() / HazardousResearchCoreConfig.euPerRF);
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (!getPowerConfig().rf())
            return 0;
        if (!canProvideEnergy(from)) {
            return 0;
        }
        maxExtract *= HazardousResearchCoreConfig.euPerRF;
        int energyExtracted = Math.min(getEnergyStored(null), Math.min(maxExtract, maxExtract));

        if (!simulate) {
            setEnergy(energy - energyExtracted);
        }
        return energyExtracted / HazardousResearchCoreConfig.euPerRF;
    }
    // END COFH

    // TechReborn

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(double energy) {
        this.energy = energy;

        if (this.getEnergy() > getMaxPower()) {
            this.setEnergy(getMaxPower());
        } else if (this.energy < 0) {
            this.setEnergy(0);
        }
    }

    @Override
    public double addEnergy(double energy) {
        return addEnergy(energy, false);
    }

    @Override
    public double addEnergy(double energy, boolean simulate) {
        double energyReceived = Math.min(getMaxPower(), Math.min(this.getMaxPower(), energy));

        if (!simulate) {
            setEnergy(getEnergy() + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public boolean canUseEnergy(double input) {
        return input <= energy;
    }

    @Override
    public double useEnergy(double energy) {
        return useEnergy(energy, false);
    }

    @Override
    public double useEnergy(double extract, boolean simulate) {
        if (extract > energy) {
            double tempEnergy = energy;
            setEnergy(0);
            return tempEnergy;
        }
        if (!simulate) {
            setEnergy(energy - extract);
        }
        return extract;
    }

    @Override
    public boolean canAddEnergy(double energy) {
        return this.energy + energy <= getMaxPower();
    }
    // TechReborn END

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagCompound data = tag.getCompoundTag("TilePowerAcceptor");
        energy = data.getDouble("energy");
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            TeslaManager.manager.readFromNBT(tag, this);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", energy);
        tag.setTag("TilePowerAcceptor", data);
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            TeslaManager.manager.writeToNBT(tag, this);
        }
        return tag;
    }

    public void readFromNBTWithoutCoords(NBTTagCompound tag) {
        NBTTagCompound data = tag.getCompoundTag("TilePowerAcceptor");
        energy = data.getDouble("energy");
    }

    public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tag) {
        NBTTagCompound data = new NBTTagCompound();
        data.setDouble("energy", energy);
        tag.setTag("TilePowerAcceptor", data);
        return tag;
    }

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        info.add(TextFormatting.LIGHT_PURPLE + "Energy buffer Size " + TextFormatting.GREEN
                + PowerSystem.getLocaliszedPower(getMaxPower()));
        if (getMaxInput() != 0) {
            info.add(TextFormatting.LIGHT_PURPLE + "Max Input " + TextFormatting.GREEN
                    + PowerSystem.getLocaliszedPower(getMaxInput()));
        }
        if (getMaxOutput() != 0) {
            info.add(TextFormatting.LIGHT_PURPLE + "Max Output " + TextFormatting.GREEN
                    + PowerSystem.getLocaliszedPower(getMaxOutput()));
        }
        // if(isRealTile){ //TODO sync to client
        // info.add(TextFormatting.LIGHT_PURPLE + "Stored energy " +
        // TextFormatting.GREEN + getEUString(energy));
        // }
    }

    public double getFreeSpace() {
        return getMaxPower() - energy;
    }


    public void charge(int slot) {
        // TODO rewrite to use built in power system
        // if(getStackInSlot(slot) != null)
        // {
        // if(getStackInSlot(slot).getItem() instanceof IElectricItem)
        // {
        // if(getEnergy() != getMaxPower())
        // {
        // ItemStack stack = inventory.getStackInSlot(slot);
        // double MaxCharge = ((IElectricItem)
        // stack.getItem()).getMaxCharge(stack);
        // double CurrentCharge = ElectricItem.manager.getCharge(stack);
        // if (CurrentCharge != 0)
        // {
        // ElectricItem.manager.discharge(stack, 5, 4, false, false, false);
        // addEnergy(5);
        // }
        // }
        // }
        // }
    }

    public int getEnergyScaled(int scale) {
        return (int) ((energy * scale / getMaxPower()));
    }

    public IPowerConfig getPowerConfig() {
        return HazardousResearchCoreConfig.getHazardousResearchPower();
    }


    //Tesla

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            return TeslaManager.manager.hasCapability(capability, facing, this);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (TeslaManager.isTeslaEnabled(getPowerConfig())) {
            return TeslaManager.manager.getCapability(capability, facing, this);
        }
        return super.getCapability(capability, facing);
    }


//End Tesla
}
