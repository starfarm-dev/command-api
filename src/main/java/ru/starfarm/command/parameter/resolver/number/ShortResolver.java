package ru.starfarm.command.parameter.resolver.number;

public class ShortResolver<S> extends NumberParameterResolver<S, Short> {

    public ShortResolver() {
        super();
    }

    public ShortResolver(short minValue) {
        super(minValue);
    }

    public ShortResolver(short minValue, short maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Short parseNumber(String input) throws NumberFormatException {
        return Short.parseShort(input);
    }
    
}
