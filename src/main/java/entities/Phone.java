package entities;

import dtos.PersonDTO;
import dtos.PhoneDTO;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import utils.Utility;


@Entity
@Table(name = "phones")
@NamedQueries({
    @NamedQuery(name = "Phone.deleteAllRows", query = "DELETE from Phone"),
    @NamedQuery(name = "Phone.getAllRows", query = "SELECT p from Phone p"),
    @NamedQuery(name = "Phone.getPhone", query = "SELECT p from Phone p WHERE p.id = :id")
})
public class Phone implements Serializable {

  //variables
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "number", length = 8, nullable = false, unique = true)
  private int number;
  @Column(name = "des", length = 175, nullable = true, unique = false)
  private String description;

  @ManyToOne
  private Person person;

  //Constructors

  public Phone() {
  }

  public Phone(int number) {
    this.number = number;
  }

  public Phone(int number, String description) {
    this.number = number;
    this.description = description;
  }

  public Phone(PhoneDTO dto){
    this.number = dto.getNumber();
    this.description = dto.getDescription();
  }

  //Getter and setters

  public void setPerson(Person person) {
    this.person = person;
  }

  public Person getPerson() {
    return person;
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

  //Methods

  @Override
  public int hashCode() {
    int hash = 3;
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
    final Phone other = (Phone) obj;
    if (this.number != other.number) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }



  @Override
  public String toString() {
    return "Phone{" + "id=" + id + ", number=" + number + ", description=" + description + '}';
  }


}
