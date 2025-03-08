package ru.sema1ary.joiner;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import ormlite.ConnectionSourceUtil;
import ormlite.DaoFinder;
import ru.sema1ary.joiner.command.JoinerCommand;
import ru.sema1ary.joiner.listener.JoinListener;
import ru.sema1ary.joiner.listener.PreJoinListener;
import ru.sema1ary.joiner.listener.QuitListener;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;
import ru.sema1ary.joiner.service.impl.JoinerMessageServiceImpl;
import ru.sema1ary.joiner.service.impl.JoinerUserServiceImpl;
import ru.sema1ary.joiner.util.LiteCommandUtil;
import ru.vidoskim.bukkit.service.ConfigService;
import ru.vidoskim.bukkit.service.impl.ConfigServiceImpl;
import service.ServiceManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Joiner extends JavaPlugin implements DaoFinder {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private JdbcPooledConnectionSource connectionSource;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        initConnectionSource();

        ServiceManager.registerService(JoinerMessageService.class, new JoinerMessageServiceImpl(
                getDao(connectionSource, JoinerMessage.class),
                ServiceManager.getService(ConfigService.class)));
        ServiceManager.registerService(JoinerUserService.class, new JoinerUserServiceImpl(miniMessage,
                getDao(connectionSource, JoinerUser.class), ServiceManager.getService(JoinerMessageService.class)));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                ServiceManager.getService(JoinerUserService.class)), this);
        getServer().getPluginManager().registerEvents(new JoinListener(miniMessage,
                ServiceManager.getService(ConfigService.class),
                ServiceManager.getService(JoinerUserService.class),
                ServiceManager.getService(JoinerMessageService.class)), this);
        getServer().getPluginManager().registerEvents(new QuitListener(miniMessage,
                ServiceManager.getService(JoinerUserService.class),
                ServiceManager.getService(JoinerMessageService.class)), this);

        new LiteCommandUtil(ServiceManager.getService(JoinerMessageService.class))
                .create(new JoinerCommand(miniMessage, ServiceManager.getService(ConfigService.class),
                ServiceManager.getService(JoinerUserService.class),
                ServiceManager.getService(JoinerMessageService.class)));
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true, connectionSource);
    }

    @SneakyThrows
    private void initConnectionSource() {
        if(ServiceManager.getService(ConfigService.class).get("sql-use")) {
            connectionSource = ConnectionSourceUtil.connectSQLDatabaseWithoutSSL(
                    ServiceManager.getService(ConfigService.class).get("sql-driver"),
                    ServiceManager.getService(ConfigService.class).get("sql-host"),
                    ServiceManager.getService(ConfigService.class).get("sql-database"),
                    ServiceManager.getService(ConfigService.class).get("sql-user"),
                    ServiceManager.getService(ConfigService.class).get("sql-password"),
                    JoinerUser.class, JoinerMessage.class);
            return;
        }

        Path databaseFilePath = Paths.get("plugins/joiner/database.sqlite");
        if(!Files.exists(databaseFilePath) && !databaseFilePath.toFile().createNewFile()) {
            return;
        }

        connectionSource = ConnectionSourceUtil.connectNoSQLDatabase("sqlite",
                databaseFilePath.toString(), JoinerUser.class, JoinerMessage.class);
    }

}
