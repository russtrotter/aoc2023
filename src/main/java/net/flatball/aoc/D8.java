package net.flatball.aoc;

import java.util.*;

/**
 * ugh, this was frustrating in that part2 was just taking "too long"(tm) and i thought i must be
 * missing some key part of the instructions.  I carefully googled some takes on the part 2 directions
 * to see if there were some details i was missing without just spoiling the technique.  Darn it if it
 * was just right there to calculate the p2 answer with LCM. The final answer is 13,830,919,117,339.
 * yes, trillions.  However, it's kinda lame since you basically
 * write code with the basic algo to produce enough iterations to confirm
 * that the input data is carefully crafted to produce specific intervals for each starting number.  I was
 * kinda hoping these challenges would reward the fast/clever/elegant, punish the naive, particularly on p2.
 * Reflecting back being a bit less critical, a lesson of this challenge is to focus on getting to the solution
 * with whatever shortcuts (general-purpose or not).
 *
 * After more googling, i added some escape hatch hacky code to detect when the original XXA path got to the same XXZ
 * and print those values.  Plugging those values into an online LCM calc resulted in the final answer.  Yeesh.
 *
 * I saw a comment on the reddit thread mentioning that the direction string repeating itself should have been a
 * trigger that "ah! cycles in the data are in play".  The consensus seems to agree that LCM won't always work for
 * every possible input data set, but for purposes of AOC, these kinds of combo of carefully framed problem with
 * carefully made data results in these revelations.
 */
public class D8 implements AOC {
  record Node (String left, String right, Set<Integer> silly) {}
  final Map<String, Node> nodes = new HashMap<>();

  @Override
  public void run(int part, List<String> lines) {
    final String directions = lines.getFirst();
    for (int li = 2; li < lines.size(); li++) {
      final String line = lines.get(li);
      final String loc = line.substring(0, 3);
      nodes.put(loc, new Node(line.substring(7, 10), line.substring(12, 15), new HashSet<>()));
    }
    System.out.println("NODES: " + nodes.size());
    final List<String> addrs = new ArrayList<>(part == 1? List.of("AAA") : nodes.keySet().stream().filter(k -> k.endsWith("A")).toList());
    //final List<String> addrs = new ArrayList<>(List.of("BLA", "TGA", "AAA", "PQA"));
    System.out.println("ADDRS: " + addrs);
    int dirIndex = 0;
    long stepCount = 0;
    int endCount = 0;
    final Set<Integer> cycles = new HashSet<>();
    for (; endCount < addrs.size() && cycles.size() < addrs.size(); ) {
      if ((stepCount % 10000000000L) == 0) {
        System.out.println("STEP CHECK: " + stepCount);
      }
      //System.out.println("ADDR: " + addr);
      char dir = directions.charAt(dirIndex++);
      if (dirIndex >= directions.length()) {
        dirIndex = 0;
      }
      endCount = 0;
      stepCount++;
      for (int addrIndex = 0; addrIndex < addrs.size();addrIndex++) {
        String addr = addrs.get(addrIndex);
        final Node node = nodes.get(addr);
        if (node == null) {
          throw new IllegalStateException("huh: " + addr);
        }
        addr = switch (dir) {
          case 'L' -> node.left;
          case 'R' -> node.right;
          default -> throw new IllegalStateException("bad dir: " + dir);
        };
        if (part == 1 ? addr.equals("ZZZ") : addr.charAt(2) == 'Z') {
          final Node foo = nodes.get(addr);
          if (foo.silly.contains(addrIndex)) {
            System.out.println("ZCYCLE on " + addrIndex + "=" + stepCount);
            cycles.add(addrIndex);
          }
          foo.silly.add(addrIndex);
          //System.out.println("Z " + addrIndex + "=" + stepCount);
          endCount++;
        }
        addrs.set(addrIndex, addr);
      }
    }
    System.out.println("PART " + part + " in " + stepCount + " steps");
  }
}
