package entities;

import static utils.Utility.*;
import static utils.Utility.convertList;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import utils.Utility;


@Entity
@Table(name = "persons")
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
    @NamedQuery(name = "Person.getAllRows", query = "SELECT p from Person p"),
    @NamedQuery(name = "Person.getPerson", query = "SELECT p from Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.deletePersonById", query = "DELETE FROM Person p WHERE p.id = :id")
})
public class Person implements Serializable {

  // Variables
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", length = 175, nullable = false, unique = true)
  private String email;
  @Column(name = "firstname", length = 175, nullable = false, unique = false)
  private String firstName;
  @Column(name = "lastname", length = 175, nullable = false, unique = false)
  private String lastName;

  @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Phone> phones;

  @ManyToOne
  private Address address;

  @ManyToMany(mappedBy = "persons", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Hobby> hobbies;

  //Constructers

  public Person() {
  }

  public Person(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phones = new ArrayList<>();
    this.hobbies = new ArrayList<>();
    this.address = null;
  }

  public Person(PersonDTO dto){
    this.email = dto.getEmail();
    this.firstName = dto.getFirstName();
    this.lastName = dto.getLastName();
    this.phones = dto.getPhones() != null ? getNumberList(dto.getPhones()) : new ArrayList<>();
    this.hobbies = dto.getHobbies() != null ? getHobbyList(dto.getHobbies()) : new ArrayList<>();
    this.address = dto.getAddress() != null ? new Address(dto.getAddress()) : null;
  }

  public Person(String email, String firstName, String lastName,
      List<Phone> phones, Address address, List<Hobby> hobbies) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phones = phones;
    this.address = address;
    this.hobbies = hobbies;
  }

  //Getter and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }

  public void setHobbies(List<Hobby> hobbies) {
    this.hobbies = hobbies;
  }

  public List<Phone> getPhones() {
    return phones;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Address getAddress() {
    return address;
  }

  public List<Hobby> getHobbies(){
    return hobbies;
  }


  //Methods
  public void addHobby(Hobby hobby){
    if(hobby != null){
      this.hobbies.add(hobby);
      hobby.getPersons().add(this);
    }
  }

  public void addPhone(Phone phone) {
    if (phone != null) {
      phone.setPerson(this);
      this.phones.add(phone);
    }
  }

  public void removeHobby(Hobby hobby){
    if(hobby != null){
      this.hobbies.remove(hobby);
      hobby.getPersons().remove(this);
    }
  }

  public List<Phone> getNumberList(List<PhoneDTO> phoneDTOS){
    ArrayList<Phone> list = new ArrayList<>();
      for (PhoneDTO p : phoneDTOS) {
        list.add(new Phone(p.getNumber()));
      }
    return list;
  }

  public List<PhoneDTO> getNumberDTOList(List<Phone> phones){
    ArrayList<PhoneDTO> list = new ArrayList<>();
    if (phones != null) {
      for (Phone p : phones) {
        list.add(new PhoneDTO(p.getNumber()));
      }
    }
    return list;
  }

  public List<Hobby> getHobbyList(List<HobbyDTO> hobbyDTOS){
    ArrayList<Hobby> list = new ArrayList<>();
    for(HobbyDTO h: hobbyDTOS){
      list.add(new Hobby(h.getName(), h.getDescription()));
    }
    return list;
  }

  public List<HobbyDTO> getHobbyDTOList(List<Hobby> hobby){
    ArrayList<HobbyDTO> list = new ArrayList<>();
    for(Hobby h: hobby){
      list.add(new HobbyDTO(h));
    }
    return list;
  }

  public void replaceHobbies(ArrayList<Hobby> hobbies) {
    this.hobbies = hobbies;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Person{");
    sb.append("id=").append(id);
    sb.append(", email='").append(email).append('\'');
    sb.append(", firstName='").append(firstName).append('\'');
    sb.append(", lastName='").append(lastName).append('\'');
    sb.append(", phones=").append(phones);
    sb.append(", address=").append(address);
    sb.append(", hobbies=").append(hobbies);
    sb.append('}');
    return sb.toString();
  }
}
