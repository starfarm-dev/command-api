package ru.starfarm.command.dispatcher;

import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

import java.util.List;

public class SimpleCommandDispatcher<S, T> extends AbstractCommandDispatcher<S, T> {

    public SimpleCommandDispatcher(CommandManager<S, T> commandManager) {
        super(commandManager);
    }

    public SimpleCommandDispatcher() {
        super();
    }

    @Override
    public void post(Command<S, T> command, S sender, List<String> args) {
        command.execute(sender, args);
    }

}
