package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventTimestamp {
    String sourceTimestamp;
    String processTimestamp;
    String databaseTimestamp;
}
