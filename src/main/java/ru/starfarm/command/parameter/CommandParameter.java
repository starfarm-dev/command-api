package ru.starfarm.command.parameter;

import lombok.Data;
import ru.starfarm.command.parameter.resolver.ParameterResolver;

@Data
public class CommandParameter<S, T> {

    protected final String name;
    protected final ParameterResolver<S, T> resolver;
    protected final boolean required;

    public String getFormat() {
        return required ? "§7<§a%s§7>" : "§7[§a%s§7]";
    }

}
