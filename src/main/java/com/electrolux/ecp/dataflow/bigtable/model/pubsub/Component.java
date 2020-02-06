package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

@Data
public class Component {
    String id;
    String name;
    String value;
    String key;
    Integer number;
    String valueType;
    String valueAsString;
}
