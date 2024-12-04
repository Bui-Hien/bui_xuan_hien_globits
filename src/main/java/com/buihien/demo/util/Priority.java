package com.buihien.demo.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Priority {
    @JsonProperty("high")
    HIGH,
    @JsonProperty("medium")
    MEDIUM,
    @JsonProperty("low")
    LOW;
}
