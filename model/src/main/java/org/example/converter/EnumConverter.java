package org.example.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class EnumConverter<T extends Enum<T>> implements ConverterFactory<String, T> {

    @Override
    @NonNull
    public <E extends T> Converter<String, E> getConverter(@NonNull Class<E> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private record StringToEnumConverter<T extends Enum>(Class<T> enumType) implements Converter<String, T> {

        public T convert(String source) {
                return (T) Enum.valueOf(this.enumType, source.toUpperCase());
            }
        }
}

