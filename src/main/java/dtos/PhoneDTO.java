package dtos;


import entities.Phone;

public class PhoneDTO {
  private Long id;
  private int number;
  private String description;
  private PersonDTO person;

  public PhoneDTO(){}

  public PhoneDTO(Phone phone) {
    this.id = phone.getId();
    this.number = phone.getNumber();
    this.description = phone.getDescription();
    this.person = phone.getPerson() == null ? null : new PersonDTO(phone.getPerson());
  }

  public PhoneDTO(int number) {
    this.number = number;
  }

  public PhoneDTO(int number, String description) {
    this.number = number;
    this.description = description;
  }

  public PhoneDTO(int number, String description, PersonDTO person) {
    this.number = number;
    this.description = description;
    this.person = person;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PersonDTO getPerson() {
    return person;
  }

  public void setPerson(PersonDTO person) {
    this.person = person;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PhoneDTO{");
    sb.append("id=").append(id);
    sb.append(", number=").append(number);
    sb.append(", description='").append(description).append('\'');
    sb.append(", person=").append(person);
    sb.append('}');
    return sb.toString();
  }
}
