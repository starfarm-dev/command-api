package ru.starfarm.command.annotation;

import ru.starfarm.command.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CommandMeta {

    String value();

    String[] aliases() default {};

    String prefix() default "";

    boolean inheritPrefix() default true;

    Class<? extends Command<?, ?>>[] commands() default {};

}
