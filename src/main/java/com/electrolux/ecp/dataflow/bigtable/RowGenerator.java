package com.electrolux.ecp.dataflow.bigtable;

import com.electrolux.ecp.dataflow.bigtable.model.bigtable.ApplianceIdentifier;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.BigtableEvent;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.ComponentIdentifier;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.EventReference;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.EventTimestamp;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.EventValue;
import com.electrolux.ecp.dataflow.bigtable.model.pubsub.DecodedHaclMessage;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.beam.sdk.transforms.DoFn;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RowGenerator extends DoFn<String, RowMutation> {

    private static final Gson GSON = new GsonBuilder().create();

    @ProcessElement
    public void processElement(ProcessContext context) {
        String decodedHaclAsJsonString = context.element();
        DecodedHaclMessage message = GSON.fromJson(decodedHaclAsJsonString, DecodedHaclMessage.class);
        BigtableEvent event = DataConverter.fromDecodedHaclMessage(message);

        String rowKey = generateRowKey(event.getApplianceIdentifier(),
                event.getComponentIdentifier(),
                event.getGatewayTimestamp());

        RowMutation rowMutation = getRowMutationRequest(rowKey,
                event.getComponentIdentifier(),
                event.getEventValue(),
                event.getEventTimestamp(),
                event.getEventReference());

        context.output(rowMutation);
    }

    private String generateRowKey(ApplianceIdentifier applianceIdentifier,
                                  ComponentIdentifier componentIdentifier,
                                  String gatewayTimestamp) {
        long ts = getTimestamp(gatewayTimestamp);
        long reverseTs = Long.MAX_VALUE - ts;
        return MessageFormat.format("{0}_{1}:{2}-{3}#{4}#{5}",
                applianceIdentifier.getPnc(),
                applianceIdentifier.getElc(),
                applianceIdentifier.getSn(),
                applianceIdentifier.getMac(),
                componentIdentifier.getPropertyName(),
                String.valueOf(reverseTs));
    }

    private long getTimestamp(String timestampAsString) {
        LocalDateTime localDateTime;
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            localDateTime = LocalDateTime.from(formatter.parse(timestampAsString));
        } catch (Exception e1) {
            localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        }
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    private RowMutation getRowMutationRequest(String rowKey,
                                              ComponentIdentifier componentIdentifier,
                                              EventValue eventValue,
                                              EventTimestamp eventTimestamp,
                                              EventReference eventReference) {

        RowMutation rowMutation = RowMutation.create("EVENTS", rowKey)
                .setCell("VALUE", "RAW_INPUT", eventValue.getRawInput())
                .setCell("TIMESTAMP", "SOURCE_TS", eventTimestamp.getSourceTimestamp())
                .setCell("TIMESTAMP", "PROCESS_TS", eventTimestamp.getProcessTimestamp())
                .setCell("TIMESTAMP", "DB_TS", eventTimestamp.getDatabaseTimestamp())
                .setCell("REFERENCE", "IS_PARENT", Optional.ofNullable(eventReference.getParentId()).isPresent() ? 0 : 1);

        if (componentIdentifier.getModulePath() != null) {
            rowMutation.setCell("VALUE", "VALUE", componentIdentifier.getModulePath());
        }

        if (componentIdentifier.getComponentId() != null) {
            rowMutation.setCell("VALUE", "VALUE", componentIdentifier.getComponentId());
        }

        if (eventValue.getValue() != null) {
            rowMutation.setCell("VALUE", "VALUE", eventValue.getValue());
        }

        if (eventValue.getPrevValue() != null) {
            rowMutation.setCell("VALUE", "PREV_VALUE", eventValue.getPrevValue());
        }

        if (eventValue.getValueType() != null) {
            rowMutation.setCell("VALUE", "VALUE_TYPE", eventValue.getValueType());
        }

        if (eventValue.getValueCode() != null) {
            rowMutation.setCell("VALUE", "VALUE_CODE", eventValue.getValueCode());
        }

        if (eventReference.getUserId() != null) {
            rowMutation.setCell("REFERENCE", "USER_ID", eventReference.getUserId());
        }

        if (eventReference.getDeviceId() != null) {
            rowMutation.setCell("REFERENCE", "DEVICE_ID", eventReference.getDeviceId());
        }

        return rowMutation;
    }

}
