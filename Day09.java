import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 09 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day09.test";

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

    record RedTile(long x, long y) {}

    public String compute(List<String> lines) {

        long maxArea = -1;

        List<RedTile> points = lines.stream()
                .map(line -> line.split(","))
                .map(parts -> new RedTile(
                        Integer.parseInt(parts[0].strip()),
                        Integer.parseInt(parts[1].strip())))
                .collect(Collectors.toList());

        for (RedTile it1 : points) {
            for (RedTile it2 : points) {
                var localArea = calcArea(it1, it2);

                if (localArea > maxArea)
                    maxArea = localArea;
            }
        }

        return String.valueOf(maxArea);
    }

    long calcArea(RedTile rt1, RedTile rt2) {
        System.out.printf("(%s,%s)(%s,%s)\t[width: %s, height: %s] - area: %s%n",
                rt1.x,
                rt1.y,
                rt2.x,
                rt2.y,
                Math.abs(rt1.x - rt2.x) + 1,
                Math.abs(rt1.y - rt2.y) + 1,
                (Math.abs(rt1.x - rt2.x) + 1) * (Math.abs(rt1.y - rt2.y) + 1));
        return (Math.abs(rt1.x - rt2.x) + 1) * (Math.abs(rt1.y - rt2.y) + 1);
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
