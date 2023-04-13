package ru.starfarm.command.require;

import lombok.Getter;
import ru.starfarm.command.Command;

import java.util.function.BiPredicate;

public class FixedCommandRequire<S, T> extends AbstractCommandRequire<S, T> {

    @Getter
    private final BiPredicate<Command<S, T>, S> check;

    public FixedCommandRequire(String message, BiPredicate<Command<S, T>, S> check) {
        super(message);
        this.check = check;
    }

    @Override
    public boolean check(Command<S, T> command, S sender) {
        return check.test(command, sender);
    }

}
