/*
Programmer: Cade Courtney
Sprint2 Task5 - Sign In - Server-side functionality 
 */
package edu.uark.registerapp.commands.employees;

import edu.uark.registerapp.commands.employees.*;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.EmployeeRepository;

;

/**
 *
 * @author cacou
 */
@Service
public class ActiveEmployeeExistsQuery implements ResultCommandInterface<Employee> {

    @Override
    public Employee execute() {
        this.validateProperties();

        final Optional<EmployeeEntity> employeeEntity = this.employeeRepository.findByEmployeeId(eID);
        if (employeeEntity.isPresent()) {
            if (this.employeeRepository.existsByIsActive(employeeEntity.get().getIsActive())) {
                return new Employee(employeeEntity.get());
            }
            else {
                throw new NotFoundException("Employee not active");
            }
        }
        throw new NotFoundException("Employee");

    }

    // Helper methods
    private void validateProperties() {
        // do not know if needed
    }

    // Properties
    private int eID;

    public int getEmployeeID() {
        return this.eID;
    }

    public ActiveEmployeeExistsQuery setActivity(final int eID) {
        this.eID = eID;
        return this;
    }

    @Autowired
    private EmployeeRepository employeeRepository;
}
