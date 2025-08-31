package imd.ufrn.data;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Content {
  public String serialize() 
    throws IOException, JsonProcessingException;
  public Content mount(String body) 
    throws IOException, JsonProcessingException;
};
