package dtos;

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
  private List<PhoneDTO> phones;
  private AddressDTO address;
  private List<HobbyDTO> hobbies;

  public PersonDTO() {}

  public PersonDTO(Person person) {
    this.id = person.getId();
    this.email = person.getEmail();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.phones = new ArrayList<>();


    this.hobbies = new ArrayList<>();

    this.address = person.getAddress() == null ? null : new AddressDTO(person.getAddress());
  }

  public PersonDTO(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public PersonDTO(String email, String firstName, String lastName,
      List<PhoneDTO> phones, AddressDTO address, List<HobbyDTO> hobbies) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phones = phones;
    this.address = address;
    this.hobbies = hobbies;
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
    return phones;
  }

  public void setPhones(List<PhoneDTO> phones) {
    this.phones = phones;
  }

  public List<Phone> getPhoneList(){
    List<Phone> list = new ArrayList<>();
    for(PhoneDTO p: phones){
      list.add(new Phone(p));
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
    return hobbies;
  }

  public void setHobbies(List<HobbyDTO> hobbies) {
    this.hobbies = hobbies;
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
