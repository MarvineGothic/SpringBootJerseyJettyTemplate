package org.example.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserCreatedEvent extends ApplicationEvent {
    private Object event;

    public UserCreatedEvent(Object source, Object event) {
        super(source);
        this.event = event;
    }
}
