import static java.lang.IO.print;
import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 07 solution using Java 25.
 */
sealed interface Part permits Part1, Part2 {
    String compute(List<String> lines);
}

void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day07.test";

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

static final class Part1 implements Part {
    public String compute(List<String> lines) {

        int rejectsCount = 0;

        Set<Integer> currentPositions = new HashSet<>();
        currentPositions.add(lines.get(0).indexOf('S'));

        // Iterate over the rows
        Set<Integer> nextPositions = new HashSet<>();
        for (int col = 1; col < lines.size(); col++) {
            nextPositions.clear();

            // Process each position in the current row
            for (Integer pos : currentPositions) {
                char charAtPos = lines.get(col).charAt(pos);
                switch (charAtPos) {
                    case '.' -> nextPositions.add(pos);
                    case '^' -> {
                        rejectsCount++;
                        nextPositions.add(pos - 1);
                        nextPositions.add(pos + 1);
                    }
                }
            }
            currentPositions.clear();
            currentPositions.addAll(nextPositions);
        }

        return String.valueOf(rejectsCount);
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
