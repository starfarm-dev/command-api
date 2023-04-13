package ru.starfarm.command.parameter.resolver.literal;

import ru.starfarm.command.Command;
import ru.starfarm.command.exception.CommandParseException;
import ru.starfarm.command.parameter.resolver.ParameterResolver;

import java.util.UUID;

public class UUIDResolver<S> implements ParameterResolver<S, UUID> {

    @Override
    public UUID resolve(Command<S, ?> command, S sender, String input) throws CommandParseException {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException exception) {
            throw new CommandParseException("§cНедопустимый формат UUID");
        }
    }

}
