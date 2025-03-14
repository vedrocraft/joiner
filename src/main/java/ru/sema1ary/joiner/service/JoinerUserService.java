package ru.sema1ary.joiner.service;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.vedrocraftapi.service.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface JoinerUserService extends Service {
    JoinerUser save(@NonNull JoinerUser user);

    void saveAll(@NonNull List<JoinerUser> users);

    Optional<JoinerUser> findById(Long id);

    Optional<JoinerUser> findByUsername(@NonNull String username);

    List<JoinerUser> findAll();

    JoinerUser getUser(@NonNull String username);

    void sendFakeJoinMessage(@NonNull Player target, @NonNull Player player);

    void sendFakeQuitMessage(@NonNull Player target, @NonNull Player player);
}
