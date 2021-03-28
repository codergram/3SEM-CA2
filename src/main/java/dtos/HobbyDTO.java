package dtos;

import entities.Hobby;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Hobby")
public class HobbyDTO {
  @Schema(required = false)
  private Long id;
  @Schema(required = true)
  private String name;
  @Schema(required = false)
  private String description;

  public HobbyDTO(){}

  public HobbyDTO(Hobby hobby) {
    this.id = hobby.getId();
    this.name = hobby.getName();
    this.description = hobby.getDescription();
  }

  public HobbyDTO(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public HobbyDTO(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
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

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HobbyDTO{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
