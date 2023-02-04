package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class AbstractTypeSerializer extends StdSerializer<AbstractType> {
    public AbstractTypeSerializer() {
        this(null);
    }
    public AbstractTypeSerializer(Class<AbstractType> t) {
        super(t);
    }

    @Override
    public void serialize(AbstractType abstractType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        serializerProvider.defaultSerializeValue(abstractType.getValue(), jsonGenerator);
    }

}
