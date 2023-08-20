package org.bobpark.bobsonclient.sample.domain.cqrs.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CreatedSampleEvent {

    private String id;
    private String name;
}
