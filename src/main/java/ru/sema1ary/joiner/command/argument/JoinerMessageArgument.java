package ru.sema1ary.joiner.command.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.sema1ary.joiner.model.message.JoinerMessage;
import ru.sema1ary.joiner.service.JoinerMessageService;

import java.util.Optional;

@RequiredArgsConstructor
public class JoinerMessageArgument extends ArgumentResolver<CommandSender, JoinerMessage> {
    private final JoinerMessageService messageService;

    @Override
    protected ParseResult<JoinerMessage> parse(Invocation<CommandSender> invocation, Argument<JoinerMessage> argument, String s) {
        Optional<JoinerMessage> optionalMessage = messageService.findByName(s);

        return optionalMessage.map(ParseResult::success).orElseGet(() -> ParseResult.failure("Message not found"));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<JoinerMessage> argument, SuggestionContext context) {
        return messageService.findAll().stream().map(JoinerMessage::getName).collect(SuggestionResult.collector());
    }
}
