import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 03 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day03.test";

    try (Stream<String> lineStream = Files.lines(Path.of(input))) {
        List<String> lines = lineStream.toList();
        println("""
                Part 1: %s
                Part 2: %s
                """.formatted(
                solve(1, lines),
                solve(2, lines)));
    } catch (IOException e) {
        err.println("Unable to read input file '" + input + "': " + e.getMessage());
    }
}

sealed interface Part permits Part1, Part2 {
    String compute(List<String> lines);
}

final static class Part1 implements Part {
    public String compute(List<String> lines) {

        int sum = lines.stream()
                .mapToInt(this::findHighestJoltage)
                .sum();

        return String.valueOf(sum);
    }

    int findHighestJoltage(String s) {
        // 1: Find the first, leftmost highest digit, excluding the last position.
        var firstSubstring = s.substring(0, s.length() - 1);

        int firstMaxDigt = Arrays.stream(firstSubstring.split(""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(-1);

        if (firstMaxDigt == -1)
            return 0;

        // 2: Find the second highest digit starting from the position of
        // firstMaxDigitPos.
        int firstMaxDigitPos = s.indexOf(String.valueOf(firstMaxDigt)) + 1;
        var secondSubstring = s.substring(firstMaxDigitPos);

        int secondMaxDigit = Arrays.stream(secondSubstring.split(""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(-1);

        return secondMaxDigit == -1
                ? 0
                : firstMaxDigt * 10 + secondMaxDigit;
    }
}

final static class Part2 implements Part {
    public String compute(List<String> lines) {
        return "Not implemented";
    }
}

static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}
