package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import com.oocl.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAll(Gender gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public List<Employee> findAll(Integer page, Integer pageSize) {
        final Page<Employee> pageResult = employeeRepository.findAll(PageRequest.of(page - 1, pageSize));
        return pageResult.getContent();
    }

    public Employee findById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow();
    }

    public Employee create(Employee employee) {
        if(employee.getAge() < 18 || employee.getAge() > 65)
            throw new EmployeeAgeNotValidException();
        if(employee.getAge() >= 30 && employee.getSalary() < 20000.0)
            throw new EmployeeAgeSalaryNotMatchedException();

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public Employee update(Integer employeeId , Employee employee) {
        Employee employeeExisted = findById(employeeId);
        if(!employeeExisted.getActive())
            throw new EmployeeInactiveException();

        updateEmployeeAttributes(employeeExisted, employee);

        return employeeRepository.save(employeeExisted);
    }

    public void delete(Integer employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    private Employee updateEmployeeAttributes(Employee employeeStored, Employee newEmployee) {
        if (newEmployee.getName() != null) {
            employeeStored.setName(newEmployee.getName());
        }
        if (newEmployee.getAge() != null) {
            employeeStored.setAge(newEmployee.getAge());
        }
        if (newEmployee.getGender() != null) {
            employeeStored.setGender(newEmployee.getGender());
        }
        if (newEmployee.getSalary() != null) {
            employeeStored.setSalary(newEmployee.getSalary());
        }
        return employeeStored;
    }
}
