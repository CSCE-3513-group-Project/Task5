package edu.uark.registerapp.commands.activeUsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.uark.registerapp.commands.VoidCommandInterface;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;

@Service
public class ActiveUserDeleteCommand implements VoidCommandInterface {

    @Transactional
    @Override
    public void execute() {
        final Optional<ActiveUserEntity> activeUserEntity = this.activeUserRepository.findBySessionKey(currentSessionKey);
        final Optional<EmployeeEntity> employeeEntity = this.employeeRepository.findByEmployeeId(activeUserEntity.get().getEmployeeId());
        if (!activeUserEntity.isPresent()) {
            try {
                // No record with the associated record ID exists in the database.
                throw new NotFoundException("Active User");
            } catch (NotFoundException ex) {
                // for some reason I must wrap NotFoundException in a try catch block...
            }
        }
        // Employee Name Validation
        if (employeeEntity.isPresent()) {
            apiEmployee = new Employee(employeeEntity.get());
            if (!validateProperties()) {
                throw new UnprocessableEntityException("Name");
            }
        }
        this.activeUserRepository.delete(activeUserEntity.get());
    }

    // Helper methods
    private boolean validateProperties() {
        if (StringUtils.isBlank(this.apiEmployee.getFirstName()) || StringUtils.isBlank(this.apiEmployee.getLastName())) {
            return false;
        }
        return true;

    }
    // Properties
    private String currentSessionKey;
    // Entity
    private Employee apiEmployee;

    public String getSessionKey() {
        return this.currentSessionKey;
    }

    public ActiveUserDeleteCommand setSessionKey(final String sessionKey) {
        this.currentSessionKey = sessionKey;
        return this;
    }

    @Autowired
    private ActiveUserRepository activeUserRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
}
