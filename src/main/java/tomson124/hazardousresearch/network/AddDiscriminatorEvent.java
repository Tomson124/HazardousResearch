package tomson124.hazardousresearch.network;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 *  Copied from Crystek: https://github.com/CrystekTeam/Crystek/blob/master/src/main/java/crystekteam/crystek/network/AddDiscriminatorEvent.java
 */

public class AddDiscriminatorEvent extends Event
{
    public PacketHandler packetHandler;

    public AddDiscriminatorEvent(PacketHandler packetHandler)
    {
        this.packetHandler = packetHandler;
    }

    public PacketHandler getPacketHandler()
    {
        return packetHandler;
    }

    @Override
    public boolean isCancelable()
    {
        return false;
    }
}
