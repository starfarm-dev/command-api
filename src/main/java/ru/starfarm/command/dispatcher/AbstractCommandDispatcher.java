package ru.starfarm.command.dispatcher;

import lombok.*;
import ru.starfarm.command.manager.CommandManager;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractCommandDispatcher<S, T> implements CommandDispatcher<S, T> {

    protected CommandManager<S, T> commandManager;

    @Override
    public final boolean post(S sender, String text) {
        val args = Arrays.stream(text.split(" ")).filter(arg -> !arg.isEmpty()).collect(Collectors.toList());
        if (!args.isEmpty()) {
            val command = commandManager.getRegistry().getCommand(args.get(0));
            if (command != null) {
                args.remove(0);
                post(command, sender, args);
                return true;
            }
        }
        return false;
    }

}
