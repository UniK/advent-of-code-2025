import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 01 solution using Java 25.
 */

// Initialize a circular array in the range [0, 99].
static final int[] CIRCULAR_ARRAY = new int[100];

void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day01.test";

    try (Stream<String> lineStream = Files.lines(Path.of(input))) {
        List<String> lines = lineStream.toList();
        println("""
                Part 1: %s
                Part 2: %s
                """
                .formatted(
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

    @Override
    public String compute(List<String> lines) {
        var steps = lines.stream().mapToInt(
                line -> line.startsWith("R")
                        ? Integer.parseInt(line.substring(1))
                        : -Integer.parseInt(line.substring(1)))
                .boxed()
                .toList();

        var pointer = new AtomicReference<>(50);
        var zeroCount = steps.stream().reduce(0, (count, step) -> {
            pointer.set(movePointer(pointer.get(), step));

            return pointer.get() == 0
                    ? count + 1
                    : count;
        });

        return String.valueOf(zeroCount);
    }
}

static final class Part2 implements Part {
    @Override
    public String compute(List<String> lines) {
        var steps = lines.stream().mapToInt(
                line -> line.startsWith("R")
                        ? Integer.parseInt(line.substring(1))
                        : -Integer.parseInt(line.substring(1)))
                .boxed()
                .toList();

        var pointer = 50;
        var zeroCount = 0;

        for (var step : steps) {
            int currentPointer = pointer;
            int newPointer = movePointer(currentPointer, step);
            int zerosPassed = 0;

            if (step > 0) { // Moving right
                zerosPassed = (currentPointer + step - 1) / CIRCULAR_ARRAY.length;
            } else if (step < 0) { // Moving left
                zerosPassed = Math.floorDiv(currentPointer - 1, CIRCULAR_ARRAY.length)
                        - Math.floorDiv(currentPointer + step, CIRCULAR_ARRAY.length);
            }

            if (newPointer == 0) {
                zerosPassed++;
            }

            zeroCount += zerosPassed;
            pointer = newPointer;
        }

        return String.valueOf(zeroCount);
    }
}

static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}

static int movePointer(int currentPointer, int steps) {
    if (steps < 0)
        steps = CIRCULAR_ARRAY.length + (steps % CIRCULAR_ARRAY.length);

    return (currentPointer + steps) % CIRCULAR_ARRAY.length;
}
