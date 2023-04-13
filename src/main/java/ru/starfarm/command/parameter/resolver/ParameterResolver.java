package ru.starfarm.command.parameter.resolver;

import ru.starfarm.command.Command;
import ru.starfarm.command.exception.CommandParseException;

public interface ParameterResolver<S, T> {

    T resolve(Command<S, ?> command, S sender, String input) throws CommandParseException;

}
