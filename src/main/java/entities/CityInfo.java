package entities;

import dtos.AddressDTO;
import dtos.CityInfoDTO;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import utils.Utility;


@Entity
@Table(name = "cityinfo")
@NamedQueries({
    @NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo"),
    @NamedQuery(name = "CityInfo.getAllRows", query = "SELECT ci from CityInfo ci"),
    @NamedQuery(name = "CityInfo.getCityInfo", query = "SELECT ci from CityInfo ci WHERE ci.id = :id")
})
public class CityInfo implements Serializable {

  //variables
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "zipcode", length = 4, unique = true)
  private String zipCode;
  @Column(name = "city", length=35, unique = false)
  private String city;

  @OneToMany(mappedBy = "cityInfo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Address> addresses;


  //Constructors
  public CityInfo() {
  }

  public CityInfo(long id, String zipCode, String city) {
    this.id = id;
    this.zipCode = zipCode;
    this.city = city;
    this.addresses = new ArrayList<>();
  }

  public CityInfo(String zipCode, String city) {
    this.zipCode = zipCode;
    this.city = city;
  }

  public CityInfo(String zipCode) {
    this.zipCode = zipCode;
  }

  public CityInfo(CityInfoDTO dto){
    this.zipCode = dto.getZipCode();
    this.city = dto.getCity();
  }

  //getter and setters

  public List<Address> getAddresses() {
    return addresses;
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

  public String getCity() {
    return city;
  }


  public void addAddress(Address address) {
    if (address != null) {
      address.setCityInfo(this);
      this.addresses.add(address);
    }
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    hash = 97 * hash + Objects.hashCode(this.zipCode);
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
    final CityInfo other = (CityInfo) obj;
    if (!Objects.equals(this.zipCode, other.zipCode)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CityInfo{" + "id=" + id + ", zipCode=" + zipCode + ", city=" + city + '}';
  }


}
