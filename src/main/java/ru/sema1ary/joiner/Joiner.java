package ru.sema1ary.joiner;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sema1ary.joiner.command.JoinerCommand;
import ru.sema1ary.joiner.command.argument.JoinerMessageArgument;
import ru.sema1ary.joiner.listener.JoinListener;
import ru.sema1ary.joiner.listener.PreJoinListener;
import ru.sema1ary.joiner.listener.QuitListener;
import ru.sema1ary.joiner.model.JoinerUser;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;
import ru.sema1ary.joiner.service.impl.JoinerMessageServiceImpl;
import ru.sema1ary.joiner.service.impl.JoinerUserServiceImpl;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DaoFinder;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceGetter;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Joiner extends JavaPlugin implements DaoFinder, ServiceGetter {
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

        LiteCommandBuilder.builder()
                .argument(JoinerMessage.class, new JoinerMessageArgument(ServiceManager.getService(
                        JoinerMessageService.class)))
                .commands(new JoinerCommand(miniMessage, ServiceManager.getService(ConfigService.class),
                        ServiceManager.getService(JoinerUserService.class),
                        ServiceManager.getService(JoinerMessageService.class)))
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true, connectionSource);
    }

    @SneakyThrows
    private void initConnectionSource() {
        if(ServiceManager.getService(ConfigService.class).get("sql-use")) {
            connectionSource = ConnectionSourceUtil.connectSQL(
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

        connectionSource = ConnectionSourceUtil.connectNoSQLDatabase(databaseFilePath.toString(),
                JoinerUser.class, JoinerMessage.class);
    }

}
