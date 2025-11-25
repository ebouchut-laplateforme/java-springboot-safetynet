package com.ericbouchut.springboot.safetynet.service;

import com.ericbouchut.springboot.safetynet.model.FireStation;
import com.ericbouchut.springboot.safetynet.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    /**
     * @return all fire stations (no duplicates)
     */
    public Set<FireStation> getAllFireStations() {
        return fireStationRepository.getAllFireStations();
    }

    public List<FireStation> getFireStationsByNumber(Integer fireStationNumber) {
        return fireStationRepository.getFireStationsByNumber(fireStationNumber);
    }
}
