package ru.starfarm.command.parameter.resolver.number;

public class IntResolver<S> extends NumberParameterResolver<S, Integer> {

    public IntResolver() {
        super();
    }

    public IntResolver(int minValue) {
        super(minValue);
    }

    public IntResolver(int minValue, int maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Integer parseNumber(String input) throws NumberFormatException {
        return Integer.parseInt(input);
    }
    
}
