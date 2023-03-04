package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ValueTypeSerializer extends StdSerializer<ValueType<?>> {
    public ValueTypeSerializer() {
        this(null);
    }

    public ValueTypeSerializer(Class<ValueType<?>> t) {
        super(t);
    }

    @Override
    public void serialize(ValueType valueType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        serializerProvider.defaultSerializeValue(valueType.getValue(), jsonGenerator);
    }

}
