import static java.lang.IO.println;
import static java.lang.System.err;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code Day 01 solution using Java 25.
 */
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
    // Initialize a circular array in the range [0, 99].
    private static final int[] CIRCULAR_ARRAY = IntStream.range(0, 100).toArray();

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

            return readValue(pointer.get()) == 0 ? count + 1 : count;
        });

        return String.valueOf(zeroCount);
    }

    int movePointer(int currentIndex, int steps) {
        if (steps < 0)
            steps = CIRCULAR_ARRAY.length + (steps % CIRCULAR_ARRAY.length);

        return (currentIndex + steps) % CIRCULAR_ARRAY.length;
    }

    int readValue(int index) {
        return CIRCULAR_ARRAY[index];
    }
}

record Part2() implements Part {
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
