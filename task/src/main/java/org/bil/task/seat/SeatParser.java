package org.bil.task.seat;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser class for seat strings.
 * Thread-safe and stateless.
 */
public final class SeatParser {

    private static final Pattern SEAT_PATTERN = Pattern.compile(
            "(?:(?<sector>.*?) (?<sectorName>(?:\\p{Lu}|\\d).*?)|(?<sectorWithoutName>.*)) "
                    + "(?<row>Ряд) (?<rowName>.*) "
                    + "(?<seat>Место) (?<seatName>.*)"
    );

    private static final Map<Character, Character> CHAR_REPLACEMENTS = Map.ofEntries(
            Map.entry('С', 'C'),
            Map.entry('Е', 'E'),
            Map.entry('Т', 'T'),
            Map.entry('Н', 'H'),
            Map.entry('У', 'Y'),
            Map.entry('О', 'O'),
            Map.entry('Р', 'P'),
            Map.entry('Х', 'X'),
            Map.entry('А', 'A'),
            Map.entry('В', 'B'),
            Map.entry('К', 'K'),
            Map.entry('М', 'M')
    );

    private SeatParser() {
    }

    public static SeatInformation parse(String input, long id) {

        if (input == null || input.isEmpty()) {
            return null;
        }

        Matcher matcher = matchInput(input);
        if (matcher == null) {
            return null;
        }

        SeatInformation seatInfo = buildSeatInformation(matcher, id);
        if (!isValid(seatInfo)) {
            return null;
        }

        return seatInfo;
    }

    private static Matcher matchInput(String input) {

        Matcher matcher = SEAT_PATTERN.matcher(input);

        return matcher.find() ? matcher : null;
    }

    private static SeatInformation buildSeatInformation(Matcher matcher, long id) {

        String sectorWithoutName = matcher.group("sectorWithoutName");
        String sector;
        String sectorName;

        if (sectorWithoutName != null && !sectorWithoutName.isEmpty()) {
            sector = sectorWithoutName;
            sectorName = "";
        } else {
            sector = matcher.group("sector");
            sectorName = replaceChars(matcher.group("sectorName"));
        }

        return new SeatInformation(
                id,
                sector,
                sectorName,
                matcher.group("row"),
                matcher.group("rowName"),
                matcher.group("seat"),
                matcher.group("seatName")
        );
    }


    private static boolean isValid(SeatInformation seatInfo) {
        return seatInfo.getSector() != null &&
                seatInfo.getRow() != null &&
                seatInfo.getRowName() != null &&
                seatInfo.getSeat() != null &&
                seatInfo.getSeatName() != null;
    }

    private static String replaceChars(String input) {

        if (input == null || input.isEmpty()) {
            return input;
        }

        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            chars[i] = CHAR_REPLACEMENTS.getOrDefault(chars[i], chars[i]);
        }

        return new String(chars);
    }

}