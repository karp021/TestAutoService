package haulmont.karp.backend.service;


import haulmont.karp.backend.models.Mechanic;

import java.util.List;

public interface MechanicService {

    Mechanic saveMechanic(Mechanic mechanic);

    Mechanic getMechanicOne(Long id);

    Mechanic getByLastName(String lastName);

    List<Mechanic> getAllMechanic();

    void deleteMechanic(Long id);

    void deleteMechanic(Mechanic mechanic);

    Mechanic updateMechanic(Mechanic mechanic);

    boolean existMechanic(Long id);

    List<Mechanic> findByLastName(String lastName);

}
