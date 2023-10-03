package org.bobpark.bobsonclient.configure.properties;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Getter
@Setter
@ConfigurationProperties("bobson.client")
public class BobsonClientProperties {

    /**
     * Enabled Bobson Client
     */
    private boolean enabled = true;

    /**
     * Bobson host
     */
    private String host;

    /**
     * Bobson Client instance id (default random uuid)
     */
    private String instanceId;

    /**
     * time to fetch event. (ms).
     */
    private Long fetchTimeMs = 1_000L;

    public BobsonClientProperties() {
        this.host = "http://localhost:8080";
        this.instanceId = UUID.randomUUID().toString();
    }
}
