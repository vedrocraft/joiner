package ru.sema1ary.joiner.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.joiner.dao.JoinerUserDao;
import ru.sema1ary.joiner.model.JoinerUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class JoinerUserDaoImpl extends BaseDaoImpl<JoinerUser, Long> implements JoinerUserDao {
    public JoinerUserDaoImpl(ConnectionSource connectionSource, Class<JoinerUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public JoinerUser save(@NonNull JoinerUser user) throws SQLException {
        createOrUpdate(user);
        return user;
    }

    @Override
    public void saveAll(@NonNull List<JoinerUser> users) throws SQLException {
        callBatchTasks((Callable<Void>) () -> {
            for (JoinerUser user : users) {
                createOrUpdate(user);
            }
            return null;
        });
    }

    @Override
    public Optional<JoinerUser> findById(Long id) throws SQLException {
        JoinerUser result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<JoinerUser> findByUsername(@NonNull String username) throws SQLException {
        QueryBuilder<JoinerUser, Long> queryBuilder = queryBuilder();
        Where<JoinerUser, Long> where = queryBuilder.where();
        String columnName = "username";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<JoinerUser> findAll() throws SQLException {
        return queryForAll();
    }
}
