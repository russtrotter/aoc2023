package net.flatball.aoc;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Another p2 long day struggle.  p1 was fun and easy and a nice change from tree/combinatorics/graphs. P2
 * went right back in that territory and was almost saddening since it didn't felt like it used too much of
 * the same machinery as p1.  Skipped the parts list entirely to just focus on the organization of the flows.
 * After looking at some solutions I at least was on the right track, but the mechanics of iterating
 * through the rules chopping up the left/right of the ranges as we go might have kept eluding me.  I know
 * i'm learning (re-learning?) as I go, but the final verdict on this challenge will definitely include something
 * about my lacking/rusty algorithmic skills.
 */
public class D19 implements AOC {
  static final String ACCEPT = "A";
  static final String REJECT = "R";

  record Range(int min, int max) {}

  record Pair<T>(T left, T right) {}

  enum Category {
    x, m, a, s
  }

  static class Flow {
    final List<Rule> rules = new ArrayList<>();
  }

  static class Part extends EnumMap<Category, Integer> {
    public Part() {
      super(Category.class);
    }
  }

  static class Rule {
    String next;
    Category category;
    Integer operand;
    char operator;
    BiFunction<Integer, Integer, Boolean> condition;

    String eval(Part part) {
      if (condition == null) {
        return next;
      }
      if (operand == null) {
        throw new IllegalStateException(" operand");
      }
      if (category == null) {
        throw new IllegalStateException(" category");
      }
      Integer partValue = part.get(category);
      if (partValue == null) {
        throw new IllegalStateException(" value for operand: " + operand);
      }
      return condition.apply(partValue, operand) ? next : null;
    }

    static Rule create(String rulePart) {
      final Rule rule = new Rule();
      if (ACCEPT.equals(rulePart)) {
        rule.next = ACCEPT;
        return rule;
      }
      if (REJECT.equals(rulePart)) {
        rule.next = REJECT;
        return rule;
      }
      int idx = rulePart.indexOf(':');
      if (idx == -1) {
        rule.next = rulePart;
        return rule;
      }
      rule.category = Category.valueOf(rulePart.substring(0, 1));
      rule.operator = rulePart.charAt(1);
      rule.condition = switch (rule.operator) {
        case '<' -> (a,b) -> (a < b);
        case '>' -> (a,b) -> (a > b);
        default -> throw new IllegalStateException("invalid operator " + rule.operator);
      };
      rule.operand = Integer.parseInt(rulePart.substring(2, idx));
      rule.next = rulePart.substring(idx + 1);
      return rule;
    }
  }

  final SequencedMap<String, Flow> flows = new LinkedHashMap<>();
  final List<Part> parts = new ArrayList<>();


  static class Combo extends EnumMap<Category, Range> {
    public Combo() {
      super(Category.class);
      for (var cat : Category.values()) {
        put(cat, new Range(1, 4000));
      }
    }

    public Combo(Combo other) {
      super(other);
    }

    long count() {
      return this.values().stream().map(r -> (long)(r.max - r.min + 1)).reduce(1L, (a, b) -> (a * b));
    }

    Pair<Combo> splitBelow(Category category, int mid) {
      final Range range = get(category);
      if (range == null) {
        throw new IllegalStateException("weird no range for: " + category);
      }
      if (mid <= range.min) {
        return new Pair<>(null, this);
      }
      if (mid > range.max) {
        return new Pair<>(this, null);
      }
      final Range lo = new Range(range.min, mid - 1);
      final Combo left = new Combo(this);
      left.put(category, lo);
      final Range hi = new Range(mid, range.max);
      final Combo right = new Combo(this);
      right.put(category, hi);
      return new Pair<>(left, right);
    }

    Pair<Combo> splitAbove(Category category, int mid) {
      final Range range = get(category);
      if (mid < range.min) {
        return new Pair<>(null, this);
      }
      if (mid >= range.max) {
        return new Pair<>(this, null);
      }
      final Range lo = new Range(range.min, mid);
      final Combo left = new Combo(this);
      left.put(category, lo);
      final Range hi = new Range(mid + 1, range.max);
      final Combo right = new Combo(this);
      right.put(category, hi);
      return new Pair<>(left, right);
    }
  }
  record Node(String flowName, Combo combo) {}

  void tree() {
    long accepted = 0L;
    final Deque<Node> queue = new ArrayDeque<>();
    queue.push(new Node("in", new Combo()));
    while (!queue.isEmpty()) {
      final Node node = queue.remove();
      Combo ratings = node.combo;
      if (ACCEPT.equals(node.flowName)) {
        accepted += ratings.count();
        continue;
      } else if (REJECT.equals(node.flowName)) {
        continue;
      }
      final Flow flow = flows.get(node.flowName);
      if (flow == null) {
        throw new IllegalStateException("no flow: " + node.flowName);
      }

      for (Rule rule : flow.rules) {
        // last rule has no condition
        if (rule.condition == null) {
          if (ratings != null) {
            queue.addLast(new Node(rule.next, ratings));
          }
          continue;
        }
        if (rule.operator == '<') {
          final Pair<Combo> pair = ratings.splitBelow(rule.category, rule.operand);
          if (pair.left != null) {
            queue.addLast(new Node(rule.next, pair.left));
            ratings = pair.right;
          }
        } else if (rule.operator == '>') {
          final Pair<Combo> pair = ratings.splitAbove(rule.category, rule.operand);
          if (pair.right != null) {
            queue.addLast(new Node(rule.next, pair.right));
            ratings = pair.left;
          }
        }
        if (ratings == null) {
          break;
        }
      }
    }
    System.out.println("ACCEPTED TOTAL : " + accepted);
  }

  @Override
  public void run(int part, List<String> lines) {
    parse(lines);
    System.out.println("FLOWS count " + flows.size());
    System.out.println("PARTS count " + parts.size());
    if (flows.isEmpty()) {
      throw new IllegalStateException(" flows bro");
    }
    if (part == 2) {
      tree();
      return;
    }

    long total = 0L;
    for (Part p : parts) {
      System.out.print("PART " + p + ": in");
      String next = "in";
      while (next != null) {
        Flow flow = flows.get(next);
        if (flow == null) {
          throw new IllegalStateException(" flow named: " + next);
        }
        for (Rule rule : flow.rules) {
          next = rule.eval(p);
          if (ACCEPT.equals(next)) {
            System.out.print(" -> " + next);
            total += p.values().stream().reduce(0, Integer::sum);
            next = null;
            break;
          } else if (REJECT.equals(next)) {
            System.out.print(" -> " + next);
            next = null;
            break;
          } else if (next != null) {
            System.out.print(" -> " + next);
            break;
          }
        }
      }
      System.out.println();
    }
    System.out.println("PART " + part + " total is " + total);
  }


  void parseRules(Flow flow, String ruleBody) {
    for (String rulePart : ruleBody.split(",")) {
      flow.rules.add(Rule.create(rulePart));
    }
    if (flow.rules.isEmpty()) {
      throw new IllegalStateException("no rules");
    }
    if (flow.rules.subList(0, flow.rules.size() - 1).stream().anyMatch(rule -> rule.condition == null)) {
      throw new IllegalStateException("rules before last should have condition");
    }
    if (flow.rules.getLast().condition != null) {
      throw new IllegalStateException("should not have condition on last rule");
    }
  }

  void parseFlow(String line) {
    int idx = line.indexOf('{');
    if (idx == -1) {
      throw new IllegalStateException("missing rule left");
    }
    final Flow flow = new Flow();
    this.flows.put(line.substring(0, idx), flow);
    int idx2 = line.lastIndexOf('}');
    if (idx2 == -1) {
      throw new IllegalStateException("missing rule right");
    }
    final String ruleBody = line.substring(idx + 1, idx2);
    parseRules(flow, ruleBody);
  }

  void parsePart(String line) {
    final Part part = new Part();
    parts.add(part);
    final String body = line.substring(1, line.length() - 1);
    for (String tuple : body.split(",")) {
      int idx = tuple.indexOf('=');
      if (idx == -1) {
        throw new IllegalStateException("missing equal");
      }
      Category category = Category.valueOf(tuple.substring(0, idx));
      int value = Integer.parseInt(tuple.substring(idx + 1));
      if (part.put(category, value) != null) {
        throw new IllegalStateException("dupe part category");
      }
    }
  }

  void parse(List<String> lines) {
    int lineIndex = 0;
    for (; lineIndex < lines.size(); lineIndex++) {
      final String line = lines.get(lineIndex);
      if (line.isEmpty()) {
        lineIndex++;
        break;
      }
      parseFlow(line);
    }
    for (; lineIndex < lines.size(); lineIndex++) {
      parsePart(lines.get(lineIndex));
    }
  }
}
