package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

@Data
public class DecodedHaclMessageValue {
    Long timestamp;
    Payload payload;
}
