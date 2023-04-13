package ru.starfarm.command.parameter.resolver.literal;

import ru.starfarm.command.Command;
import ru.starfarm.command.exception.CommandParseException;
import ru.starfarm.command.parameter.resolver.ParameterResolver;

public class BooleanResolver<S> implements ParameterResolver<S, Boolean> {

    @Override
    public Boolean resolve(Command<S, ?> command, S sender, String input) throws CommandParseException {
        return input.equalsIgnoreCase("1")
                || input.equalsIgnoreCase("yes")
                || Boolean.parseBoolean(input);
    }

}
