package dtos;

import entities.CityInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CityInfo")
public class CityInfoDTO {
  @Schema(required = false)
  private Long id;
  @Schema(required = true)
  private String zipCode;
  @Schema(required = true)
  private String city;

  public CityInfoDTO(){}

  public CityInfoDTO(CityInfo entity) {
    this.id = entity.getId();
    this.city = entity.getCity();
    this.zipCode = entity.getZipCode();

  }

  public CityInfoDTO(String zipCode, String city) {
    this.zipCode = zipCode;
    this.city = city;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CityInfoDTO{");
    sb.append("id=").append(id);
    sb.append(", zipCode='").append(zipCode).append('\'');
    sb.append(", city='").append(city).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
