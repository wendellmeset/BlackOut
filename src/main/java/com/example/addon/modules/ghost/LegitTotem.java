package com.example.addon.modules.ghost;

import com.example.addon.BlackOut;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;

/*
Made by KassuK
*/

public class LegitTotem extends Module {

    public LegitTotem() {
        super(BlackOut.GHOST, "LegitTotem", "More legit autototem");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTick(TickEvent.Pre event) {
        if (mc.player != null && mc.world != null) {
            FindItemResult result = InvUtils.find(Items.TOTEM_OF_UNDYING);
            int totems = result.count();
            if (mc.currentScreen != null) {
                if (totems != 0 && mc.currentScreen instanceof InventoryScreen) {
                    if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                        InvUtils.move().from(result.slot()).toOffhand();
                    }
                }
            }
        }
    }
}
