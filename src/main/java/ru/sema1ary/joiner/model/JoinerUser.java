package ru.sema1ary.joiner.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sema1ary.joiner.dao.impl.JoinerUserDaoImpl;
import ru.sema1ary.joiner.model.message.JoinerMessage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "players", daoClass = JoinerUserDaoImpl.class)
public class JoinerUser {
    @DatabaseField(unique = true, generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String username;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "join_message")
    private JoinerMessage joinMessage;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "quit_message")
    private JoinerMessage quitMessage;
}
