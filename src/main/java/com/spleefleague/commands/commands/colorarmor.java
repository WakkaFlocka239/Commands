package com.spleefleague.commands.commands;

import static com.spleefleague.annotations.CommandSource.PLAYER;
import com.spleefleague.annotations.Endpoint;
import com.spleefleague.annotations.IntArg;
import com.spleefleague.commands.command.BasicCommand;
import com.spleefleague.core.player.Rank;
import com.spleefleague.core.plugin.CorePlugin;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class colorarmor extends BasicCommand {

    public colorarmor(CorePlugin plugin, String name, String usage) {
        super(plugin, new colorarmorDispatcher(), name, usage, Rank.SENIOR_MODERATOR);
    }
    
    @Endpoint(target = {PLAYER})
    public void color(Player p, @IntArg(min = 0, max = 255) int r, @IntArg(min = 0, max = 255) int g, @IntArg(min = 0, max = 255) int b) {
        ItemStack is = p.getInventory().getItemInMainHand();
        if (is == null || !isLeatherArmor(is)) {
            error(p, "You must be holding a leather armor piece");
            return;
        }
        LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
        meta.setColor(Color.fromRGB(r, g, b));
        is.setItemMeta(meta);
        success(p, "Color applied");
    }

    private Material[] armors = new Material[] {
            Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS
    };

    public boolean isLeatherArmor(ItemStack is) {
        for (int i = 0; i < armors.length; i++) {
            if (is.getType() == armors[i]) {
                return true;
            }
        }
        return false;
    }

}
