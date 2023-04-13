package ru.starfarm.command.parameter.resolver.number;

public class LongResolver<S> extends NumberParameterResolver<S, Long> {

    public LongResolver() {
        super();
    }

    public LongResolver(long minValue) {
        super(minValue);
    }

    public LongResolver(long minValue, long maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Long parseNumber(String input) throws NumberFormatException {
        return Long.parseLong(input);
    }
    
}
