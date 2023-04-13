package ru.starfarm.command.parameter.resolver.literal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.starfarm.command.Command;
import ru.starfarm.command.exception.CommandParseException;
import ru.starfarm.command.parameter.resolver.ParameterResolver;

import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringResolver<S> implements ParameterResolver<S, String> {

    protected int minValue = 0, maxValue = Integer.MAX_VALUE;
    @Accessors(fluent = true, chain = true)
    protected Predicate<Character> validator;

    public StringResolver(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String resolve(Command<S, ?> command, S sender, String input) throws CommandParseException {
        if (input.length() > maxValue)
            throw new CommandParseException(String.format("§cДлинна строки больше допустимой (§e%s§c)", maxValue));
        else if (input.length() < minValue)
            throw new CommandParseException(String.format("§cДлинна строки меньшей допустимой (§e%s§c)", maxValue));
        else if (validator != null) {
            for (char character : input.toCharArray()) {
                if (!validator.test(character))
                    throw new CommandParseException("§cВ строке содержатся запрещенные символы.");
            }
        }
        return input;
    }

}
