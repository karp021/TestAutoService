package haulmont.karp.backend.repos;

import haulmont.karp.backend.models.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    List<Mechanic> findByLastNameStartsWithIgnoreCase(String lastName);

    Mechanic findByLastName(String lastName);
}
