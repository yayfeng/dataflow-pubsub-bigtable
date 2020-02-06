package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventReference {
    String parentId;
    String userId;
    String deviceId;
}
