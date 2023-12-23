package net.flatball.aoc;

import java.util.*;
import java.util.function.BiFunction;

public class D19 implements AOC {

  enum Status {
    A,
    R,
    CONTINUE
  }

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

  static class Context {
    Status status = Status.CONTINUE;
    String flow = "in";
  }

  static class Rule {
    Status status = Status.CONTINUE;
    Category category;
    Integer operand;
    BiFunction<Integer, Integer, Boolean> condition;
    String nextFlow;

    void eval(Context ctx, Map<Category, Integer> part) {
      if (condition != null) {
        if (operand == null) {
          throw new IllegalStateException("no operand");
        }
        if (category == null) {
          throw new IllegalStateException("no category");
        }
        Integer partValue = part.get(category);
        if (partValue == null) {
          throw new IllegalStateException("no value for operand: " + operand);
        }
        if (condition.apply(partValue, operand)) {
          next(ctx);
        }
        return;
      }
      next(ctx);
    }

    void next(Context ctx) {
      if (status != null) {
        ctx.status = status;
      }
      if (nextFlow != null) {
        ctx.flow = nextFlow;
      }
    }
  }

  final SequencedMap<String, Flow> flows = new LinkedHashMap<>();
  final List<Part> parts = new ArrayList<>();


  @Override
  public void run(int part, List<String> lines) {
    parse(lines);
    System.out.println("FLOWS count " + flows.size());
    System.out.println("PARTS count " + parts.size());
    if (flows.isEmpty()) {
      throw new IllegalStateException("no flows bro");
    }
    long total = 0L;
    for (Part p : parts) {
      System.out.print("PART " + p + ": in");
      final Context ctx = new Context();
      while (ctx.status == Status.CONTINUE) {
        Flow flow = flows.get(ctx.flow);
        if (flow == null) {
          throw new IllegalStateException("no flow named: " + ctx.flow);
        }
        ctx.flow = null;
        for (Rule rule : flow.rules) {
          rule.eval(ctx, p);
          if (ctx.status == Status.A) {
            System.out.print(" -> " + ctx.status);
            total += p.values().stream().reduce(0, Integer::sum);
            break;
          } else if (ctx.status == Status.R) {
            System.out.print(" -> " + ctx.status);
            break;
          } else if (ctx.flow != null) {
            System.out.print(" -> " + ctx.flow);
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
      final Rule rule = new Rule();
      flow.rules.add(rule);
      if (Status.A.name().equals(rulePart)) {
        rule.status = Status.A;
        continue;
      } else if (Status.R.name().equals(rulePart)) {
        rule.status = Status.R;
        continue;
      }
      int idx = rulePart.indexOf(':');
      if (idx == -1) {
        rule.nextFlow = rulePart;
        continue;
      }
      final String dest = rulePart.substring(idx + 1);
      if (Status.A.name().equals(dest)) {
        rule.status = Status.A;
      } else if (Status.R.name().equals(dest)) {
        rule.status = Status.R;
      } else {
        rule.nextFlow = dest;
      }
      rule.category = Category.valueOf(rulePart.substring(0, 1));
      if (rulePart.charAt(1) == '<') {
        rule.condition = (a, b) -> a < b;
      } else if (rulePart.charAt(1) == '>') {
        rule.condition = (a, b) -> a > b;
      } else {
        throw new IllegalStateException("bad condition operator: " + rulePart);
      }
      rule.operand = Integer.parseInt(rulePart.substring(2, idx));
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
