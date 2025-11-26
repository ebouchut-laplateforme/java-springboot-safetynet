package com.ericbouchut.springboot.safetynet.dto;

import com.ericbouchut.springboot.safetynet.model.Person;

import java.util.List;

/**
 * Child (age <= 18) :
 * <ul>
 *   <li>first name</li>
 *   <li>first name</li>
 *   <li>age</li>
 *   <li>list of other household members</li>
 * </ul>
 */
public record ChildAlertDTO(
    String firstName,
    String lastName,
    int age,

    List<Person> otherHouseholdMembers
) {}
