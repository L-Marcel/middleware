package imd.ufrn.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Headers {
  private int length = 0;
  private String type = "";

  public void put(String key, String content) {
    switch (key) {
      case "Content-Length":
        this.length = Integer.parseInt(content);
        break;
      case "Content-Type":
        this.type = content;
        break;
      default:
        break;
    }
  };
};
