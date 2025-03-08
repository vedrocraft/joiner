package ru.sema1ary.joiner.listener;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;
import ru.vidoskim.bukkit.service.ConfigService;

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
            player.sendMessage(miniMessage.deserialize(configService.get("welcome-text")));
        }

        JoinerUser user = userService.getUser(player.getName());
        event.joinMessage(miniMessage.deserialize(
                PlaceholderAPI.setPlaceholders(player, messageService.getJoinMessage(user))
        ));
    }
}
