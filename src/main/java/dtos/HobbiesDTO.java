package dtos;

import entities.Hobby;
import java.util.ArrayList;
import java.util.List;

public class HobbiesDTO {

  private final List<HobbyDTO> all = new ArrayList<>();

  public HobbiesDTO(List<Hobby> hobbyEntities) {
    hobbyEntities.forEach((h) -> {
      all.add(new HobbyDTO(h.getName(), h.getDescription()));
    });
  }

  public List<HobbyDTO> getAll() {
    return all;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HobbyDTO{");
    sb.append("all=").append(all);
    sb.append('}');
    return sb.toString();
  }

}
