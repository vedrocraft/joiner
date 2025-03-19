package ru.sema1ary.joiner.service;

import lombok.NonNull;
import org.bukkit.entity.Player;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.vedrocraftapi.service.UserService;

@SuppressWarnings("unused")
public interface JoinerUserService extends UserService<JoinerUser> {
    void sendFakeJoinMessage(@NonNull Player target, @NonNull Player player);

    void sendFakeQuitMessage(@NonNull Player target, @NonNull Player player);
}
