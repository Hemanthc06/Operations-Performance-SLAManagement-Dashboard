package com.ops.dashboard.service;

import com.ops.dashboard.dto.EmployeeDTO;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.Team;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private TeamRepository teamRepository;

    public List<EmployeeDTO> findAll() {
        List<EmployeeDTO> list = new ArrayList<>();
        for (Employee e : employeeRepository.findAll()) {
            list.add(toDTO(e));
        }
        return list;
    }

    public EmployeeDTO findById(Long id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        return toDTO(e);
    }

    public EmployeeDTO create(EmployeeDTO dto) {
        Employee e = Employee.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .designation(dto.getDesignation())
                .department(dto.getDepartment())
                .build();

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            e.setTeam(team);
        }

        return toDTO(employeeRepository.save(e));
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));

        e.setFullName(dto.getFullName());
        e.setEmail(dto.getEmail());
        e.setDesignation(dto.getDesignation());
        e.setDepartment(dto.getDepartment());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            e.setTeam(team);
        }

        return toDTO(employeeRepository.save(e));
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO toDTO(Employee e) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(e.getId());
        dto.setFullName(e.getFullName());
        dto.setEmail(e.getEmail());
        dto.setDesignation(e.getDesignation());
        dto.setDepartment(e.getDepartment());
        if (e.getTeam() != null) {
            dto.setTeamId(e.getTeam().getId());
            dto.setTeamName(e.getTeam().getName());
        }
        return dto;
    }
}