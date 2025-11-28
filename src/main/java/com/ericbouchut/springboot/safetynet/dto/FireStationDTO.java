package com.ericbouchut.springboot.safetynet.dto;

import java.util.List;


/**
 * A response DTO (Data Transfer Object) describing
 * the coverage of a fire station and contains:
 * <ul>
 *     <li>the list of {@link PersonDTO persons} covered by a specific fire station.</li>
 *     <li>the number of adults covered</li>
 *     <li>the number of children (age <= 18) covered</li>
 * </ul>
 * Each person contains:
 * <ul>
 *      <li>first name,</li>
 *      <li> last name,</li>
 *      <li> address,</li>
 *      <li> phone number.</mli>
 * </ul>
 *
 * @param persons       a list of {@link PersonDTO}
 * @param adultsCount    the number of adults covered by the fire station
 * @param childrenCount the number of children covered by the fire station
 */
public record FireStationDTO (
    List<FireStationDTO.PersonDTO> persons,
    Long adultsCount,
    Long childrenCount
) {

    /**
     * A person covered by a specific fire station:
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param phone
     */
    public record PersonDTO(
            String firstName,
            String lastName,
            String address,
            String phone
    ) {
    }
}
