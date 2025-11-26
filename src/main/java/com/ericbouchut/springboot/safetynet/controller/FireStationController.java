package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.model.FireStation;
import com.ericbouchut.springboot.safetynet.service.FireStationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class FireStationController {
    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  CRUD Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @GetMapping("/firestation")
    public Set<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return all the fire stations that share the same number
     */
    @GetMapping("/firestation/{id}")
    public List<FireStation> getFireStationsByNumber(
            @PathVariable("id")
            @NotNull
            Integer fireStationNumber
    ) {
        return fireStationService.getFireStationsByNumber(fireStationNumber);
    }
}
