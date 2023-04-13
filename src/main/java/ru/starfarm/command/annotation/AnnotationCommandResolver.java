package ru.starfarm.command.annotation;

import lombok.experimental.UtilityClass;
import lombok.val;
import ru.starfarm.command.Command;
import ru.starfarm.command.context.CommandContext;
import ru.starfarm.command.exception.CommandException;
import ru.starfarm.command.exception.CommandParseException;

import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.function.Consumer;

@UtilityClass
public class AnnotationCommandResolver {

    public void resolveMeta(AnnotatedElement element, Command<?, ?> command) {
        val metaAnnotation = element.getDeclaredAnnotation(CommandMeta.class);
        if (metaAnnotation != null) {
            command.setName(metaAnnotation.value());
            command.setAliases(Arrays.asList(metaAnnotation.aliases()));
            if (!metaAnnotation.prefix().isEmpty()) command.setPrefix(metaAnnotation.prefix());
            command.setInheritPrefix(metaAnnotation.inheritPrefix());
            Arrays.stream(metaAnnotation.commands())
                    .map(commandClass -> {
                        try {
                            return (Command) commandClass.newInstance();
                        } catch (Throwable throwable) {
                            throw new CommandException("Error while resolving sub-command " + commandClass, throwable);
                        }
                    })
                    .forEach(command::addCommand);
        }
    }

    public void resolveCommands(Class<?> commandClass, Command<?, ?> command) {
        Arrays.stream(commandClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(CommandMeta.class))
                .filter(method -> method.getParameterCount() == 1
                        && CommandContext.class.isAssignableFrom(method.getParameterTypes()[0]))
                .filter(method -> !method.getName().equals("execute"))
                .forEach(method -> {
                    try {
                        val executor = MethodHandleProxies.asInterfaceInstance(
                                Consumer.class,
                                MethodHandles.lookup().unreflect(method).bindTo(command)
                        );
                        val meta = method.getDeclaredAnnotation(CommandMeta.class);
                        val subCommand = command.addCommand(executor, meta.value(), meta.aliases());
                        resolveMeta(method, subCommand);
                    } catch (Throwable throwable) {
                        throw new CommandParseException("Error while resolving method sub-command " + method, throwable);
                    }
                });
    }

}
