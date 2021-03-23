package utils;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class Utility {

  public static boolean ValidatePerson(Person p){
    return p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null;
  }

  public static boolean ValidatePersonDto(PersonDTO p){
    System.out.println("Validation: " + p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null);
    return p.getEmail() != null || p.getFirstName() != null || p.getLastName() != null;
  }

  public static List convertList(Class<?> type, List list){
    List l = new ArrayList();
    for(Object p: list){
      if(type == Phone.class) l.add(new Phone((PhoneDTO) p));
      if(type == Hobby.class) l.add(new Hobby((HobbyDTO) p));
    }
    return l;
  }

}
