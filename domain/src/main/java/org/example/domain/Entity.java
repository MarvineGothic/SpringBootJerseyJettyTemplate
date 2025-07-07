package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public abstract class Entity {
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entity entity)) {
            return false;
        }

        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
