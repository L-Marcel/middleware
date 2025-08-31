package imd.ufrn.utils;

import java.util.LinkedList;
import java.util.List;

public class Paths {
  public static List<String> match(
    String candidate, 
    String route
  ) {
    List<String> params = new LinkedList<>();
    String[] paths = candidate.split("/");
    String[] expecteds = route.split("/");

    if (paths.length != expecteds.length) {
      return null;
    };

    for (int i = 0; i < paths.length; i++) {
      String path = paths[i].trim();
      String expected = expecteds[i].trim();

      if (expected.startsWith(":")) {
        params.add(path.replace(":", ""));
      } else if (!path.equals(expected)) {
        return null;
      };
    };

    return params;
  };
};
