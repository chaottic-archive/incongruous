package com.chaottic.incongruous.graphics;

import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class CharacterToCoordinates {
    private static final FloatFloatPair EMPTY = FloatFloatPair.of(0.0F, 0.0F);

    private static final Int2ObjectMap<FloatFloatPair> MAP;

    static {
        var map = new Int2ObjectOpenHashMap<FloatFloatPair>();
        map.put('A', EMPTY);
        map.put('B', EMPTY);
        map.put('C', EMPTY);
        map.put('D', EMPTY);
        map.put('E', EMPTY);
        map.put('F', EMPTY);
        map.put('G', EMPTY);
        map.put('H', EMPTY);
        map.put('I', EMPTY);
        map.put('J', EMPTY);
        map.put('K', EMPTY);
        map.put('L', EMPTY);
        map.put('M', EMPTY);
        map.put('N', EMPTY);
        map.put('O', EMPTY);
        map.put('P', EMPTY);
        map.put('Q', EMPTY);
        map.put('R', EMPTY);
        map.put('S', EMPTY);
        map.put('T', EMPTY);
        map.put('U', EMPTY);
        map.put('V', EMPTY);
        map.put('W', EMPTY);
        map.put('X', EMPTY);
        map.put('Y', EMPTY);
        map.put('Z', EMPTY);
        map.put('a', EMPTY);
        map.put('b', EMPTY);

        MAP = Int2ObjectMaps.unmodifiable(map);
    }

    public static FloatFloatPair getCoordinates(int character) {
        return MAP.getOrDefault(character, EMPTY);
    }
}
