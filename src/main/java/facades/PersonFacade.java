package facades;

import dtos.AddressDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import utils.EMF_Creator;
import utils.Utility;

public class PersonFacade {

  private static PersonFacade instance;
  private static EntityManagerFactory emf;

  //Private Constructor to ensure Singleton
  private PersonFacade() {
  }

  public static PersonFacade getFacade(EntityManagerFactory _emf) {
    if (instance == null) {
      emf = _emf;
      instance = new PersonFacade();
    }
    return instance;
  }

  private EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  private synchronized boolean checkIfNumberExists(Phone phone) {
    EntityManager em = emf.createEntityManager();
    try {
      Query query = em.createQuery("SELECT p FROM Phone p WHERE p.number = :number", Phone.class);
      query.setParameter("number", phone.getNumber());
      phone = (Phone) query.getSingleResult();
      return phone != null;
    } catch (NoResultException ex) {
      return false;
    } catch (RuntimeException ex) {
      throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
      em.close();
    }
  }

  public synchronized PersonDTO createPerson(PersonDTO personDTO) {
    if (Utility.ValidatePersonDto(personDTO) && !isEmailTaken(personDTO)) {
      Person person = new Person(personDTO);
      EntityManager em = emf.createEntityManager();
      try {
        em.getTransaction().begin();
        if(person.getAddress() != null){
          if(person.getAddress().getCityInfo() != null){
            CityInfo ci = person.getAddress().getCityInfo();
            em.persist(ci);
            person.getAddress().setCityInfo(ci);
            em.merge(ci);
          }
          em.persist(person.getAddress());
        }
        if(person.getPhones() != null){
          for(Phone p: person.getPhones()){
            if (!checkIfNumberExists(p)) {
              System.out.println("number does not exist: " + p);
              em.persist(p);
              p.setPerson(person);
              em.merge(p);
            } else {
              System.out.println("Number exists: " + p);
            }
          }
        }
        em.persist(person);
        System.out.println("persitet: " + person);
        em.getTransaction().commit();

      } finally {
        em.close();
      }
      return new PersonDTO(person);
    } else {
      throw new WebApplicationException("Please check your data", 400);
    }
  }

  public synchronized PersonDTO editPerson(PersonDTO personDTO, long id) {
    if (personDTO.getEmail() == null) {
      throw new WebApplicationException("Email is missing", 400);
    } else if (personDTO.getFirstName() == null || personDTO.getLastName() == null) {
      throw new WebApplicationException("First or last name is missing", 400);
    }
    Person personEditInfo = new Person(personDTO.getEmail(), personDTO.getFirstName(), personDTO.getLastName());
    EntityManager em = emf.createEntityManager();
    Person personOldInfo = em.find(Person.class, id);
    if (personOldInfo == null) {
      throw new WebApplicationException(String.format("No person with provided id: (%d) found", id), 404);
    }
    if (isEmailTaken(personDTO) && personEditInfo.getEmail() != personOldInfo.getEmail()) {
      throw new WebApplicationException("New email is already in use", 400);
    } else {
      personOldInfo.setEmail(personEditInfo.getEmail());
      personOldInfo.setFirstName(personEditInfo.getFirstName());
      personOldInfo.setLastName(personEditInfo.getLastName());
      em.getTransaction().begin();
      em.merge(personOldInfo);
      em.getTransaction().commit();
      return new PersonDTO(personOldInfo);
    }
  }

  public synchronized PersonDTO addAddressToPerson(AddressDTO addressDTO, long id) {
    if (addressDTO.getStreet() == null) {
      throw new WebApplicationException("Street is missing", 400);
    } else if (addressDTO.getCityInfo().getZipCode() == null || addressDTO.getCityInfo().getCity() == null) {
      throw new WebApplicationException("Zipcode or city is missing", 400);
    }
    addressDTO = new AddressFacade().createAddress(addressDTO);
    EntityManager em = emf.createEntityManager();
    Address address = em.find(Address.class, addressDTO.getId());
    if (address == null) {
      throw new WebApplicationException(String.format("No address with provided id: (%d) found", addressDTO.getId()), 404);
    }
    Person person = em.find(Person.class, id);
    if (person == null) {
      throw new WebApplicationException(String.format("No person with provided id: (%d) found", id), 404);
    }
    try {
      person.setAddress(address);
      //address.addPerson(person);
      em.getTransaction().begin();
      //em.merge(address);
      em.merge(person);
      em.getTransaction().commit();
      return new PersonDTO(person);
    } catch (RuntimeException ex) {
      throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
      em.close();
    }
  }



  public synchronized PersonDTO addHobbyToPerson(HobbyDTO hobbyDTO, long id) {
    if (id <= 0) {
      throw new WebApplicationException("The provided is at not valid", 400);
    } else if (hobbyDTO.getName() == null) {
      throw new WebApplicationException("The hobby name is at not valid", 400);
    }
    hobbyDTO = new HobbyFacade().createHobby(hobbyDTO);
    EntityManager em = emf.createEntityManager();
    Hobby hobby = em.find(Hobby.class, hobbyDTO.getId());
    if (hobby == null) {
      throw new WebApplicationException(String.format("No hobby with provided id: (%d) found", hobbyDTO.getId()), 404);
    }
    Person person = em.find(Person.class, id);
    if (person == null) {
      throw new WebApplicationException(String.format("No person with provided id: (%d) found", id), 404);
    }
    person.addHobby(hobby);
    try {
      em.getTransaction().begin();
      em.merge(person);
      em.getTransaction().commit();
      return new PersonDTO(person);
    } catch (RuntimeException ex) {
      throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
      em.close();
    }
  }



  public List<Phone> getPhoneByPersonId(long id) {
    EntityManager em = emf.createEntityManager();
    try {
      TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p WHERE p.person.id = :id", Phone.class);
      query.setParameter("id", id);
      return query.getResultList();
    } catch (NoResultException ex) {
      return new ArrayList<>();
    }
  }

  public List<Hobby> getHobbyByPersonId(long id) {
    EntityManager em = emf.createEntityManager();
    try{
      TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h JOIN h.persons p WHERE p.id = :id", Hobby.class);
      query.setParameter("id", id);
      return query.getResultList();
    }catch (NoResultException ex) {
      return new ArrayList<>();
    }
  }

  public Address getAddressByPersonId(long id) {
    EntityManager em = emf.createEntityManager();
    try {
      Query query = em.createQuery("SELECT a FROM Address a JOIN a.persons p WHERE p.id = :id", Address.class);
      query.setParameter("id", id);
      return (Address) query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  public PersonDTO getPersonById(long id) {
    EntityManager em = emf.createEntityManager();
    Person person = em.find(Person.class, id);

    if (person != null) {
      List<Phone> phones = getPhoneByPersonId(id);
      List<Hobby> hobbies = getHobbyByPersonId(id);
      Address address = getAddressByPersonId(id);

      person.setPhones(phones);
      person.setHobbies(hobbies);
      person.setAddress(address);
      return new PersonDTO(person);
    } else {
      throw new WebApplicationException(String.format("No person with provided id: (%d) found", id), 400);
    }
  }


  public List<PersonDTO> getPersonListByZip(CityInfo cityInfo) {
    if (cityInfo.getZipCode() == null) {
      throw new WebApplicationException("Zipcode is missing", 400);
    }
    EntityManager em = emf.createEntityManager();
    TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address a JOIN a.cityInfo c WHERE c.zipCode = :zip ", Person.class);
    query.setParameter("zip", cityInfo.getZipCode());
    List<Person> persons = query.getResultList();
    List<PersonDTO> dtos = new ArrayList<>();
    for(Person p: persons){
      dtos.add(new PersonDTO(p));
    }
    return dtos;
  }

  public List<PersonDTO> getPersonListByHobby(Hobby hobby) {
    if (hobby.getName() == null) {
      throw new WebApplicationException("Hobby name is missing", 400);
    }
    EntityManager em = emf.createEntityManager();
    TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :name ", Person.class);
    query.setParameter("name", hobby.getName());
    List<Person> persons = query.getResultList();
    List<PersonDTO> dtos = new ArrayList<>();
    for(Person p: persons){
      dtos.add(new PersonDTO(p));
    }
    return dtos;
  }

  public long getPersonCount() {
    EntityManager em = emf.createEntityManager();
    try {
      return (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
    } finally {
      em.close();
    }
  }

  public boolean deletePersonById(long id) {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      em.createNamedQuery("Person.deletePersonById").setParameter("id", id).executeUpdate();
      em.getTransaction().commit();
      return true;
    } finally {
      em.close();
    }
  }

  public long getPersonCountByHobby(Hobby hobby) {
    if (hobby.getName() == null) {
      throw new WebApplicationException("Hobby name is missing", 400);
    }
    EntityManager em = emf.createEntityManager();
    Query query = em.createQuery("SELECT COUNT(p) FROM Person p JOIN p.hobbies h WHERE h.name = :name ");
    query.setParameter("name", hobby.getName());
    long count = (long) query.getSingleResult();
    return count;
  }

  public List<PersonDTO> getAllPersonsFullInfo() {
    EntityManager em = emf.createEntityManager();
    TypedQuery<Person> query = em.createQuery("SELECT person FROM Person person", Person.class);
    List<Person> persons = query.getResultList();
    List<PersonDTO> dtos = new ArrayList<>();
    for(Person p: persons){
      dtos.add(new PersonDTO(p));
    }
    return dtos;
  }

  private boolean isEmailTaken(PersonDTO personDTO) {
    EntityManager em = emf.createEntityManager();
    try {
      Query query = em.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class);
      query.setParameter("email", personDTO.getEmail());
      Person person = (Person) query.getSingleResult();
      if (person != null) {
        return true;
      } else {
        return false;
      }
    } catch (NoResultException ex) {
      return false;
    } catch (RuntimeException ex) {
      throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
      em.close();
    }
  }

  public static void main(String[] args) {
    emf = EMF_Creator.createEntityManagerFactory();
    PersonFacade fe = getFacade(emf);
  }

}
