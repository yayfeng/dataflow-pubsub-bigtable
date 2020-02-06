package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

@Data
public class DecodedHaclMessageKey {
    Device device;
    Long deviceTimestamp;
    Long kafkaTimestamp;
    Long decoderTimestamp;
    String value;
}
