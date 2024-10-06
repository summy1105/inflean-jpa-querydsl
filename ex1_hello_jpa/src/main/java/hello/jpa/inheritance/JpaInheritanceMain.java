package hello.jpa.inheritance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;

public class JpaInheritanceMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        try {
//            test1(entityManager);
//            test2(entityManager);
            MappedSuperclassTest(entityManager);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close(); // 커넥션 정리 필요
        }

        entityManagerFactory.close();
    }

    private static void test1(EntityManager entityManager) {
        Movie movie = new Movie();
        movie.setDirector("director A");
        movie.setActor("actor B");
        movie.setName("바람과함께사라지다");
        movie.setPrice(10000);

        entityManager.persist(movie);

        entityManager.flush();
        entityManager.clear();

        Movie findMovie = entityManager.find(Movie.class, 1L);
        System.out.println("findMovie name=" + findMovie.getName());
    }

    private static void test2(EntityManager entityManager) {
        Movie movie = new Movie();
        movie.setDirector("director A");
        movie.setActor("actor B");
        movie.setName("바람과함께사라지다");
        movie.setPrice(10000);

        entityManager.persist(movie);

        entityManager.flush();
        entityManager.clear();

        Item findItem = entityManager.find(Item.class, 1L);
        System.out.println("findItem name=" + findItem.getName());
    }

    private static void MappedSuperclassTest(EntityManager entityManager) {
        Movie movie = new Movie();
        movie.setDirector("director A");
        movie.setActor("actor B");
        movie.setName("바람과함께사라지다");
        movie.setPrice(10000);
        movie.setCreatedBy("kim");
        movie.setCreatedDate(LocalDateTime.now());


        entityManager.persist(movie);
    }
}
