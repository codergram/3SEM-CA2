package dtos;

import entities.Hobby;
import entities.Person;
import java.util.ArrayList;
import java.util.List;

public class HobbyDTO {
  private Long id;
  private String name;
  private String description;
  private List<PersonDTO> persons;

  public HobbyDTO(){}

  public HobbyDTO(Hobby hobby) {
    this.id = hobby.getId();
    this.name = hobby.getName();
    this.description = hobby.getDescription();
    this.persons = new ArrayList<>();
    for(Person p: hobby.getPersons()){
      this.persons.add(new PersonDTO(p));
    }
  }

  public HobbyDTO(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public HobbyDTO(String name, String description, List<PersonDTO> persons) {
    this.name = name;
    this.description = description;
    this.persons = persons;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<PersonDTO> getPersons() {
    return persons;
  }

  public void setPersons(List<PersonDTO> persons) {
    this.persons = persons;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HobbyDTO{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", persons=").append(persons);
    sb.append('}');
    return sb.toString();
  }
}
