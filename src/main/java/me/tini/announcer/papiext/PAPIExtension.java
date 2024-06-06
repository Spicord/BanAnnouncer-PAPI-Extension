package me.tini.announcer.papiext;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.tini.announcer.BanAnnouncerPlugin;
import me.tini.announcer.PunishmentInfo;
import me.tini.announcer.extension.AbstractExtension;

public class PAPIExtension extends AbstractExtension {

    public PAPIExtension(BanAnnouncerPlugin plugin) {
    }

    @Override
    public String processPlaceholder(PunishmentInfo info, String placeholder) {
        if (!placeholder.startsWith("papi_")) {
            return null;
        }
        placeholder = placeholder.substring("papi_".length());

        final UUID playerId = getPlayerId(info);
        final String likelyPlayerName = info.getPlayer();

        OfflinePlayer offlinePlayer = getPlayer(playerId, likelyPlayerName);

        final String placeholderSym = "%" + placeholder + "%";

        if (offlinePlayer instanceof Player) {
            return PlaceholderAPI.setPlaceholders((Player) offlinePlayer, placeholderSym);
        } else {
            return PlaceholderAPI.setPlaceholders(offlinePlayer, placeholderSym);
        }
    }

    @SuppressWarnings("deprecation")
    private OfflinePlayer getPlayer(UUID playerId, String likelyPlayerName) {
        final OfflinePlayer player;

        if (playerId != null) {
            player = Bukkit.getServer().getOfflinePlayer(playerId);
        } else {
            player = Bukkit.getServer().getOfflinePlayer(likelyPlayerName);
        }

        if (player != null && player.hasPlayedBefore()) {
            return player;
        }

        return null;
    }

    private UUID getPlayerId(PunishmentInfo action) {
        String playerIdString = action.getPlayerId();

        if (playerIdString == null) {
            return null;
        }

        try {
            UUID playerId = UUID.fromString(playerIdString);

            if (playerId.getLeastSignificantBits() == 0 && playerId.getMostSignificantBits() == 0) {
                return null;
            }

            return playerId;
        } catch (IllegalArgumentException e) {/* invalid id */}

        return null;
    }
}
