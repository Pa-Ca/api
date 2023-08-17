package com.paca.paca.branch.service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableMapper tableMapper;

    private final TableRepository tableRepository;

    private final BranchRepository branchRepository;

    public TableDTO save(TableDTO tableDTO) throws NoContentException, ConflictException {
        long branchId = tableDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }
        if (tableRepository.existsByBranchIdAndName(branchId, tableDTO.getName())) {
            throw new ConflictException(
                    "Table with name " + tableDTO.getName() + " already exists",
                    55);
        }

        Table table = tableMapper.toEntity(tableDTO, branch.get());
        table = tableRepository.save(table);
        return tableMapper.toDTO(table);
    }

    public TableDTO update(long id, TableDTO tableDTO) throws NoContentException, ConflictException {
        Optional<Table> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + id + " does not exists",
                    49);
        }

        long branchId = tableDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }
        if (tableDTO.getName() != null &&
                tableRepository.existsByBranchIdAndName(branchId, tableDTO.getName())) {
            throw new ConflictException(
                    "Table with name " + tableDTO.getName() + " already exists",
                    55);
        }

        Table tableToUpdate = table.get();
        tableToUpdate = tableMapper.toEntity(tableDTO, branch.get());
        tableToUpdate = tableRepository.save(tableToUpdate);

        return tableMapper.toDTO(tableToUpdate);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Table> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + id + " does not exists",
                    49);
        }
        tableRepository.deleteById(table.get().getId());
    }
}
