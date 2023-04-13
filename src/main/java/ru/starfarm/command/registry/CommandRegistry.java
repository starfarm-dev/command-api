package ru.starfarm.command.registry;

import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

import java.util.Map;
import java.util.Set;

public interface CommandRegistry<S, T> {

    Set<Command<S, T>> getCommands();

    Map<String, Command<S, T>> getAliasCommandMap();

    CommandManager<S, T> getCommandManager();

    boolean isEmpty();

    void setCommandManager(CommandManager<S, T> commandManager);

    void registerCommand(Command<S, T> command);

    void unregisterCommand(Command<S, T> command);

    void unregisterCommand(String label);

    boolean isRegistered(Command<S, T> command);

    boolean isRegistered(String name);

    Command<S, T> getCommand(String name);

}
