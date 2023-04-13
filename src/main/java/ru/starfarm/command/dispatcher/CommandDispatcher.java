package ru.starfarm.command.dispatcher;

import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

import java.util.List;

public interface CommandDispatcher<S, T> {

    void setCommandManager(CommandManager<S, T> commandManager);

    CommandManager<S, T> getCommandManager();

    boolean post(S sender, String text);

    void post(Command<S, T> command, S sender, List<String> arguments);

}
