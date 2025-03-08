package ru.sema1ary.joiner.dao;

import com.j256.ormlite.dao.Dao;
import lombok.NonNull;
import ru.sema1ary.joiner.model.message.JoinerMessage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface JoinerMessageDao extends Dao<JoinerMessage, Long> {
    JoinerMessage save(@NonNull JoinerMessage message) throws SQLException;

    Optional<JoinerMessage> findById(Long id) throws SQLException;

    Optional<JoinerMessage> findByName(@NonNull String name) throws SQLException;

    List<JoinerMessage> findAll() throws SQLException;
}
