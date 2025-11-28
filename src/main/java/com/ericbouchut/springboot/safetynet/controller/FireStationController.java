package com.ericbouchut.springboot.safetynet.controller;

import com.ericbouchut.springboot.safetynet.dto.FireStationDTO;
import com.ericbouchut.springboot.safetynet.model.FireStation;
import com.ericbouchut.springboot.safetynet.service.FireStationService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     *
     * @return all fire stations (no duplicates)
     */
    @GetMapping("/firestation/all")
    public Set<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }


    @GetMapping("/firestation/{id}")
    public List<FireStation> getFireStationsByNumber(
            @PathVariable("id")
            @NotNull
            @Positive // > 0
            Integer fireStationNumber
    ) {
        return fireStationService.getFireStationsByNumber(fireStationNumber);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  Custom Finder Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     *  Return an JSON object with:
     *  <ul>
     *      <li>the list of people covered by the fire station with the passed-in number,</li>
     *      <li>the number of adults in the served area</li>
     *      <li>the number of children (age <= 18) in the served area</li>
     *  </ul>
     *
     *  For instance, if the station number is 1, the list should contain the residents
     *  covered by station number 1.
     *  <p>
     *  Each list item should include the following specific information:
     * <ul>
     *     <li>first name,</li>
     *     <li> last name,</li>
     *     <li> address,</li>
     *     <li> phone number.</li>
     * </ul>
     *
     * @return a {@link FireStationDTO} with the list of people covered by the fire station and the count of adults and children
     * @see FireStationDTO
     */
    @GetMapping("/firestation")
    public FireStationDTO getFireStationDTO(
            @RequestParam("stationNumber")
            @NotNull
            @Positive // > 0
            Integer fireStationNumber
    ) {
        return fireStationService.getFireStationDTOByNumber(fireStationNumber);
    }
}

