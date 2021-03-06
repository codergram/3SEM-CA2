package dtos;
import entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Address")
public class AddressDTO {

  @Schema(required = false, example = "3")
  private Long id;
  @Schema(required = true)
  private String street;
  @Schema(required = false)
  private String additionalInfo;
  @Schema(required = true)
  private CityInfoDTO cityInfo;

  public AddressDTO() {
  }

  public AddressDTO(Address entity) {
    this.id = entity.getId() == null ? null : entity.getId();
    this.street = entity.getStreet() == null ? null : entity.getStreet();
    this.additionalInfo = entity.getAdditionalInfo();
    this.cityInfo = entity.getCityInfo() == null ? null : new CityInfoDTO(entity.getCityInfo());
  }

  public AddressDTO(String street, String additionalInfo) {
    this.street = street;
    this.additionalInfo = additionalInfo;
  }

  public AddressDTO(String street, String additionalInfo, CityInfoDTO cityInfo) {
    this.street = street;
    this.additionalInfo = additionalInfo;
    this.cityInfo = cityInfo;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }


  public CityInfoDTO getCityInfo() {
    return cityInfo;
  }

  public void setCityInfo(CityInfoDTO cityInfo) {
    this.cityInfo = cityInfo;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AddressDTO{");
    sb.append("id=").append(id);
    sb.append(", street='").append(street).append('\'');
    sb.append(", additionalInfo='").append(additionalInfo).append('\'');
    sb.append(", cityInfo=").append(cityInfo);
    sb.append('}');
    return sb.toString();
  }
}
