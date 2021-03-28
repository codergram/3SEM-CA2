package entities;

import dtos.AddressDTO;
import dtos.PersonDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
@NamedQueries({
    @NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address"),
    @NamedQuery(name = "Address.getAllRows", query = "SELECT a from Address a"),
    @NamedQuery(name = "Address.getAddress", query = "SELECT a from Address a WHERE a.id = :id")
})
public class Address implements Serializable {

  //variables
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "street", length = 255, nullable = false, unique = false)
  private String street;
  @Column(name = "info", length = 255, nullable = true, unique = false)
  private String additionalInfo;

  @OneToMany(mappedBy = "address", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Person> persons;

  @ManyToOne
  private CityInfo cityInfo;

  //Constructors
  public Address() {
  }

  public Address(String street, String additionalInfo) {
    this.street = street;
    this.additionalInfo = additionalInfo;
  }

  public Address(String street, CityInfo cityInfo) {
    this.street = street;
    this.cityInfo = cityInfo;
  }

  public Address(AddressDTO dto){
    this.street = dto.getStreet();
    this.additionalInfo = dto.getAdditionalInfo();
    this.cityInfo = new CityInfo(dto.getCityInfo());
  }

  //getter and setters
  public List<Person> getPersons() {
    return persons;
  }

  public void setCityInfo(CityInfo cityInfo) {
    this.cityInfo = cityInfo;
  }

  public CityInfo getCityInfo() {
    return cityInfo;
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


  //Methods

  public void addPerson(Person person) {
    if (person != null) {
      person.setAddress(this);
      this.persons.add(person);
    }
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 73 * hash + Objects.hashCode(this.id);
    hash = 73 * hash + Objects.hashCode(this.street);
    hash = 73 * hash + Objects.hashCode(this.cityInfo);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Address other = (Address) obj;
    if (!Objects.equals(this.street, other.street)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.cityInfo, other.cityInfo)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Address{");
    sb.append("id=").append(id);
    sb.append(", street='").append(street).append('\'');
    sb.append(", additionalInfo='").append(additionalInfo).append('\'');
    sb.append(", persons=").append(persons);
    sb.append(", cityInfo=").append(cityInfo);
    sb.append('}');
    return sb.toString();
  }
}
