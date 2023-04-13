package ru.starfarm.command.parameter.resolver.number;

public class FloatResolver<S> extends NumberParameterResolver<S, Float> {

    public FloatResolver() {
        super();
    }

    public FloatResolver(float minValue) {
        super(minValue);
    }

    public FloatResolver(float minValue, float maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Float parseNumber(String input) throws NumberFormatException {
        return Float.parseFloat(input);
    }

}
