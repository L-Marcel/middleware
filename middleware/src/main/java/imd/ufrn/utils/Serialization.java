package imd.ufrn.utils;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Serialization {
  public static String serialize(Object object) throws JsonProcessingException {
    if(object instanceof String) return (String) object;
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(object);
  };

  public static <T> Optional<T> deserialize(String object, Class<T> _class) {
    try {
      if(object == null) return Optional.empty();
      ObjectMapper objectMapper = new ObjectMapper();
      return Optional.of(objectMapper.readValue(object, _class));
    } catch (Exception e) {
      return Optional.empty();
    }
  };
};
