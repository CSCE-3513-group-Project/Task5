/*
Programmer: Cade Courtney
Sprint2 Task5 - Sign In - Server-side functionality 
 */
package edu.uark.registerapp.commands.employees;

import edu.uark.registerapp.commands.employees.*;
import org.apache.commons.lang3.StringUtils;
import edu.uark.registerapp.controllers.SignInRouteController;
import org.springframework.beans.factory.annotation.Autowired;
import edu.uark.registerapp.commands.ResultCommandInterface;
import edu.uark.registerapp.commands.exceptions.ConflictException;
import edu.uark.registerapp.commands.exceptions.UnprocessableEntityException;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import org.springframework.transaction.annotation.Transactional;
import edu.uark.registerapp.models.api.EmployeeSignIn;
import edu.uark.registerapp.commands.exceptions.NotFoundException;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.entities.EmployeeEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSignInCommand implements ResultCommandInterface<Employee> {


    @Override
    public Employee execute() {
        this.validateProperties(); // validate employeeID and Password
        System.out.println("Employee ID:" + employeeID);
        System.out.println("Employee Password:" + employeePassword);
        System.out.println("Sign in  object: "+ this.employeeCreds.getEmployeeId());
        Optional<EmployeeEntity> employeeEntity = this.employeeRepository.findByEmployeeId(Integer.parseInt(this.employeeCreds.getEmployeeId())); // create Employee Entity
        if (employeeEntity.isPresent()) {
            apiEmployee = employeeEntity.get(); // set Entity
            if (employeeEntity.get().getPassword().equals(employeePassword)) { // check employee password
                SignInRouteController.setExistFlag(1);
                final Optional<ActiveUserEntity> activeUserEntity = this.activeUserRepository.findByEmployeeId(employeeEntity.get().getEmployeeId()); // pull active user
                if (activeUserEntity.isPresent()) { // if active user present, update session key
                    activeUserEntity.get().setSessionKey(currentSessionKey);
                    activeUserRepository.save(activeUserEntity.get());
                    return new Employee(apiEmployee);
                } else { // else add new active user
                    final ActiveUserEntity createdAUEntity = this.createdActiveUserEntity(); // creates new active user
                    return new Employee(apiEmployee);
                }
            } else { // password incorrect
                throw new NotFoundException("Incorrect Password");
            }
        } else { // employee id incorrect
            throw new NotFoundException("Employee");
        }
    }

    // Helper methods
    private void validateProperties() {
        try {
            employeeID = Integer.parseInt(employeeCreds.getEmployeeId());
            employeePassword = employeeCreds.getEmployeePassword();
        } catch (Exception e) {
            throw new UnprocessableEntityException("Incorrect Employee Credentials");
        }
        if (StringUtils.isBlank(this.employeePassword)) {
            throw new UnprocessableEntityException("Employee Password");
        }
    }

    @Transactional
    private ActiveUserEntity createdActiveUserEntity() { // places employee into activeuser table
        final Optional<ActiveUserEntity> queriedActiveUserEntity = this.activeUserRepository.findByEmployeeId(this.apiEmployee.getEmployeeId());
        if (queriedActiveUserEntity.isPresent()) {
            throw new ConflictException("Active User Exists");
        }
        //// No ENTITY object was returned from the database, thus the API object's
        // employeeID must be unique.
        // Write, via an INSERT, the new record to the database.
        return this.activeUserRepository.save(new ActiveUserEntity(this.apiEmployee, this.currentSessionKey));
    }

    // Properties
    private EmployeeSignIn employeeCreds;
    private int employeeID;
    private String employeePassword;
    private String currentSessionKey;
    // Entity
    private EmployeeEntity apiEmployee;

    // Employee Credentials = EmployeeSignIn obj; Contains EmployeeID and Password
    public EmployeeSignIn getEmployeeCredentials() {
        return this.employeeCreds;
    }

    public EmployeeSignInCommand setEmployeeCredentials(final EmployeeSignIn employeeCreds) {
        this.employeeCreds = employeeCreds;
        return this;
    }

    public String getSessionKey() {
        return this.currentSessionKey;
    }

    public EmployeeSignInCommand setSessionKey(final String sessionKey) {
        this.currentSessionKey = sessionKey;
        return this;
    }

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ActiveUserRepository activeUserRepository;
}
