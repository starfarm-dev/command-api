package ru.starfarm.command.require;

import lombok.Data;
import ru.starfarm.command.Command;

@Data
public abstract class AbstractCommandRequire<S, T> implements CommandRequire<S, T> {

    private final String message;

    @Override
    public void notify(Command<S, T> command, S sender) {
        command.sendMessage(sender, message);
    }

}
