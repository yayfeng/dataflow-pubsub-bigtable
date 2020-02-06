package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BigtableEvent {

    String applianceId;
    String gatewayTimestamp;
    ApplianceIdentifier applianceIdentifier;
    ComponentIdentifier componentIdentifier;
    EventTimestamp eventTimestamp;
    EventValue eventValue;
    EventReference eventReference;

}
