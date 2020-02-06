package com.electrolux.ecp.dataflow.bigtable.model.pubsub;

import lombok.Data;

@Data
public class Device {
    String deviceType;
    String deviceId;
    String pnc;
    String elc;
    String macAddress;
    String serialNumber;
}
