package entities;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "hobbies")
@NamedQueries({
    @NamedQuery(name = "Hobby.deleteAllRows", query = "DELETE from Hobby"),
    @NamedQuery(name = "Hobby.getAllRows", query = "SELECT h from Hobby h"),
    @NamedQuery(name = "Hobby.getHobby", query = "SELECT h from Hobby h WHERE h.id = :id")
})
public class Hobby implements Serializable {

  //Variables
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 175, nullable = false, unique = true)
  private String name;
  @Column(name = "des", length = 175, nullable = true, unique = false)
  private String description;

  @ManyToMany
  private List<Person> persons;


  //Constructors
  public Hobby() {
  }

  public Hobby(String name, String description) {
    this.name = name;
    this.description = description;
    this.persons = new ArrayList<>();
  }

  public Hobby(HobbyDTO dto){
    this.name = dto.getName();
    this.description = dto.getDescription();
    this.persons = new ArrayList<>();
  }

  public Hobby(String name) {
    this.name = name;
    this.persons = new ArrayList<>();
  }

  //Getter and setters
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

  public List<Person> getPersons(){
    return persons;
  }

  //Methods
  public void addPerson(Person person){
    if(person != null){
      this.persons.add(person);
      person.getHobbies().add(this);
    }
  }

  public void removePerson(Person person){
    if(person != null){
      this.persons.add(person);
      person.getHobbies().remove(this);
    }
  }

  @Override
  public String toString() {
    return "Hobby{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
  }


}
