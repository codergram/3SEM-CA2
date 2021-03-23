package dtos;

import entities.Phone;
import java.util.ArrayList;
import java.util.List;

public class PhonesDTO {

  private final List<PhoneDTO> all = new ArrayList<>();

  public PhonesDTO(List<Phone> phoneEntities) {
    phoneEntities.forEach((p) -> {
      all.add(new PhoneDTO(p.getNumber()));
    });
  }

  public List<PhoneDTO> getAll() {
    return all;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PhonenDTO{");
    sb.append("all=").append(all);
    sb.append('}');
    return sb.toString();
  }

}
