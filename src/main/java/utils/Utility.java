package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Utility {


  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

  public static boolean ValidatePerson(Person p){
    return p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null;
  }

  public static String encodeUrl(String str){
    try {
      return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex.getCause());
    }
  }

  public static boolean ValidatePersonDto(PersonDTO p){
    System.out.println("Validation: " + p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null);
    return p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null;
  }

  public static List convertList(Class<?> type, List list){
    List l = new ArrayList();
    for(Object p: list){
      if(type == Phone.class) l.add(new Phone((PhoneDTO) p));
      if(type == PhoneDTO.class) l.add(new PhoneDTO((Phone) p));
      if(type == Hobby.class) l.add(new Hobby((HobbyDTO) p));
      if(type == HobbyDTO.class) l.add(new HobbyDTO((Hobby) p));
      if(type == CityInfo.class) l.add(new CityInfoDTO((CityInfo) p));
      if(type == PersonDTO.class) l.add(new PersonDTO((Person) p));
    }
    return l;
  }

}
