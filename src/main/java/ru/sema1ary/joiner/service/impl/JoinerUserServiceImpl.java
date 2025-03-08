package ru.sema1ary.joiner.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.sema1ary.joiner.dao.JoinerUserDao;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JoinerUserServiceImpl implements JoinerUserService {
    private final MiniMessage miniMessage;
    private final JoinerUserDao joinerUserDao;
    private final JoinerMessageService messageService;

    @Override
    public JoinerUser save(@NonNull JoinerUser user) {
        try {
            return joinerUserDao.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(@NonNull List<JoinerUser> users) {
        try {
            joinerUserDao.saveAll(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<JoinerUser> findById(Long id) {
        try {
            return joinerUserDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<JoinerUser> findByUsername(@NonNull String username) {
        try {
            return joinerUserDao.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JoinerUser> findAll() {
        try {
            return joinerUserDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JoinerUser getUser(@NonNull String username) {
        return findByUsername(username).orElseGet(() -> save(JoinerUser.builder()
                .username(username)
                .build()));
    }

    @Override
    public void sendFakeJoinMessage(@NonNull Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer ->
                onlinePlayer.sendMessage(miniMessage.deserialize(
                        PlaceholderAPI.setPlaceholders(player, messageService.getJoinMessage(getUser(player.getName())))
                )));
    }

    @Override
    public void sendFakeQuitMessage(@NonNull Player player) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer ->
                onlinePlayer.sendMessage(miniMessage.deserialize(
                        PlaceholderAPI.setPlaceholders(player, messageService.getQuitMessage(getUser(player.getName())))
                )));
    }
}
