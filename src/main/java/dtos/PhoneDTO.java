package dtos;


import entities.Phone;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Phone")
public class PhoneDTO {
  @Schema(required = true)
  private int number;
  @Schema(required = false)
  private String description;

  public PhoneDTO(){}

  public PhoneDTO(Phone phone) {
    this.number = phone.getNumber();
    this.description = phone.getDescription();
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



  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PhoneDTO{");
    sb.append(", number=").append(number);
    sb.append(", description='").append(description).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
