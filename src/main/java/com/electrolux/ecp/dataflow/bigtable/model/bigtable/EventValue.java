package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventValue {
    String value;
    String valueCode;
    String rawInput;
    String prevValue;
    String valueType;
}
