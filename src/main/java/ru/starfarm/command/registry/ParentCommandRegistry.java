package ru.starfarm.command.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

@Getter
@RequiredArgsConstructor
public class ParentCommandRegistry<S, T> extends SimpleCommandRegistry<S, T> {

    protected final Command<S, T> parent;

    @Override
    public void registerCommand(Command<S, T> command) {
        super.registerCommand(command);
        command.setParent(parent);
        command.setCommandManager(parent.getCommandManager());
    }

    @Override
    public void unregisterCommand(String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregisterCommand(Command<S, T> command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCommandManager(CommandManager<S, T> commandManager) {
        super.setCommandManager(commandManager);
        aliasCommandMap.values().forEach(command -> command.setCommandManager(commandManager));
    }

}
