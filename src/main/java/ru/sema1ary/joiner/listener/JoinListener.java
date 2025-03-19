package ru.sema1ary.joiner.listener;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
public class JoinListener implements Listener {
    private final MiniMessage miniMessage;
    private final ConfigService configService;
    private final JoinerUserService userService;
    private final JoinerMessageService messageService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(configService.get("enable-welcome-text")) {
            PlayerUtil.sendMessage(player, (String) configService.get("welcome-text"));
        }

        JoinerUser user = userService.getUser(player.getName());
        event.joinMessage(null);

        Bukkit.getServer().sendMessage(miniMessage.deserialize(
                PlaceholderAPI.setPlaceholders(player, messageService.getJoinMessage(user))
        ));
    }
}
