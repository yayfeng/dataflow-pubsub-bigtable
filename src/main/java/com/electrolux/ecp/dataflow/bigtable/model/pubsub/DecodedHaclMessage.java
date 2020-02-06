package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

@Data
public class DecodedHaclMessage {
    String key;
    String value;
}
