package ru.starfarm.command;

import lombok.Data;
import lombok.val;
import ru.starfarm.command.annotation.AnnotationCommandResolver;
import ru.starfarm.command.context.CommandContext;
import ru.starfarm.command.context.SimpleCommandContext;
import ru.starfarm.command.exception.CommandException;
import ru.starfarm.command.exception.CommandExecuteException;
import ru.starfarm.command.exception.CommandParseException;
import ru.starfarm.command.manager.CommandManager;
import ru.starfarm.command.parameter.CommandParameter;
import ru.starfarm.command.parameter.resolver.ParameterResolver;
import ru.starfarm.command.registry.CommandRegistry;
import ru.starfarm.command.registry.ParentCommandRegistry;
import ru.starfarm.command.require.CommandRequire;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Data
public abstract class Command<S, T> {

//    protected Class<? extends S> senderClass =
//            (Class<? extends S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    //meta
    protected String name, description = "Какая-то команда", prefix = "";
    protected boolean inheritPrefix = true;
    protected List<String> aliases;

    //base
    protected final List<CommandParameter<S, ?>> parameters = new LinkedList<>();
    protected final List<CommandRequire<S, T>> requires = new LinkedList<>();

    protected Command<S, T> parent;
    protected final CommandRegistry<S, T> registry = new ParentCommandRegistry<>(this);

    protected CommandManager<S, T> commandManager;

    public Command() {
        try {
            AnnotationCommandResolver.resolveMeta(getClass(), this);
            AnnotationCommandResolver.resolveCommands(getClass(), this);
        } catch (Throwable throwable) {
            throw new CommandException("Error while resolving command annotations", throwable);
        }
    }

    public Command(String name, List<String> aliases) {
        this();
        this.name = name;
        this.aliases = aliases;
    }

    public Command(String name, String... aliases) {
        this(name, Arrays.asList(aliases));
    }

    protected abstract void execute(CommandContext<S, T> ctx) throws CommandExecuteException;

    public final void execute(S sender, List<String> arguments) {
        for (CommandRequire<S, T> require : requires) {
            if (!require.check(this, sender)) {
                require.notify(this, sender);
                return;
            }
        }

        if (!arguments.isEmpty()) {
            val command = registry.getCommand(arguments.get(0));
            if (command != null) {
                arguments.remove(0);
                command.execute(sender, arguments);
                return;
            }
        }

        try {
            val parsedArguments = new ArrayList<>();

            for (int i = 0; i < parameters.size(); i++) {
                val parameter = parameters.get(i);
                if (arguments.size() - 1 < i) {
                    if (parameter.isRequired())
                        throw new CommandParseException(String.format("§cУкажите параметр §e%s", parameter.getName()));
                    else continue;
                }
                parsedArguments.add(parameter.getResolver().resolve(this, sender, arguments.get(i)));
            }

            execute(new SimpleCommandContext<>(sender, this, arguments, parsedArguments));
        } catch (CommandException exception) {
            sendMessage(sender, exception.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            sendMessage(sender, "§cОшибка выполнения команды, обратитесь к разработчику!");
        }
    }

    public void setCommandManager(CommandManager<S, T> commandManager) {
        this.commandManager = commandManager;
        registry.setCommandManager(commandManager);
    }

    public void addRequire(CommandRequire<S, T> require) {
        requires.add(require);
    }

    public <P> CommandParameter<S, P> addParameter(String name, ParameterResolver<S, P> resolver, boolean required) {
        if (required && !parameters.isEmpty() && !parameters.get(parameters.size() - 1).isRequired())
            throw new IllegalStateException("Adding required parameter after non-required");

        val parameter = new CommandParameter<>(name, resolver, required);
        parameters.add(parameter);
        return parameter;
    }

    public <P> CommandParameter<S, P> addParameter(String name, ParameterResolver<S, P> resolver) {
        return addParameter(name, resolver, true);
    }

    public void addCommand(Command<S, T> command) {
        registry.registerCommand(command);
    }

    public Command<S, T> addCommand(Consumer<CommandContext<S, T>> executor, String name, String... aliases) {
        val command = new Command<S, T>(name, aliases) {
            @Override
            protected void execute(CommandContext<S, T> ctx) throws CommandExecuteException {
                executor.accept(ctx);
            }

            @Override
            protected void sendMessageFinal(S sender, List<T> message) {
                Command.this.sendMessageFinal(sender, message);
            }
        };
        addCommand(command);
        return command;
    }


    public String getPrefix() {
        return inheritPrefix && parent != null
                ? parent.getPrefix()
                : prefix != null ? prefix : "";
    }

    public void sendHelp(S sender) {
        val message = new StringBuilder();
        val prefix = getPrefix();
        message.append(String.format("§aПомощь по команде /%s:", getHelpName()));

        if (registry.isEmpty()) message.append(prefix).append(getHelpLine(sender));
        else registry.getCommands().stream()
                .filter(command -> command.isAllowedFor(sender))
                .forEach(command -> message.append('\n').append(prefix).append(command.getHelpLine(sender)));
        sendMessage(sender, message.toString());
    }

    public String getHelpLine(S sender) {
        return String.format("§r/%s %s§7- %s",
                getHelpName(),
                registry.isEmpty() ? getHelpParameters() : (String.format(
                        "§7[§a%s§7]",
                        registry.getCommands()
                                .stream()
                                .filter(entry -> entry.isAllowedFor(sender))
                                .map(Command::getName)
                                .collect(Collectors.joining("/"))
                ) + (registry.isEmpty() ? "" : " ")),
                description
        );
    }

    protected String getHelpName() {
        return parent != null ? parent.getHelpName() + " " + name : name;
    }

    protected String getHelpParameters() {
        return getParameters()
                .stream()
                .map(parameter -> String.format(parameter.getFormat(), parameter.getName()))
                .collect(Collectors.joining(" ")) + (getParameters().isEmpty() ? "" : " ");
    }

    public boolean isAllowedFor(S sender) {
        return requires.stream().allMatch(require -> require.check(this, sender));
    }

    public final void sendMessage(S sender, String message, Object... parameters) {
        sendMessage(sender, commandManager.getTextResolver().apply(
                String.format(message, parameters)
        ));
    }

    public final void sendMessage(S sender, T... message) {
        sendMessage(sender, Arrays.asList(message));
    }

    public final void sendMessage(S sender, List<T> message) {
        if (!getPrefix().isEmpty()) {
            val list = new ArrayList<T>();
            list.add(commandManager.getTextResolver().apply(getPrefix()));
            list.addAll(message);
            sendMessageFinal(sender, list);
        } else sendMessageFinal(sender, message);
    }

    protected abstract void sendMessageFinal(S sender, List<T> message);

}
