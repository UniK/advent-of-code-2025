import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 05 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day05.test";

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
        List<String> freshIngredients = lines.stream()
                .takeWhile(line -> !line.isBlank())
                .toList();

        List<String> availableIngredients = lines.stream()
                .dropWhile(line -> !line.isBlank())
                .skip(1) // Skip the blank line itself
                .toList();

        // println("Fresh Ingredients: " + freshIngredients);
        // println("Available Ingredients: " + availableIngredients);

        Set<Long> freshIngredientRanges = freshIngredients.stream()
                .flatMapToLong(line -> {
                    String[] parts = line.split("[,-]");
                    List<Long> numbers = Arrays.stream(parts)
                            .map(Long::parseLong)
                            .toList();

                    // For each pair (start, end) produce a LongStream of the range
                    return IntStream.range(0, numbers.size() / 2)
                            .mapToLong(i -> numbers.get(2 * i)) // start values
                            .mapToObj(start -> {
                                int idx = numbers.indexOf(start);
                                long end = numbers.get(idx + 1);
                                return LongStream.rangeClosed(start, end);
                            })
                            .flatMapToLong(l -> l);
                })
                .boxed()
                .collect(Collectors.toSet());

        println("Fresh Ingredient Ranges: " + freshIngredientRanges);

        long sum = availableIngredients.stream()
                .flatMapToLong(line -> {
                    String[] parts = line.split("[,-]");
                    if (parts.length == 1) {
                        return LongStream.of(Long.parseLong(parts[0]));
                    } else if (parts.length == 2) {
                        long start = Long.parseLong(parts[0]);
                        long end = Long.parseLong(parts[1]);
                        return LongStream.rangeClosed(start, end);
                    }
                    return LongStream.empty();
                })
                .filter(freshIngredientRanges::contains)
                .count();

        return String.valueOf(sum);
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
