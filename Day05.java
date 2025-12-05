import java.util.*;
import static java.lang.System.out;
import static java.lang.System.err;

/**
 * Advent of Code Day 05 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day05.test";

    try (Stream<String> lineStream = Files.lines(Path.of(input))) {
        List<String> lines = lineStream.toList();
        out.println("""
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
        List<String> freshIngredients = lines.stream()
                .takeWhile(line -> !line.isBlank())
                .toList();

        List<String> availableIngredients = lines.stream()
                .dropWhile(line -> !line.isBlank())
                .skip(1) // Skip the blank line itself
                .toList();

        // Parse fresh ranges
        List<long[]> freshRanges = new ArrayList<>();
        for (String line : freshIngredients) {
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            freshRanges.add(new long[] { start, end });
        }

        // Sort and merge overlapping ranges
        freshRanges.sort(Comparator.comparingLong(a -> a[0]));
        List<long[]> mergedRanges = new ArrayList<>();
        for (long[] range : freshRanges) {
            if (mergedRanges.isEmpty() || mergedRanges.getLast()[1] < range[0]) {
                mergedRanges.add(range);
            } else {
                mergedRanges.getLast()[1] = Math.max(mergedRanges.getLast()[1], range[1]);
            }
        }

        long sum = 0;
        for (String line : availableIngredients) {
            String[] parts = line.split("-");
            if (parts.length == 1) {
                long num = Long.parseLong(parts[0]);
                if (isInRanges(num, mergedRanges))
                    sum++;
            } else {
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                sum += countInRange(start, end, mergedRanges);
            }
        }

        return String.valueOf(sum);
    }

    private static boolean isInRanges(long num, List<long[]> ranges) {
        for (long[] r : ranges) {
            if (num >= r[0] && num <= r[1])
                return true;
        }
        return false;
    }

    private static long countInRange(long start, long end, List<long[]> ranges) {
        long count = 0;
        for (long[] r : ranges) {
            long overlapStart = Math.max(start, r[0]);
            long overlapEnd = Math.min(end, r[1]);
            if (overlapStart <= overlapEnd) {
                count += overlapEnd - overlapStart + 1;
            }
        }
        return count;
    }
}

final static class Part2 implements Part {
    public String compute(List<String> lines) {
        List<String> freshIngredients = lines.stream()
                .takeWhile(line -> !line.isBlank())
                .toList();

        // Parse ranges
        List<long[]> freshRanges = new ArrayList<>();
        for (String line : freshIngredients) {
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            freshRanges.add(new long[] { start, end });
        }

        // Sort and merge overlapping ranges
        freshRanges.sort(Comparator.comparingLong(a -> a[0]));
        List<long[]> mergedRanges = new ArrayList<>();
        for (long[] range : freshRanges) {
            if (mergedRanges.isEmpty() || mergedRanges.getLast()[1] < range[0]) {
                mergedRanges.add(range);
            } else {
                mergedRanges.getLast()[1] = Math.max(mergedRanges.getLast()[1], range[1]);
            }
        }

        // Sum the sizes of merged ranges
        long total = 0;
        for (long[] range : mergedRanges) {
            total += range[1] - range[0] + 1;
        }

        return String.valueOf(total);
    }
}

static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}
