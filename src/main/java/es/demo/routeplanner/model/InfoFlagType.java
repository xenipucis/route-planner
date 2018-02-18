package es.demo.routeplanner.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum InfoFlagType {
    FROM(0),
    TO(1),
    FROM_EQUAL_TO(2),
    ;

    private int bitPosition;

    InfoFlagType(final int bitPosition) {
        this.bitPosition = bitPosition;
    }

    public int getBitPosition() {
        return bitPosition;
    }

    public int getValue() {
        return (int) Math.pow(2.0, this.getBitPosition());
    }

    public static int generateValue(final List<InfoFlagType> infoFlagTypes) {
        final Set<InfoFlagType> infoFlagTypesSet = new HashSet<>(infoFlagTypes);
        return infoFlagTypes.stream().mapToInt(infoFlagType -> infoFlagType.getValue()).sum();
    }

    public static boolean containsBothFromAndTo(final int value) {
        return containsAllInfoFlagTypesFromList(value, Arrays.asList(FROM, TO));
    }

    public static boolean fromAndToAreEqual(final int value) {
        return containsInfoFlagType(value, FROM_EQUAL_TO);
    }

    // returns true if value contains InfoFlagType infoFlagType
    private static boolean containsInfoFlagType(final int value, final InfoFlagType infoFlagType) {
        return ((value & (1L << infoFlagType.getBitPosition())) != 0);
    }

    // returns true if value contains all PositionTypes from list
    private static boolean containsAllInfoFlagTypesFromList(final int value, final List<InfoFlagType> infoFlagTypes) {
        return !infoFlagTypes.stream().map(infoFlagType -> containsInfoFlagType(value, infoFlagType))
                .collect(Collectors.toList()).contains(false);
    }
}
