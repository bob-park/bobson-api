package org.bobpark.bobsonclient.configure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bobson.client")
public class BobsonClientProperties {

    private boolean enabled;
    private String host;
    private String instanceId;

    public BobsonClientProperties() {
        this.enabled = true;
        this.host = "http://localhost:9090";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
