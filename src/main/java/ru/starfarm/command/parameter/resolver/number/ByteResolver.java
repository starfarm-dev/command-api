package ru.starfarm.command.parameter.resolver.number;

public class ByteResolver<S> extends NumberParameterResolver<S, Byte> {

    public ByteResolver() {
        super();
    }

    public ByteResolver(byte minValue) {
        super(minValue);
    }

    public ByteResolver(byte minValue, byte maxValue) {
        super(minValue, maxValue);
    }

    @Override
    protected Byte parseNumber(String input) throws NumberFormatException {
        return Byte.parseByte(input);
    }
    
}
