package ru.starfarm.command.dispatcher;

import lombok.Getter;
import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncCommandDispatcher<S, T> extends SimpleCommandDispatcher<S, T> {

    @Getter
    protected final Executor executor;

    public AsyncCommandDispatcher(CommandManager<S, T> commandManager, Executor executor) {
        super(commandManager);
        this.executor = executor;
    }

    public AsyncCommandDispatcher(CommandManager<S, T> commandManager) {
        this(commandManager, Executors.newSingleThreadExecutor());
    }

    public AsyncCommandDispatcher() {
        this(null, Executors.newSingleThreadExecutor());
    }

    @Override
    public void post(Command<S, T> command, S sender, List<String> args) {
        executor.execute(() -> super.post(command, sender, args));
    }

}
