package com.ops.dashboard.service;

import com.ops.dashboard.dto.EscalationDTO;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.Escalation;
import com.ops.dashboard.model.Status;
import com.ops.dashboard.model.Task;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.EscalationRepository;
import com.ops.dashboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EscalationService {

    @Autowired private EscalationRepository escalationRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private EmployeeRepository employeeRepository;

    public List<EscalationDTO> findAll() {
        List<EscalationDTO> list = new ArrayList<>();
        for (Escalation e : escalationRepository.findAll()) {
            list.add(toDTO(e));
        }
        return list;
    }

    public EscalationDTO create(EscalationDTO dto) {
        Escalation e = Escalation.builder()
                .reason(dto.getReason())
                .priority(dto.getPriority())
                .status(dto.getStatus() == null ? Status.TODO : dto.getStatus())
                .build();

        if (dto.getTaskId() != null) {
            Task t = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            e.setTask(t);
        }
        if (dto.getEscalatedById() != null) {
            Employee emp = employeeRepository.findById(dto.getEscalatedById())
                    .orElseThrow(() -> new RuntimeException("EscalatedBy not found"));
            e.setEscalatedBy(emp);
        }
        if (dto.getEscalatedToId() != null) {
            Employee emp = employeeRepository.findById(dto.getEscalatedToId())
                    .orElseThrow(() -> new RuntimeException("EscalatedTo not found"));
            e.setEscalatedTo(emp);
        }

        return toDTO(escalationRepository.save(e));
    }

    public EscalationDTO updateStatus(Long id, Status status) {
        Escalation e = escalationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escalation not found: " + id));
        e.setStatus(status);
        return toDTO(escalationRepository.save(e));
    }

    public void delete(Long id) {
        escalationRepository.deleteById(id);
    }

    private EscalationDTO toDTO(Escalation e) {
        EscalationDTO dto = new EscalationDTO();
        dto.setId(e.getId());
        dto.setReason(e.getReason());
        dto.setPriority(e.getPriority());
        dto.setStatus(e.getStatus());
        dto.setCreatedAt(e.getCreatedAt());
        if (e.getTask() != null) {
            dto.setTaskId(e.getTask().getId());
            dto.setTaskTitle(e.getTask().getTitle());
        }
        if (e.getEscalatedBy() != null) {
            dto.setEscalatedById(e.getEscalatedBy().getId());
            dto.setEscalatedByName(e.getEscalatedBy().getFullName());
        }
        if (e.getEscalatedTo() != null) {
            dto.setEscalatedToId(e.getEscalatedTo().getId());
            dto.setEscalatedToName(e.getEscalatedTo().getFullName());
        }
        return dto;
    }
}