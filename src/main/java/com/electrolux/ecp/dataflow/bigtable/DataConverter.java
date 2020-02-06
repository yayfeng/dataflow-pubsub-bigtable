package com.electrolux.ecp.dataflow.bigtable;

import com.electrolux.ecp.dataflow.bigtable.model.bigtable.ApplianceIdentifier;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.BigtableEvent;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.ComponentIdentifier;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.EventTimestamp;
import com.electrolux.ecp.dataflow.bigtable.model.bigtable.EventValue;
import com.electrolux.ecp.dataflow.bigtable.model.pubsub.Component;
import com.electrolux.ecp.dataflow.bigtable.model.pubsub.DecodedHaclMessage;
import com.electrolux.ecp.dataflow.bigtable.model.pubsub.DecodedHaclMessageKey;
import com.electrolux.ecp.dataflow.bigtable.model.pubsub.DecodedHaclMessageValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataConverter {

    private static final Gson GSON = new GsonBuilder().create();

    public static BigtableEvent fromDecodedHaclMessage(DecodedHaclMessage message) {
        String key = message.getKey();
        String value = message.getValue();

        DecodedHaclMessageKey messageKey = GSON.fromJson(key, DecodedHaclMessageKey.class);
        DecodedHaclMessageValue messageValue = GSON.fromJson(value, DecodedHaclMessageValue.class);

        String applianceId =
                MessageFormat.format("{0}:{1}",
                        messageKey.getDevice().getDeviceType(),
                        messageKey.getDevice().getDeviceId());

        ApplianceIdentifier applianceIdentifier =
                ApplianceIdentifier.builder()
                        .pnc(messageKey.getDevice().getPnc())
                        .elc(messageKey.getDevice().getElc())
                        .sn(messageKey.getDevice().getSerialNumber())
                        .mac(messageKey.getDevice().getMacAddress())
                        .build();

        Component component = messageValue.getPayload().getComponents().get(0);

        ComponentIdentifier componentIdentifier =
                ComponentIdentifier.builder()
                        .componentId(component.getId())
                        .propertyName(component.getName())
                        .modulePath(messageValue.getPayload().getSource())
                        .build();

        EventValue eventValue =
                EventValue.builder()
                        .rawInput(messageKey.getValue())
                        .build();

        EventTimestamp eventTimestamp =
                EventTimestamp.builder()
                        .sourceTimestamp(toTimestampString(messageKey.getDecoderTimestamp()))
                        .processTimestamp(toTimestampString(messageValue.getPayload().getTimestamp()))
                        .build();

        return BigtableEvent.builder()
                .applianceId(applianceId)
                .applianceIdentifier(applianceIdentifier)
                .componentIdentifier(componentIdentifier)
                .eventValue(eventValue)
                .eventTimestamp(eventTimestamp)
                .build();
    }

    private static String toTimestampString(Long timestamp) {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime
                .ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"))
                .format(formatter);
    }

}
