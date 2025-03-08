package ru.sema1ary.joiner.service;

import lombok.NonNull;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import service.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface JoinerMessageService extends Service {
    JoinerMessage save(@NonNull JoinerMessage message);

    Optional<JoinerMessage> findById(Long id);

    Optional<JoinerMessage> findByName(@NonNull String name);

    List<JoinerMessage> findAll();

    void delete(@NonNull JoinerMessage message);

    String getJoinMessage(@NonNull JoinerUser user);

    String getQuitMessage(@NonNull JoinerUser user);

    List<JoinerMessage> getJoinMessages();

    List<JoinerMessage> getQuitMessages();
}
