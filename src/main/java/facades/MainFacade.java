package facades;

import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import utils.Utility;

public class MainFacade {

  protected static MainFacade instance;
  protected static EntityManagerFactory emf;

  //Private Constructor to ensure Singleton
  protected MainFacade() {
  }

  public static MainFacade getFacade(EntityManagerFactory _emf) {
    if (instance == null) {
      emf = _emf;
      instance = new MainFacade();
    }
    return instance;
  }

  protected EntityManager getEntityManager() {
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

  private synchronized Hobby createHobby(HobbyDTO hobby) {
    EntityManager em = emf.createEntityManager();
    try {
      Query query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name", Hobby.class);
      query.setParameter("name", hobby.getName());
      return (Hobby) query.getSingleResult();
    } catch (NoResultException ex) {
      Hobby h = new Hobby(hobby);
      em.getTransaction().begin();
      em.persist(h);
      em.getTransaction().commit();
      return h;
    } finally {
      em.close();
    }
  }

  public synchronized PersonDTO createPerson(PersonDTO personDTO) {
    if (Utility.ValidatePersonDto(personDTO) && !isEmailTaken(personDTO)) {
      Person person = null;
      List<HobbyDTO> hobbies = personDTO.getHobbies();
      List<HobbyDTO> h2 = new ArrayList<>();
      personDTO.setHobbies(h2);
      EntityManager em = emf.createEntityManager();
      try {
        person = new Person(personDTO);

        em.getTransaction().begin();
        if(person.getAddress() != null && person.getAddress().getCityInfo() != null){
            Address a = person.getAddress();
            CityInfo ci = a.getCityInfo();
            em.persist(ci);
            a.setCityInfo(ci);
            em.persist(a);
        }
        if (person.getPhones() != null) {
          for (Phone p : person.getPhones()) {
            if (!checkIfNumberExists(p)) {
              em.persist(p);
              p.setPerson(person);
              em.merge(p);
            }
          }
        }

        em.persist(person);

        if(person.getHobbies() != null){
          for(HobbyDTO h: hobbies){
            Hobby ho = createHobby(h);
            em.find(Hobby.class, ho.getId());
            person.addHobby(ho);
            em.merge(person);
          }
        }

        em.merge(person);

        em.getTransaction().commit();

      } finally {
        em.close();
      }
      return new PersonDTO(person);
    } else {
      throw new WebApplicationException("Please check your data", 400);
    }
  }

  public synchronized PersonDTO updatePerson(Long id, PersonDTO dto){
    EntityManager em = emf.createEntityManager();

    boolean updatedData = false;

    Person person = em.find(Person.class, id);

    if (person == null) {
      throw new WebApplicationException(
          String.format("No person with provided id: (%d) found", dto.getId()), 400
      );
    }


    if(dto.getEmail() != null){
      person.setEmail(dto.getEmail());
      updatedData = true;
    }
    if(dto.getFirstName() != null){
      person.setFirstName(dto.getFirstName());
      updatedData = true;
    }
    if(dto.getLastName() != null){
      person.setLastName(dto.getLastName());
      updatedData = true;
    }
    if(dto.getPhones() != null){
      person.setPhones(person.getNumberList(dto.getPhones()));
      updatedData = true;
    }


    if(dto.getHobbies() != null){

      TypedQuery<Hobby> existingHobbies = em.createQuery("SELECT h FROM Hobby h JOIN h.persons p WHERE p.id = :id ", Hobby.class);
      existingHobbies.setParameter("id", person.getId());
      List<Hobby> hobbies = existingHobbies.getResultList();

      for(Hobby existHobby : hobbies){
        existHobby = em.find(Hobby.class, existHobby.getId());
        person.removeHobby(existHobby);
      }

      for(HobbyDTO h: dto.getHobbies()){
        Hobby ho = createHobby(h);
        ho = em.find(Hobby.class, ho.getId());
        person.addHobby(ho);
      }
      updatedData = true;
    }

    //Only run if any of the given data has been updated
    if (updatedData) {
      em.getTransaction().begin();
      em.merge(person);
      em.getTransaction().commit();
    }
    return new PersonDTO(person);

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

  public List<PersonDTO> getPersonListByHobby(String hobbyname) {
    //hobbyname = Utility.encodeUrl(hobbyname);
    EntityManager em = emf.createEntityManager();
    TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies h WHERE h.name = :name ", Person.class);
    query.setParameter("name", hobbyname);
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
      em.createQuery("DELETE FROM Phone p WHERE p.person.id = :id").setParameter("id", id).executeUpdate();
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
    return (long) query.getSingleResult();
  }

  public List<CityInfoDTO> getAllZips() {
    EntityManager em = emf.createEntityManager();
    TypedQuery<CityInfo> query = em.createQuery("SELECT c FROM CityInfo c", CityInfo.class);
    return Utility.convertList(CityInfo.class, query.getResultList());
  }

  public List<PersonDTO> getAllPersons() {
    EntityManager em = emf.createEntityManager();
    TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.phones pp JOIN p.hobbies ph JOIN p.address pa", Person.class);
    return Utility.convertList(PersonDTO.class, query.getResultList());
  }
}
