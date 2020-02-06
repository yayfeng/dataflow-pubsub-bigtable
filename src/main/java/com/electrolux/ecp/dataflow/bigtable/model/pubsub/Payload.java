package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

import java.util.List;

@Data
public class Payload {
    String version;
    List<Component> components;
    String source;
    String destination;
    Long timestamp;
}
