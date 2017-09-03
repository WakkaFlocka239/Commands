package com.spleefleague.commands.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.registry.WorldData;
import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.LiteralArg;
import com.spleefleague.annotations.StringArg;
import com.spleefleague.core.chat.Theme;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.io.Settings;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import com.spleefleague.core.utils.FlattenedClipboardTransform;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import java.io.*;

/**
 * Created by Josh on 10/08/2016.
 */
public class globalschematic extends BasicCommand {

    public globalschematic(CorePlugin plugin, String name, String usage) {
        super(plugin, new globalschematicDispatcher(), name, usage, Rank.SENIOR_MODERATOR, Rank.BUILDER);
    }

    private boolean checkEnabled(Player p) {
        if (!Settings.getBoolean("global_schematics_enabled").orElse(false) || !Settings.hasKey("global_schematics_save_location")) {
            error(p, "This command is currently disabled!");
            return false;
        }
        return true;
    }

    @Endpoint(target = {PLAYER})
    public void save(Player p, @LiteralArg(value = "save") String l, @StringArg String name) {
        if (!checkEnabled(p)) {
            return;
        }
        if (name.contains(".")) {
            error(p, "Invalid schematic name!");
            return;
        }

        File saveLocation = new File(Settings.getString("global_schematics_save_location").get());
        File saveFile = new File(saveLocation, name + ".schematic");

        ClipboardFormat format = ClipboardFormat.findByAlias("schematic");

        ClipboardHolder holder;
        try {
            holder = WorldEdit.getInstance().getSession(p.getName()).getClipboard();
        } catch (EmptyClipboardException e) {
            error(p, "You need to copy an area first!");
            return;
        }
        Clipboard clipboard = holder.getClipboard();
        Transform transform = holder.getTransform();
        Clipboard target;

        // If we have a transform, bake it into the copy
        if (!transform.isIdentity()) {
            FlattenedClipboardTransform result = FlattenedClipboardTransform.transform(clipboard, transform, holder.getWorldData());
            target = new BlockArrayClipboard(result.getTransformedRegion());
            target.setOrigin(clipboard.getOrigin());
            try {
                Operations.completeLegacy(result.copyTo(target));
            } catch (MaxChangedBlocksException e) {
                error(p, "Too many blocks changed in one session! Please contact a developer.");
                return;
            }
        } else {
            target = clipboard;
        }

        Closer closer = Closer.create();
        try {
            FileOutputStream fos = closer.register(new FileOutputStream(saveFile));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(format.getWriter(bos));
            writer.write(target, holder.getWorldData());
            success(p, "Schematic saved! You can now load it on any server using /globalschematic load " + name + "!");
        } catch (IOException e) {
            error(p, "Unable to write to schematic file - please contact a developer!");
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Endpoint(target = {PLAYER})
    public void load(Player p, @LiteralArg(value = "load") String l, @StringArg String name) {
        if (!checkEnabled(p)) {
            return;
        }
        if (name.contains(".")) {
            error(p, "Invalid schematic name!");
            return;
        }

        File loadLocation = new File(Settings.getString("global_schematics_save_location").get());
        File loadFile = new File(loadLocation, name + ".schematic");

        if (!loadFile.exists()) {
            error(p, "That schematic doesn't seem to exist!");
            return;
        }

        ClipboardFormat format = ClipboardFormat.findByAlias("schematic");

        Closer closer = Closer.create();
        try {
            FileInputStream fis = closer.register(new FileInputStream(loadFile));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            ClipboardReader reader = format.getReader(bis);

            WorldData worldData = BukkitUtil.getLocalWorld(p.getWorld()).getWorldData();
            Clipboard clipboard = reader.read(worldData);
            WorldEdit.getInstance().getSession(p.getName()).setClipboard(new ClipboardHolder(clipboard, worldData));
            success(p, "Schematic loaded - you can now paste it using //paste.");
        } catch (IOException | NullPointerException e) {
            error(p, "The schematic could not be read - please contact a developer!");
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Endpoint(target = {PLAYER})
    public void list(Player p, @LiteralArg(value = "list") String l) {
        if (!checkEnabled(p)) {
            return;
        }
        File schematicLocation = new File(File.separator + "home" + File.separator + "schematics" + File.separator);
        if (!schematicLocation.exists() || !schematicLocation.isDirectory() || schematicLocation.listFiles().length == 0) {
            error(p, "No schematics found!");
            return;
        }
        p.sendMessage(Theme.INFO.buildTheme(true) + "Global schematics:");
        p.sendMessage(Theme.INFO.buildTheme(true) + StringUtils.join(schematicLocation.list(), ", ").replace(".schematic", "") + ".");
    }
}
