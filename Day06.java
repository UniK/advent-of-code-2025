import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 06 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day06.test";

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

static final class Part1 implements Part {
    public String compute(List<String> lines) {

        // Numbers: Read the all but last line and split into columns.
        List<String[]> numberRows = lines.subList(0, lines.size() - 1).stream()
                .map(line -> line.strip().split("\\s+"))
                .toList();

        // Operators: Read the last line and split into columns.
        String[] operatorRow = lines.getLast().strip().split("\\s+");

        int numColumns = numberRows.getFirst().length;

        return String.valueOf(IntStream.range(0, numColumns)
                .mapToLong(colIndex -> {
                    char operator = operatorRow[colIndex].charAt(0);

                    return numberRows.stream()
                            .map(row -> row[colIndex])
                            .mapToLong(Long::parseLong)
                            .reduce((a, b) -> switch (operator) {
                                case '+' -> a + b;
                                case '*' -> a * b;
                                default -> throw new IllegalStateException("Unexpected operator: " + operator);
                            })
                            .orElseThrow(() -> new IllegalStateException("No numbers found for column: " + colIndex));
                })
                .sum());
    }
}

static final class Part2 implements Part {
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
