package dtos;

import entities.Person;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Person")
public class PersonDTO {
  //variables
  @Schema(required = false)
  private Long id;
  @Schema(required = true)
  private String email;
  @Schema(required = true)
  private String firstName;
  @Schema(required = true)
  private String lastName;
  @Schema(required = false)
  private List<PhoneDTO> phones;
  @Schema(required = false)
  private List<HobbyDTO> hobbies;
  @Schema(required = false)
  private AddressDTO address;

  public PersonDTO() {}

  public PersonDTO(Person person) {
    this.id = person.getId();
    this.email = person.getEmail();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.phones = person.getPhones() != null ? person.getNumberDTOList(person.getPhones()) : new ArrayList<>();
    this.hobbies = person.getHobbies() != null ? person.getHobbyDTOList(person.getHobbies()) : new ArrayList<>();
    this.address = person.getAddress() == null ? new AddressDTO() : new AddressDTO(person.getAddress());
  }

  public PersonDTO(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public PersonDTO(String email, String firstName, String lastName, List<PhoneDTO> phones) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phones = phones;
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
