package kassuk.addon.blackout.modules;

import kassuk.addon.blackout.BlackOut;
import kassuk.addon.blackout.BlackOutModule;
import kassuk.addon.blackout.utils.OLEPOSSUtils;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OLEPOSSU
 */
public class FeetESP extends BlackOutModule {
    public FeetESP() {
        super(BlackOut.BLACKOUT, "Feet ESP", "No, it doesn't show you pictures of feet.");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> friend = sgGeneral.add(new BoolSetting.Builder()
        .name("Friend")
        .description("Renders friends' feet.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> other = sgGeneral.add(new BoolSetting.Builder()
        .name("Other")
        .description("Renders other players' feet.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> self = sgGeneral.add(new BoolSetting.Builder()
        .name("Self")
        .description("Renders own feet.")
        .defaultValue(true)
        .build()
    );
    private final Setting<ShapeMode> shapeMode = sgGeneral.add(new EnumSetting.Builder<ShapeMode>()
        .name("Shape Mode")
        .description("Which parts of feet should be rendered")
        .defaultValue(ShapeMode.Both)
        .build()
    );
    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder()
        .name("Line Color")
        .description("Color of the feet outlines.")
        .defaultValue(new SettingColor(255, 0, 0, 255))
        .build()
    );
    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder()
        .name("Side Color")
        .description("Color of the feet sides.")
        .defaultValue(new SettingColor(255, 0, 0, 50))
        .build()
    );
    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("Range")
        .description("Renders feet inside this range.")
        .defaultValue(25)
        .min(0)
        .sliderRange(0, 25)
        .build()
    );

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onRender(Render3DEvent event) {

        if (mc.player == null || mc.world == null) {return;}

        mc.world.getPlayers().forEach(player -> {
            if (player.distanceTo(mc.player) > range.get()) {return;}

            if (!friend.get() && Friends.get().isFriend(player)) {return;}
            if (!other.get() && player != mc.player && !Friends.get().isFriend(player)) {return;}
            if (!self.get() && mc.player == player) {return;}

            render(event, new Vec3d(
                lerp(mc.getTickDelta(), player.prevX, player.getX()),
                lerp(mc.getTickDelta(), player.prevY, player.getY()),
                lerp(mc.getTickDelta(), player.prevZ, player.getZ())
            ));
        });
    }

    private double lerp(double delta, double min, double max) {
        return min + (max - min) * delta;
    }

    private void render(Render3DEvent event, Vec3d vec) {
        event.renderer.sideHorizontal(vec.x - 0.3, vec.y, vec.z - 0.3, vec.x + 0.3, vec.z + 0.3, sideColor.get(), lineColor.get(), shapeMode.get());
    }
}
