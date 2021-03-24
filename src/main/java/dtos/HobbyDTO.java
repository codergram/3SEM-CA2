package dtos;

import entities.Hobby;

public class HobbyDTO {
  private Long id;
  private String name;
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
