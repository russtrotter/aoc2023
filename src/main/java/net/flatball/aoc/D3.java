package net.flatball.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class D3 {
  static class Adj {
    int count = 0;
    int ratio = 1;

    boolean update(List<int[]> lineValues, int lineIndex, int pos) {
      if (lineIndex >= 0 && lineIndex < lineValues.size()) {
        final int[] values = lineValues.get(lineIndex);
        if (pos >= 0 && pos < values.length && values[pos] > 0) {
          this.ratio *= values[pos];
          this.count++;
          return true;
        }
      }
      return false;
    }
  }

  boolean symbol(String line, int begin, int end) {
    for (int i = Math.max(0, begin - 1); i < Math.min(line.length(), end + 1); i++) {
      char c = line.charAt(i);
      if (c != '.' && !Character.isDigit(c)) {
        return true;
      }
    }
    return false;
  }

  int[] parse(String line) {
    final int[] values = new int[line.length()];
    for (int pos = 0; pos < line.length();) {
      char c = line.charAt(pos);
      if (Character.isDigit(c)) {
        int end = pos + 1;
        for (; end < line.length(); end++) {
          c = line.charAt(end);
          if (!Character.isDigit(c)) {
            break;
          }
        }
        final int pn = Integer.parseInt(line.substring(pos, end));
        for (int i = pos; i < end; i++) {
          values[i] = pn;
        }
        pos = end;
      } else if (c == '*') {
        values[pos] = 0;
        pos++;
      } else {
        values[pos] = -1;
        pos++;
      }
    }
    return values;
  }




  void part2(List<String> lines) {
    int total = 0;
    final List<int[]> parsed  = lines.stream().map(this::parse).toList();
    for (int lineIndex = 0; lineIndex < parsed.size(); lineIndex++) {
      final int[] values = parsed.get(lineIndex);
      for (int pos = 0; pos < values.length; pos++) {
        if (values[pos] == 0) {
          final Adj adj = new Adj();
          if (!adj.update(parsed, lineIndex - 1, pos)) {
            adj.update(parsed, lineIndex - 1, pos - 1);
            adj.update(parsed, lineIndex - 1, pos + 1);
          }
          adj.update(parsed, lineIndex, pos - 1);
          adj.update(parsed, lineIndex, pos + 1);
          if (!adj.update(parsed, lineIndex + 1, pos)) {
            adj.update(parsed, lineIndex + 1, pos - 1);
            adj.update(parsed, lineIndex + 1, pos + 1);
          }
          if (adj.count == 2) {
            System.out.println("ADJ: " + (lineIndex + 1) + ":" + (pos + 1) + "=" + adj.count + " adjacancies");
            total += adj.ratio;
          }
        }
      }
    }
    System.out.println("PART2 TOTAL=" + total);
  }

  void part1(List<String> lines) {
    int total = 0;
    Integer lineLength = null;
    for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
      final String line = lines.get(lineIndex);
      if (lineLength == null) {
        lineLength = line.length();
      } else if (line.length() != lineLength) {
        throw new IllegalStateException("line length mismatch: " + lineIndex);
      }
      char prev = 0;
      for (int pos = 0; pos < line.length(); ) {
        char c = line.charAt(pos);
        if (Character.isDigit(c)) {
          boolean pn = prev != '.' && !Character.isDigit(prev);
          int end = pos + 1;
          for (; end < line.length(); end++) {
            c = line.charAt(end);
            if (!Character.isDigit(c)) {
              if (c != '.') {
                pn = true;
              }
              break;
            }
          }
          if (!pn) {
            if (lineIndex >= 1) {
              pn = symbol(lines.get(lineIndex - 1), pos, end);
            }
            if (!pn && lineIndex < (lines.size() - 1)) {
              pn = symbol(lines.get(lineIndex + 1), pos, end);
            }
          }
          if (pn) {
            int partNumber = Integer.parseInt(line.substring(pos, end));
            total += partNumber;
          }
          pos = end;
          c = 0;
        } else if (c == '.') {
          pos++;
        } else {
          pos++;
        }
        prev = c;
      }
    }
    System.out.println("PART1 total: " + total);
  }
}
