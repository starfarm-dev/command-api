package ru.starfarm.command.registry;

import lombok.*;
import ru.starfarm.command.Command;
import ru.starfarm.command.manager.CommandManager;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCommandRegistry<S, T> implements CommandRegistry<S, T> {

    protected CommandManager<S, T> commandManager;

    protected final Set<Command<S, T>> commands = Collections.newSetFromMap(new ConcurrentHashMap<>());
    protected final Map<String, Command<S, T>> aliasCommandMap = new ConcurrentHashMap<>();

    @Override
    public void registerCommand(Command<S, T> command) {
        if (command.getCommandManager() != null)
            throw new IllegalStateException("Command already registered on " + command.getCommandManager());

        aliasCommandMap.put(command.getName().toLowerCase(), command);
        command.getAliases().forEach(alias -> aliasCommandMap.put(alias.toLowerCase(), command));
        command.setCommandManager(commandManager);
        command.getRegistry().setCommandManager(commandManager);
    }

    @Override
    public void unregisterCommand(Command<S, T> command) {
        if (command.getCommandManager() != commandManager)
            throw new IllegalStateException("Command registered in different registry");

        aliasCommandMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == command)
                .map(Map.Entry::getKey)
                .forEach(aliasCommandMap::remove);

        commands.remove(command);
    }

    @Override
    public void unregisterCommand(String label) {
        unregisterCommand(getCommand(label));
    }

    @Override
    public boolean isRegistered(Command<S, T> command) {
        return commands.contains(command);
    }

    @Override
    public boolean isRegistered(String name) {
        return aliasCommandMap.containsKey(name.toLowerCase());
    }

    @Override
    public Command<S, T> getCommand(String name) {
        return aliasCommandMap.get(name.toLowerCase());
    }

    @Override
    public boolean isEmpty() {
        return commands.isEmpty() || aliasCommandMap.isEmpty();
    }

}
