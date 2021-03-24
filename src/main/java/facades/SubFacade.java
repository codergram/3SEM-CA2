package facades;

import dtos.HobbyDTO;
import entities.Hobby;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.WebApplicationException;

public class SubFacade extends MainFacade{

  private synchronized Hobby checkIfHobbyExistsElseCreateIt(Hobby hobby) {
    EntityManager em = emf.createEntityManager();
    try {
      Query query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name AND h.description = :des", Hobby.class);
      query.setParameter("name", hobby.getName());
      query.setParameter("des", hobby.getDescription());
      hobby = (Hobby) query.getSingleResult();
      return hobby;
    } catch (NoResultException ex) {
      em.getTransaction().begin();
      em.persist(hobby);
      em.getTransaction().commit();
      return hobby;
    } catch (RuntimeException ex) {
      throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
    } finally {
      em.close();
    }
  }

  public synchronized HobbyDTO createHobby(HobbyDTO hdto) throws WebApplicationException {
    if (hdto.getName() == null) {
      throw new WebApplicationException("Hobby name is missing", 400);
    }
    Hobby h = new Hobby(hdto.getName(), hdto.getDescription());
    h = checkIfHobbyExistsElseCreateIt(h);
    return new HobbyDTO(h);
  }

}
