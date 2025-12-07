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

static long vertical(List<String> padded, int k) {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < padded.size() - 1; row++) {
        char c = padded.get(row).charAt(k);
        if (Character.isDigit(c)) sb.append(c);
    }
    return sb.length() > 0 ? Long.parseLong(sb.toString()) : 0L;
}

sealed interface Part permits Part1, Part2 {
    String compute(List<String> lines);
}

static final class Part1 implements Part {
    public String compute(List<String> lines) {
        List<String> numberLines = lines.subList(0, lines.size() - 1);
        String[] operators = lines.getLast().strip().split("\\s+");

        return String.valueOf(IntStream.range(0, operators.length)
                .mapToLong(i -> {
                    char op = operators[i].charAt(0);
                    List<Long> nums = numberLines.stream()
                            .map(line -> line.strip().split("\\s+")[i])
                            .map(Long::parseLong)
                            .toList();
                    return nums.stream()
                            .reduce((a, b) -> switch (op) {
                                case '+' -> a + b;
                                case '*' -> a * b;
                                default -> throw new IllegalStateException("Unexpected operator: " + op);
                            })
                            .orElse(0L);
                })
                .sum());
    }
}

static final class Part2 implements Part {
    public String compute(List<String> lines) {
        int maxLen = lines.stream().mapToInt(String::length).max().orElse(0);
        List<String> padded = lines.stream()
                .map(line -> String.format("%-" + maxLen + "s", line))
                .toList();
        String operatorLine = lines.getLast();
        List<Integer> opPositions = new ArrayList<>();
        for (int j = 0; j < operatorLine.length(); j++) {
            if (operatorLine.charAt(j) != ' ') {
                opPositions.add(j);
            }
        }
        String[] operators = opPositions.stream().map(j -> "" + operatorLine.charAt(j)).toArray(String[]::new);
        return String.valueOf(IntStream.range(0, operators.length)
                .map(i -> operators.length - 1 - i)
                .mapToLong(i -> {
                    char op = operators[i].charAt(0);
                    int start = opPositions.get(i);
                    int end = i < opPositions.size() - 1 ? opPositions.get(i + 1) - 1 : maxLen - 1;
                    List<Long> nums = new ArrayList<>();
                    for (int k = start; k <= end; k++) {
                        long v = vertical(padded, k);
                        if (v != 0) nums.add(v);
                    }
                    Collections.reverse(nums);
                    return nums.stream()
                            .reduce((a, b) -> switch (op) {
                                case '+' -> a + b;
                                case '*' -> a * b;
                                default -> throw new IllegalStateException("Unexpected operator: " + op);
                            })
                            .orElse(0L);
                })
                .sum());
    }
}



static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}
