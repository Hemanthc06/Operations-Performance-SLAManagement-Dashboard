package com.ops.dashboard.service;

import com.ops.dashboard.dto.SLADTO;
import com.ops.dashboard.model.Employee;
import com.ops.dashboard.model.SLARecord;
import com.ops.dashboard.model.SLAStatus;
import com.ops.dashboard.repository.EmployeeRepository;
import com.ops.dashboard.repository.SLARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SLAService {

    @Autowired private SLARepository slaRepository;
    @Autowired private EmployeeRepository employeeRepository;

    public List<SLADTO> findAll() {
        List<SLADTO> list = new ArrayList<>();
        for (SLARecord r : slaRepository.findAll()) {
            list.add(toDTO(r));
        }
        return list;
    }

    public SLADTO create(SLADTO dto) {
        SLARecord r = SLARecord.builder()
                .title(dto.getTitle())
                .priority(dto.getPriority())
                .targetHours(dto.getTargetHours())
                .actualHours(dto.getActualHours())
                .status(dto.getStatus() == null ? SLAStatus.ON_TRACK : dto.getStatus())
                .build();

        if (dto.getAssignedToId() != null) {
            Employee e = employeeRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            r.setAssignedTo(e);
        }

        return toDTO(slaRepository.save(r));
    }

    public SLADTO updateStatus(Long id, SLAStatus status) {
        SLARecord r = slaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA not found: " + id));
        r.setStatus(status);
        return toDTO(slaRepository.save(r));
    }

    public void delete(Long id) {
        slaRepository.deleteById(id);
    }

    private SLADTO toDTO(SLARecord r) {
        SLADTO dto = new SLADTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setPriority(r.getPriority());
        dto.setTargetHours(r.getTargetHours());
        dto.setActualHours(r.getActualHours());
        dto.setStatus(r.getStatus());
        dto.setCreatedAt(r.getCreatedAt());
        if (r.getAssignedTo() != null) {
            dto.setAssignedToId(r.getAssignedTo().getId());
            dto.setAssignedToName(r.getAssignedTo().getFullName());
        }
        return dto;
    }
}