package ru.sema1ary.joiner;

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
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DatabaseUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Joiner extends JavaPlugin implements BaseCommons {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        DatabaseUtil.initConnectionSource(
                this,
                getService(ConfigService.class),
                JoinerUser.class, JoinerMessage.class);

        ServiceManager.registerService(JoinerMessageService.class, new JoinerMessageServiceImpl(
                getDao(JoinerMessage.class),
                getService(ConfigService.class)));
        ServiceManager.registerService(JoinerUserService.class, new JoinerUserServiceImpl(
                getDao(JoinerUser.class),
                getService(JoinerMessageService.class)));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                getService(JoinerUserService.class)
        ), this);

        getServer().getPluginManager().registerEvents(new JoinListener(miniMessage,
                getService(ConfigService.class),
                getService(JoinerUserService.class),
                getService(JoinerMessageService.class)
        ), this);

        getServer().getPluginManager().registerEvents(new QuitListener(miniMessage,
                getService(JoinerUserService.class),
                getService(JoinerMessageService.class)
        ), this);

        LiteCommandBuilder.builder()
                .argument(JoinerMessage.class, new JoinerMessageArgument(
                        getService(JoinerMessageService.class))
                )
                .commands(new JoinerCommand(
                        getService(ConfigService.class),
                        getService(JoinerUserService.class),
                        getService(JoinerMessageService.class))
                )
                .build();
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);
    }
}
