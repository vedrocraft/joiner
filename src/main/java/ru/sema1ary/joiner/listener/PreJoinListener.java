package ru.sema1ary.joiner.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.service.JoinerUserService;

@RequiredArgsConstructor
public class PreJoinListener implements Listener {
    private final JoinerUserService userService;

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        String username = event.getName();

        if(username.isEmpty()) {
            return;
        }

        if(userService.findByUsername(username).isEmpty()) {
            userService.save(JoinerUser.builder()
                    .username(username)
                    .build());
        }
    }
}
