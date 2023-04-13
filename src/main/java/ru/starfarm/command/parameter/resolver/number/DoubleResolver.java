package ru.starfarm.command.parameter.resolver.number;

public class DoubleResolver<S> extends NumberParameterResolver<S, Double> {

    public DoubleResolver() {
        super();
    }

    public DoubleResolver(double minValue) {
        super(minValue);
    }

    public DoubleResolver(double minValue, double maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Double parseNumber(String input) throws NumberFormatException {
        return Double.parseDouble(input);
    }

}
