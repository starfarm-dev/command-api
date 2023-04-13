package ru.starfarm.command.manager;

import ru.starfarm.command.Command;
import ru.starfarm.command.dispatcher.CommandDispatcher;
import ru.starfarm.command.registry.CommandRegistry;

import java.util.List;
import java.util.function.Function;

public interface CommandManager<S, T> {

    CommandRegistry<S, T> getRegistry();

    CommandDispatcher<S, T> getDispatcher();

    Function<String, T> getTextResolver();

    void registerCommand(Command<S, T> command);

    void registerCommands(List<Command<S, T>> commands);

    void registerCommands(Command<S, T>... commands);

    void unregisterCommand(String label);

    void unregisterCommand(Command<S, T> command);

    void unregisterCommands(List<String> labels);

}
