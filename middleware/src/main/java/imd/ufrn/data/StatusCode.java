package imd.ufrn.data;

import lombok.Getter;

@Getter
public enum StatusCode {
  OK(200, "Ok"),
  CREATED(201, "Created"),
  BAD_REQUEST(400, "Bad Request"),
  NOT_FOUND(404, "Not Found"),
  CONFLICT(409, "Conflict"),
  INTERNAL_SERVER_ERROR(500, "Internal Server Error");

  private int value;
  private String name;

  private StatusCode(int value, String name) {
    this.value = value;
    this.name = name;
  };

  public static StatusCode ofName(String name) {
    for(StatusCode code : StatusCode.values()) {
      if(code.getName().equals(name)) return code;
    };

    return StatusCode.BAD_REQUEST;
  };

  public static StatusCode ofValue(int value) {
    for(StatusCode code : StatusCode.values()) {
      if(code.getValue() == value) return code;
    };

    return StatusCode.BAD_REQUEST;
  };

  @Override
  public String toString() {
    return Integer.toString(this.getValue());
  };
};
