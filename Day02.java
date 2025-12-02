import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 02 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day02.test";

    try (Stream<String> lineStream = Files.lines(Path.of(input))) {
        List<String> lines = lineStream.toList();
        println("""
                Part 1: %s
                Part 2: %s
                """.formatted(solve(1, lines), solve(2, lines)));
    } catch (IOException e) {
        err.println("Unable to read input file '" + input + "': " + e.getMessage());
    }
}

sealed interface Part permits Part1, Part2 {
    String compute(List<String> lines);
}

static final class Part1 implements Part {

    @Override
    public String compute(List<String> lines) {
        var sumOfInvalidIds = lines.stream()
                .flatMap(line -> Arrays.stream(line.split("[,-]")))
                .map(Long::parseLong)
                .gather(Gatherers.windowFixed(2))
                // .peek(it -> IO.print(it + " - "))
                .map(window -> LongStream.rangeClosed(window.get(0), window.get(1))
                        .filter(num -> String.valueOf(num).length() % 2 == 0)
                        .filter(this::containsInvalidId)
                        // .peek(id -> IO.print(id + ","))
                        .sum())
                // .peek(IO::println)
                .reduce(0L, Long::sum);

        return String.valueOf(sumOfInvalidIds);
    }

    private boolean containsInvalidId(long id) {
        var idString = String.valueOf(id);

        int length = idString.length();
        var firstHalf = idString.substring(0, length / 2);
        var secondHalf = idString.substring(length / 2);

        return firstHalf.equals(secondHalf);
    }
}

static final class Part2 implements Part {

    @Override
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
