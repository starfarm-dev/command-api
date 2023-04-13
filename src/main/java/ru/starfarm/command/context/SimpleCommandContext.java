package ru.starfarm.command.context;

import lombok.Data;
import lombok.val;
import ru.starfarm.command.Command;

import java.util.List;
import java.util.function.Supplier;

@Data
public class SimpleCommandContext<S, T> implements CommandContext<S, T> {

    protected final S sender;
    protected final Command<S, T> command;
    protected final List<String> originalArguments;
    protected final List<Object> arguments;

    @Override
    public <A> A getArgument(int index) {
        return hasArgument(index) ? (A) arguments.get(index) : null;
    }

    @Override
    public <A> A getArgument(int index, A ifNull) {
        val argument = this.<A>getArgument(index);
        return argument == null ? ifNull : argument;
    }

    @Override
    public <A> A getArgument(int index, Supplier<A> ifNullLazy) {
        val argument = this.<A>getArgument(index);
        return argument == null ? ifNullLazy.get() : argument;
    }

    @Override
    public boolean hasArgument(int index) {
        return !arguments.isEmpty() && index >= 0 && index < arguments.size();
    }

    @Override
    public void sendHelp() {
        command.sendHelp(sender);
    }

    @Override
    public void sendMessage(String message, Object... parameters) {
        command.sendMessage(sender, message, parameters);
    }

    @Override
    public void sendMessage(T... message) {
        command.sendMessage(sender, message);
    }

    @Override
    public void sendMessage(List<T> message) {
        command.sendMessage(sender, message);
    }

}
