package com.ericbouchut.springboot.safetynet.dto;

import com.ericbouchut.springboot.safetynet.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A response Data Transfer Object that contains
 * the list of all households served by the fire station.
 * <br/>
 * This list should group people by address.
 * It should also include the name, phone number and age of the residents,
 * and list their medical history (medications, dosage and allergies)
 * next to each name.
 * <p>
 * Example:
 * <code><pre>
 * [
 *    {
 *        "address": "1509 Culver St",
 *        "households": [
 *            {
 *                "name": "John Boyd",
 *                "phone": "841-874-6512",
 *                "age":   41,
 *                "medicalHistory": {
 *                    "medications": [
 *                        { "name": "aznol",        "dosage": "350mg" },
 *                        { name": "hydrapermazol", "dosage": "100mg" }
 *                    ]
 *                    "allergies": [
 *                        "nillacilan"
 *                    ]
 *                }
 *            },
 *            {
 *                "name":  "Jacob Boyd" ,
 *                "phone": "841-874-6513",
 *                "age":   36
 *                "medicalHistory": {
 *                    "medications": [
 *                        { "name": "pharmacol",  "dosage": "10mg" },
 *                        { "name": "terazine",   "dosage": "100mg" },
 *                        { "name": "noznazol",   "dosage": "250mg" }
 *                    ]
 *                    "allergies": [ ]
 *                }
 *            },
 *            ...
 *        ]
 *     },
 *     {
 *         "address": "29 15th St",
 *         "households": [
 *            {
 *                "name":  "Jonanathan Marrack",
 *                "phone": "841-874-6513",
 *                "age":   36
 *                "medicalHistory": {
 *                    "medications": [
 *                        { "name": "pharmacol",  "dosage": "10mg" },
 *                        { "name": "terazine",   "dosage": "100mg" },
 *                        { "name": "noznazol",   "dosage": "250mg" }
 *                    ]
 *                    "allergies": [ ]
 *                }
 *            }
 *         ]
 *     }
 * ]
 * </pre></code>
 *
 * @see HouseholdDTO
 * @see PersonDTO
 */
public record FloodDTO (
    String address,
    List<HouseholdDTO> households
) {

    /**
     * List of household members (persons living at the same address).
     *
     * @param householdMembers persons living at the same address
     *
     * @see PersonDTO
     * @see FloodDTO
     */
    public static record HouseholdDTO(
        List<PersonDTO> householdMembers
    ) {
    }

    /**
     * A Person (household member) with a:
     *
     * @param name
     * @param phone
     * @param age
     * @param medicalHistoryDTO the medications and allergies of this person
     *
     * @see HouseholdDTO
     * @see FloodDTO
     */
    public static record PersonDTO(
        String name,
        String phone,
        int    age,

        @JsonProperty("medicalHistory")
        MedicalHistoryDTO medicalHistoryDTO
    ) {
    }
}