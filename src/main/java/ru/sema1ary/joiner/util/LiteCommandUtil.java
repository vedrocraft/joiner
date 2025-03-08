package ru.sema1ary.joiner.util;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.sema1ary.joiner.command.argument.JoinerMessageArgument;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.service.JoinerMessageService;

@SuppressWarnings("all")
@RequiredArgsConstructor
public class LiteCommandUtil {
    private final JoinerMessageService messageService;

    public LiteCommands<CommandSender> create(Object... commands) {
        return LiteBukkitFactory.builder()
                .settings(settings -> settings
                        .fallbackPrefix("chatroom")
                        .nativePermissions(true)
                )
                .argument(JoinerMessage.class, new JoinerMessageArgument(messageService))
                .commands(commands)
                .message(LiteBukkitMessages.INVALID_USAGE, "&cНеверное использование!")
                .message(LiteBukkitMessages.PLAYER_ONLY, "&cЭта команда только для игроков!")
                .message(LiteBukkitMessages.PLAYER_NOT_FOUND, "&cЭтот игрок не найден.")
                .message(LiteBukkitMessages.MISSING_PERMISSIONS, "&cУ вас нет прав.")
                .schematicGenerator(SchematicFormat.angleBrackets())
                .build();
    }
}
