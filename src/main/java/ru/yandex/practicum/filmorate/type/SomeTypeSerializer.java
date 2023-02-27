package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class SomeTypeSerializer extends StdSerializer<SomeType<?>> {
    public SomeTypeSerializer() {
        this(null);
    }
    public SomeTypeSerializer(Class<SomeType<?>> t) {
        super(t);
    }

    @Override
    public void serialize(SomeType someType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        serializerProvider.defaultSerializeValue(someType.getValue(), jsonGenerator);
    }

}
