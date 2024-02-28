package com.priceline.chutes;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.entry;

import java.util.LinkedList;
import java.util.List;

public class MovementMap {
    private Map<Integer, Integer> MOVEMENT_MAP;

    /* 
    Changed this structure because the previous structure is harder to read.
    It's easier to understand (and therefore to debug) when you have the actual result you expect in the code
    rather than having to derive it at testing time
    */
    public MovementMap() {
        MOVEMENT_MAP = Map.ofEntries(
            entry(1, 38),
            entry(4, 14),
            entry(9, 31),
            entry(16, 6),
            entry(21, 42),
            entry(28, 84),
            entry(36, 44),
            entry(47, 26),
            entry(49, 11),
            entry(51, 67),
            entry(56, 53),
            entry(62, 19),
            entry(64, 60),
            entry(71, 91),
            entry(80, 100),
            entry(87, 24),
            entry (93, 73),
            entry(95, 75),
            entry(98, 78)
        );
    }

    /* NEW FEATURE: set your own configuration of chutes and ladders
     * 
     * Expected input to have each "from" followed by a "to".
     * 
     * In other languages you can do a filter with an index, e.g. in NodeJS:
     * 
     * oddIndexValues = myList.filter((item, idx) => if (idx % 2 == 1) { return item } else { return null }))
     * 
     * or something like that.  This isn't possible in Java (at least I couldn't find it on Google) so I'm doing it in
     * a bit of a roundabout way
     */
    public MovementMap(int boardSize, List<Integer> movementConfiguration) {
        if (movementConfiguration.size() % 2 != 0) throw new IllegalArgumentException("Each 'from' must be accompanied by a 'to'");

        List<Integer> fromEntries = new LinkedList<>();
        List<Integer> toEntries = new LinkedList<>();

        for (int i = 0; i < movementConfiguration.size(); i++) {
            if (movementConfiguration.get(i) > boardSize || movementConfiguration.get(i) < 1) {
                throw new IllegalArgumentException("Board spaces are numbered from 1 to the board size");
            }
            if (i % 2 == 0) {
                fromEntries.add(movementConfiguration.get(i));
            } else {
                toEntries.add(movementConfiguration.get(i));
            };
        }
        MOVEMENT_MAP = IntStream.range(0, fromEntries.size()).boxed()
            .collect(Collectors.toMap(fromEntries::get, toEntries::get));
    }

    public int getFinalLocation(int startingLocation) {
        return MOVEMENT_MAP.getOrDefault(startingLocation, startingLocation);
    }
}
