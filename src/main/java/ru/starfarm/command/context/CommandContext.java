package ru.starfarm.command.context;

import ru.starfarm.command.Command;

import java.util.List;
import java.util.function.Supplier;

public interface CommandContext<S, T> {

    S getSender();

    Command<S, T> getCommand();

    List<String> getOriginalArguments();

    List<Object> getArguments();

    <A> A getArgument(int index);

    <A> A getArgument(int index, A ifNull);

    <A> A getArgument(int index, Supplier<A> ifNullLazy);

    boolean hasArgument(int index);

    void sendHelp();

    void sendMessage(String message, Object... parameters);

    void sendMessage(T... message);

    void sendMessage(List<T> message);

}
