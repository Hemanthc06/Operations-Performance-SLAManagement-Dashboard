package com.ops.dashboard.service;

import com.ops.dashboard.dto.TeamDTO;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.Team;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired private TeamRepository teamRepository;
    @Autowired private EmployeeRepository employeeRepository;

    public List<TeamDTO> findAll() {
        List<TeamDTO> list = new ArrayList<>();
        for (Team t : teamRepository.findAll()) {
            list.add(toDTO(t));
        }
        return list;
    }

    public TeamDTO findById(Long id) {
        Team t = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found: " + id));
        return toDTO(t);
    }

    public TeamDTO create(TeamDTO dto) {
        if (teamRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Team name already exists");
        }
        Team t = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return toDTO(teamRepository.save(t));
    }

    public TeamDTO update(Long id, TeamDTO dto) {
        Team t = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found: " + id));
        t.setName(dto.getName());
        t.setDescription(dto.getDescription());
        return toDTO(teamRepository.save(t));
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    private TeamDTO toDTO(Team t) {
        TeamDTO dto = new TeamDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setDescription(t.getDescription());
        List<Employee> members = employeeRepository.findByTeamId(t.getId());
        dto.setMemberCount(members.size());
        return dto;
    }
}