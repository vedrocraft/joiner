package ru.sema1ary.joiner.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.model.message.MessageType;
import ru.sema1ary.joiner.service.JoinerMessageService;
import ru.sema1ary.joiner.service.JoinerUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "joiner")
public class JoinerCommand {
    private final ConfigService configService;
    private final JoinerUserService userService;
    private final JoinerMessageService messageService;

    @Async
    @Execute(name = "reload")
    @Permission("joiner.reload")
    void reload(@Context CommandSender sender) {
        configService.reload();
        PlayerUtil.sendMessage(sender, (String) configService.get("reload-message"));
    }

    @Async
    @Execute(name = "create", aliases = {"add"})
    @Permission("joiner.create")
    void create(@Context CommandSender sender, @Arg("название") String name, @Arg("тип") MessageType type,
        @Join("текст") String text) {

        if(messageService.findByName(name).isPresent()) {
            PlayerUtil.sendMessage(sender, (String) configService.get("error-message-already-exists-message"));
            return;
        }

        messageService.save(JoinerMessage.builder()
                .name(name)
                .type(type)
                .text(text)
                .build());

        PlayerUtil.sendMessage(sender, (String) configService.get("successful-message-creation-message"));
    }

    @Async
    @Execute(name = "delete", aliases = {"remove", "del"})
    @Permission("joiner.delete")
    void delete(@Context CommandSender sender, @Arg("сообщение") JoinerMessage message) {
        messageService.delete(message);
        PlayerUtil.sendMessage(sender, (String) configService.get("successful-message-delete-message"));
    }
}
