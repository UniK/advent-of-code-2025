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

    @Override
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

    private static final int RIGHT_PADDING = 12;

    @Override
    public String compute(List<String> lines) {
        long sum = lines.stream()
                .mapToLong(this::findHighestJoltageOfTwelve)
                .sum();

        return String.valueOf(sum);
    }

    long findHighestJoltageOfTwelve(String s) {
        var digits = s.chars()
                .map(Character::getNumericValue)
                .boxed()
                .toList();

        var currentIndex = 0;
        long max = 0;

        for (int i = RIGHT_PADDING - 1; i >= 0; i--) {
            int rightBound = digits.size() - i;
            var window = digits.subList(currentIndex, rightBound);

            int localMax = window.stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElseThrow();

            int nextIndex = currentIndex + digits.subList(currentIndex, rightBound)
                    .indexOf(localMax) + 1;

            max = max * 10 + localMax;
            currentIndex = nextIndex;
        }

        return max;
    }
}

static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}
