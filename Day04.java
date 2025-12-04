import static java.lang.IO.println;
import static java.lang.System.err;

/**
 * Advent of Code Day 04 solution using Java 25.
 */
void main(String... args) {
    var input = args.length > 0 ? args[0] : "Day04.test";

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
    record Point(int r, int c) {
    }

    public String compute(List<String> lines) {
        char[][] grid = lines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        Set<Point> collectedRolls = new HashSet<>();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] == '@') {
                    long atNeighborCount = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if (dr == 0 && dc == 0)
                                continue;

                            int neighborR = r + dr;
                            int neighborC = c + dc;

                            if (neighborR >= 0 && neighborR < grid.length &&
                                    neighborC >= 0 && neighborC < grid[r].length) {
                                if (grid[neighborR][neighborC] == '@') {
                                    atNeighborCount++;
                                }
                            }
                        }
                    }

                    Point currentPoint = new Point(r, c);
                    if (atNeighborCount < 4) {
                        collectedRolls.add(currentPoint);
                    } else {
                        collectedRolls.remove(currentPoint);
                    }
                }
            }
        }

        return String.valueOf(collectedRolls.size());
    }
}

final static class Part2 implements Part {

    public String compute(List<String> lines) {
        record Point(int r, int c) {
        }

        char[][] grid = lines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        Set<Point> collectedRolls = new HashSet<>();

        boolean changed;
        do {
            changed = false;

            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    if (grid[r][c] == '@') {
                        long rollNeighborCount = 0;
                        for (int dr = -1; dr <= 1; dr++) {
                            for (int dc = -1; dc <= 1; dc++) {
                                if (dr == 0 && dc == 0)
                                    continue;

                                int neighborR = r + dr;
                                int neighborC = c + dc;

                                if (neighborR >= 0 && neighborR < grid.length &&
                                        neighborC >= 0 && neighborC < grid[r].length) {
                                    if (grid[neighborR][neighborC] == '@') {
                                        rollNeighborCount++;
                                    }
                                }
                            }
                        }

                        if (rollNeighborCount < 4) {
                            collectedRolls.add(new Point(r, c));
                            grid[r][c] = '.';
                            changed = true;
                        }
                    }
                }
            }

        } while (changed);

        return String.valueOf(collectedRolls.size());
    }
}

static String solve(int part, List<String> lines) {
    return switch (part) {
        case 1 -> new Part1().compute(lines);
        case 2 -> new Part2().compute(lines);
        default -> throw new IllegalArgumentException("Invalid part: " + part);
    };
}
