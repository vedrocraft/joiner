package ru.sema1ary.joiner.listener;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;

@RequiredArgsConstructor
public class QuitListener implements Listener {
    private final MiniMessage miniMessage;
    private final JoinerUserService userService;
    private final JoinerMessageService messageService;

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        JoinerUser user = userService.getUser(player.getName());

        event.quitMessage(miniMessage.deserialize(
                PlaceholderAPI.setPlaceholders(player, messageService.getQuitMessage(user))
        ));
    }
}
