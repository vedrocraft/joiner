package ru.sema1ary.joiner.dao;

import com.j256.ormlite.dao.Dao;
import lombok.NonNull;
import ru.sema1ary.joiner.model.JoinerUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface JoinerUserDao extends Dao<JoinerUser, Long> {
    JoinerUser save(@NonNull JoinerUser user) throws SQLException;

    void saveAll(@NonNull List<JoinerUser> users) throws SQLException;

    Optional<JoinerUser> findById(Long id) throws SQLException;

    Optional<JoinerUser> findByUsername(@NonNull String username) throws SQLException;

    List<JoinerUser> findAll() throws SQLException;
}
