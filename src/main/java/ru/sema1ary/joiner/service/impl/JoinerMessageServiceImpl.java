package ru.sema1ary.joiner.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.sema1ary.joiner.dao.JoinerMessageDao;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.model.message.MessageType;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.vidoskim.bukkit.service.ConfigService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JoinerMessageServiceImpl implements JoinerMessageService {
    private final JoinerMessageDao joinerMessageDao;
    private final ConfigService configService;

    @Override
    public JoinerMessage save(@NonNull JoinerMessage message) {
        try {
            return joinerMessageDao.save(message);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<JoinerMessage> findById(Long id) {
        try {
            return joinerMessageDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<JoinerMessage> findByName(@NonNull String name) {
        try {
            return joinerMessageDao.findByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<JoinerMessage> findAll() {
        try {
            return joinerMessageDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NonNull JoinerMessage message) {
        try {
            joinerMessageDao.delete(message);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getJoinMessage(@NonNull JoinerUser user) {
        if(user.getJoinMessage() == null) {
            return configService.get("default-join-message");
        }

        return user.getJoinMessage().getText();
    }

    @Override
    public String getQuitMessage(@NonNull JoinerUser user) {
        if(user.getQuitMessage() == null) {
            return configService.get("default-quit-message");
        }

        return user.getQuitMessage().getText();
    }

    @Override
    public List<JoinerMessage> getJoinMessages() {
        return findAll().stream().filter(joinerMessage -> joinerMessage.getType().equals(MessageType.JOIN)).toList();
    }

    @Override
    public List<JoinerMessage> getQuitMessages() {
        return findAll().stream().filter(joinerMessage -> joinerMessage.getType().equals(MessageType.QUIT)).toList();
    }
}
