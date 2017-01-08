import java.awt.Point;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;


/**
 * Created by dsteeber on 12/30/2016.
 */
public class JavaDay22 {

        private static class Node extends Point {
            final int size;
            final int used;
            final int avail;

            public Node(int x, int y, int size, int used, int avail) {
                super(x, y);
                this.size = size;
                this.used = used;
                this.avail = avail;
            }
        }

        private static List<Node> parse(List<String> input) {
            return input.stream().map(s -> {
                Matcher m = Pattern.compile("(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)").matcher(s);
                if (m.find())
                    return new Node(Integer.valueOf(m.group(1)),
                            Integer.valueOf(m.group(2)),
                            Integer.valueOf(m.group(3)),
                            Integer.valueOf(m.group(4)),
                            Integer.valueOf(m.group(5)));
                else return null;
            }).filter(n -> n != null).collect(Collectors.toList());
        }

        private static long partOne(List<Node> s) {
            return s.stream().filter(n -> n.used > 0).mapToLong(n -> s.stream().filter(other -> !n.equals(other) && other.avail >= n.used).count()).sum();
        }

        private static long partTwo(List<Node> s) {
            int x_size = s.stream().max(Comparator.comparing(n -> n.x)).get().x;
            int y_size = s.stream().max(Comparator.comparing(n -> n.y)).get().y;
            Node wStart = null, hole = null;
            Node[][] nodes = new Node[x_size + 1][y_size + 1];
            s.forEach(n -> nodes[n.x][n.y] = n);
            for (int x = 0; x < nodes.length; x++) {
                for (int y = 0; y < nodes[x].length; y++) {
                    Node n = nodes[x][y];
                    if (x == 0 && y == 0)
                        System.out.print("S");
                    else if (x == x_size && y == 0)
                        System.out.print("G");
                    else if (n.used == 0) {
                        hole = n;
                        System.out.print("_");
                    } else if (n.size > 250) {
                        if (wStart == null)
                            wStart = nodes[x - 1][y];
                        System.out.print("#");
                    } else
                        System.out.print(".");
                }
                System.out.println();
            }
            int result = Math.abs(hole.x - wStart.x) + Math.abs(hole.y - wStart.y);
            result += Math.abs(wStart.x - x_size) + wStart.y;
            return result + 5 * (x_size - 1);
        }

        public static void main(String[] args) throws IOException {
            List<String> s = Files.readAllLines(Paths.get("c:\\Users\\dsteeber\\IdeaProjects\\aoc2016\\resources\\day22.data"));
            System.out.println("Part One = " + partOne(parse(s)));
            System.out.println("Part One = " + partTwo(parse(s)));
        }
    }


