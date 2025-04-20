package org.example.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.example.error.ServiceException;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EnumDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    private Class<? extends Enum<?>> enumClass;

    public EnumDeserializer() {
    }

    public EnumDeserializer(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        JavaType type = deserializationContext.getContextualType() != null
                ? deserializationContext.getContextualType()
                : beanProperty.getMember().getType();
        if (type.isCollectionLikeType()) {
            type = type.getContentType();
        }
        return new EnumDeserializer((Class<? extends Enum<?>>) type.getRawClass());
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (node.isArray()) {
            return StreamSupport.stream(node.spliterator(), false)
                    .map(this::getEnumFromValue).toList();
        } else {
            return getEnumFromValue(node);
        }
    }

    private Enum<?> getEnumFromValue(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.asText() == null || enumClass == null) {
            return null;
        }

        String value = jsonNode.asText();

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.name().equals(value.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new ServiceException("Invalid enum value: " + value, 400));
    }
}
