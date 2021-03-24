package dtos;

import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PersonDTO {
  //variables
  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private PhonesDTO phones;
  private AddressDTO address;
  private HobbiesDTO hobbies;

  public PersonDTO() {}

  public PersonDTO(Person person) {
    this.id = person.getId();
    this.email = person.getEmail();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.phones = person.getPhones() == null ? null : new PhonesDTO(person.getPhones());
    this.hobbies = person.getHobbies() == null ? null : new HobbiesDTO(person.getHobbies());
    this.address = person.getAddress() == null ? null : new AddressDTO(person.getAddress());
  }

  public PersonDTO(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }


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

  public List<PhoneDTO> getPhones() {
    return phones.getAll();
  }

  public List<Phone> getNumberList(){
    ArrayList<Phone> list = new ArrayList<>();
    for(PhoneDTO p: this.getPhones()){
      list.add(new Phone(p.getNumber()));
    }
    return list;
  }

  public List<Hobby> getHobbyList(){
    ArrayList<Hobby> list = new ArrayList<>();
    for(HobbyDTO h: this.getHobbies()){
      list.add(new Hobby(h.getName(), h.getDescription()));
    }
    return list;
  }


  public AddressDTO getAddress() {
    return address;
  }

  public void setAddress(AddressDTO address) {
    this.address = address;
  }

  public List<HobbyDTO> getHobbies() {
    return hobbies.getAll();
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PersonDTO{");
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
