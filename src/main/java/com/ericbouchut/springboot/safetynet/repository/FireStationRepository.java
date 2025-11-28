package com.ericbouchut.springboot.safetynet.repository;

import com.ericbouchut.springboot.safetynet.model.Data;
import com.ericbouchut.springboot.safetynet.model.FireStation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FireStationRepository {
    private final Data data;

    public FireStationRepository(Data data) {
        this.data = data;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return all the fire stations (no duplicates)
     */
    public Set<FireStation> getAllFireStations() {
        return data.getFireStations();
    }

    /**
     * Return the fire stations that have the same station {@link FireStation#number}.
     * @param fireStationNumber the name of a fire station
     * @return the fire stations with a given number
     */
    public List<FireStation> getFireStationsByNumber(Integer fireStationNumber) {
        return data.getFireStations()
                .stream()
                .filter(s -> s.getNumber().equals(fireStationNumber))
                .toList();
    }

    /**
     * @param fireStationNumber a (station) number shared by one or more fire stations
     * @return the addresses of fire stations that share the same number (no duplicate)
     */
    public Set<String> getFireStationAddressesByNumber(Integer fireStationNumber) {
        return data.getFireStations()
                .stream()
                .filter(fireStation -> fireStation.getNumber().equals(fireStationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());
    }
}
