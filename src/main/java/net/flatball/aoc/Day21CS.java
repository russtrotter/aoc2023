package net.flatball.aoc;

import java.util.List;
// adaptation of CSharp solution repo from:
// https://github.com/villuna/aoc23/tree/main
public class Day21CS implements AOC {
    static final long TargetSteps = 26501365L;

    private char[][] _map;

    private int _width;

    private int _height;

    @Override
    public void run ( int part, List<String > lines){
    ParseInput(lines);
    FillUnreachable();
    var result = ExtrapolateAnswer();
    System.out.println("CSHARP says " + result);
  }

    protected long ExtrapolateAnswer ()
    {
      var halfWidth = _width / 2;

      var countHalfWidth = CountAtStep(halfWidth);

      var countWidthPlusHalf = CountAtStep(halfWidth + _width);

      var delta1 = countWidthPlusHalf - countHalfWidth;

      var delta2 = CountAtStep(halfWidth + _width * 2) - countWidthPlusHalf;

      var quotient = TargetSteps / _width;

      var result = countHalfWidth + delta1 * quotient + quotient * (quotient - 1) / 2 * (delta2 - delta1);

      return result;
    }

    protected void FillUnreachable ()
    {
      for (var y = 1; y < _height - 2; y++) {
        for (var x = 1; x < _width - 2; x++) {
          if (_map[x - 1][y] =='#' && _map[x + 1][y] =='#' && _map[x][y - 1] =='#' && _map[x][y + 1] =='#')
          {
            _map[x][y] ='#';
          }
        }
      }
    }

    protected int CountAtStep ( int step)
    {
      var count = 0;

      var yD = 0;

      for (var x = -step; x <= step; x++) {
        for (var y = -yD; y <= yD; y += 2) {
          if (CheckMap(65 + x, 65 + y)) {
            count++;
          }
        }

        if (x < 0) {
          yD++;
        } else {
          yD--;
        }
      }

      return count;
    }

    private boolean CheckMap ( int x, int y)
    {
      while (x < 0) {
        x += 131;
      }

      while (x > 130) {
        x -= 131;
      }

      while (y < 0) {
        y += 131;
      }

      while (y > 130) {
        y -= 131;
      }

      return _map[x][y] !='#';
    }

    protected void ParseInput (List<String> lines)
    {
      _width = lines.getFirst().length();

      _height = lines.size();

      _map = new char[_width][_height];

      var y = 0;

      for (String line : lines)
      {
        for (var x = 0; x < line.length(); x++) {
          _map[x][y] =line.charAt(x);

          if (line.charAt(x) == 'S') {
            _map[x][y] ='.';
          }
        }

        y++;
      }
    }
  }



