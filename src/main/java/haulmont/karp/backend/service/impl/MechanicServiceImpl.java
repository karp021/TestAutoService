package haulmont.karp.backend.service.impl;

import haulmont.karp.backend.models.Mechanic;
import haulmont.karp.backend.repos.MechanicRepository;
import haulmont.karp.backend.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MechanicServiceImpl implements MechanicService {

    @Autowired
    private MechanicRepository mechanicRepository;

    @Override
    public Mechanic saveMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic getMechanicOne(Long id) {
        return mechanicRepository.findOne(id);
    }

    @Override
    public Mechanic getByLastName(String lastName) { return mechanicRepository.findByLastName(lastName); }

    @Override
    public List<Mechanic> getAllMechanic() {
        return mechanicRepository.findAll();
    }

    @Override
    public void deleteMechanic(Long id) {
            mechanicRepository.delete(id);
    }

    @Override
    public void deleteMechanic(Mechanic mechanic) throws DataIntegrityViolationException { mechanicRepository.delete(mechanic); }

    @Override
    public Mechanic updateMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }

    @Override
    public boolean existMechanic(Long id) {
        return mechanicRepository.exists(id);
    }

    @Override
    public List<Mechanic> findByLastName(String lastName) { return mechanicRepository.findByLastNameStartsWithIgnoreCase(lastName); }
}
