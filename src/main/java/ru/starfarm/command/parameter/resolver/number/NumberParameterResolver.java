package ru.starfarm.command.parameter.resolver.number;

import lombok.*;
import ru.starfarm.command.Command;
import ru.starfarm.command.exception.CommandParseException;
import ru.starfarm.command.parameter.resolver.ParameterResolver;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class NumberParameterResolver<S, N extends Number & Comparable<N>> implements ParameterResolver<S, N> {

    protected N minValue = getBaseMinValue(), maxValue = getBaseMaxValue();

    public NumberParameterResolver(N minValue) {
        this.minValue = minValue;
    }

    protected abstract N parseNumber(String input) throws NumberFormatException;

    @Override
    public N resolve(Command<S, ?> command, S sender, String input) throws CommandParseException {
        try {
            val value = parseNumber(input);
            if (value.compareTo(maxValue) > 0)
                throw new CommandParseException(String.format("§cЧисло больше допустимого (§e%s§c)", maxValue));
            else if (value.compareTo(minValue) < 0)
                throw new CommandParseException(String.format("§cЧисло меньше допустимого (§e%s§c)", minValue));
            return value;
        } catch (NumberFormatException exception) {
            throw new CommandParseException("§cНедопустимый формат числа!");
        }
    }

    protected static final Map<Class<? extends Number>, Object> MAX_VALUES = new HashMap<>(), MIN_VALUES = new HashMap<>();

    @SneakyThrows
    @SuppressWarnings("ALL")
    protected N getBaseMaxValue() {
        return getBaseValue("MAX_VALUE", MAX_VALUES);
    }

    @SneakyThrows
    @SuppressWarnings("ALL")
    protected N getBaseMinValue() {
        return getBaseValue("MIN_VALUE", MIN_VALUES);
    }

    @SneakyThrows
    @SuppressWarnings("ALL")
    protected N getBaseValue(String name, Map<Class<? extends Number>, Object> cache) {
        return (N) cache.computeIfAbsent(
                (Class<N>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1],
                clazz -> {
                    try {
                        val field = clazz.getDeclaredField(name);
                        field.setAccessible(true);
                        return field.get(null);
                    } catch (Throwable throwable) {
                        throw new RuntimeException(throwable);
                    }
                }
        );
    }


}
