package com.buihien.demo.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("new")
    NEW,

    @JsonProperty("in_progress")
    IN_PROGRESS,

    @JsonProperty("completed")
    COMPLETED,
    @JsonProperty("paused")
    PAUSED;
}
