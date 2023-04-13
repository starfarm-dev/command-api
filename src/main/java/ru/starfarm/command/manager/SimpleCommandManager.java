package ru.starfarm.command.manager;

import lombok.Data;
import ru.starfarm.command.Command;
import ru.starfarm.command.dispatcher.CommandDispatcher;
import ru.starfarm.command.registry.CommandRegistry;

import java.util.List;
import java.util.function.Function;

@Data
public class SimpleCommandManager<S, T> implements CommandManager<S, T> {

    public static final Function<String, String> NOOP_TEXT_RESOLVER = it -> it;

    protected final CommandRegistry<S, T> registry;
    protected final CommandDispatcher<S, T> dispatcher;

    protected final Function<String, T> textResolver;

    public SimpleCommandManager(
            CommandRegistry<S, T> registry, CommandDispatcher<S, T> dispatcher, Function<String, T> textResolver
    ) {
        this.registry = registry;
        this.dispatcher = dispatcher;
        this.textResolver = textResolver;

        registry.setCommandManager(this);
        dispatcher.setCommandManager(this);
    }

    @Override
    public void registerCommand(Command<S, T> command) {
        registry.registerCommand(command);
    }

    @Override
    public void registerCommands(List<Command<S, T>> commands) {
        commands.forEach(this::registerCommand);
    }

    @Override
    public void registerCommands(Command<S, T>... commands) {
        for (Command<S, T> command : commands) {
            registerCommand(command);
        }
    }

    @Override
    public void unregisterCommand(String label) {
        registry.unregisterCommand(label);
    }

    @Override
    public void unregisterCommand(Command<S, T> command) {
        registry.unregisterCommand(command);
    }

    @Override
    public void unregisterCommands(List<String> labels) {
        labels.forEach(this::unregisterCommand);
    }

}
