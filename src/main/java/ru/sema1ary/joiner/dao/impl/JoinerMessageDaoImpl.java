package ru.sema1ary.joiner.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.joiner.dao.JoinerMessageDao;
import ru.sema1ary.joiner.model.message.JoinerMessage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JoinerMessageDaoImpl extends BaseDaoImpl<JoinerMessage, Long> implements JoinerMessageDao {
    public JoinerMessageDaoImpl(ConnectionSource connectionSource, Class<JoinerMessage> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public JoinerMessage save(@NonNull JoinerMessage message) throws SQLException {
        if (message.getUsers() == null) {
            message.setUsers(getEmptyForeignCollection("users"));
        }
        createOrUpdate(message);
        return message;
    }

    @Override
    public Optional<JoinerMessage> findById(Long id) throws SQLException {
        JoinerMessage result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<JoinerMessage> findByName(@NonNull String username) throws SQLException {
        QueryBuilder<JoinerMessage, Long> queryBuilder = queryBuilder();
        Where<JoinerMessage, Long> where = queryBuilder.where();
        String columnName = "name";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<JoinerMessage> findAll() throws SQLException {
        return queryForAll();
    }
}
