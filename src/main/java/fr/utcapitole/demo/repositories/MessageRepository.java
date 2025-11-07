package fr.utcapitole.demo.repositories;

import fr.utcapitole.demo.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("select m.createdAt from Message m where m.author = :author")
    List<OffsetDateTime> findCreatedAtsByAuthor(@Param("author") String author);
}