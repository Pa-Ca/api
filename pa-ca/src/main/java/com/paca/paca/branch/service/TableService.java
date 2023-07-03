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
        // Check if the branch exists
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
        // Create a table
        Table table = tableMapper.toEntity(tableDTO, branch.get());
        // Save the table
        table = tableRepository.save(table);
        // Return the table
        return tableMapper.toDTO(table);
    }

    public TableDTO update(long id, TableDTO tableDTO) throws NoContentException, ConflictException {

        Optional<Table> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + id + " does not exists",
                    49);
        }
        // Check if the branch exists
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
        // Update the table
        tableToUpdate = tableMapper.toEntity(tableDTO, branch.get());

        // Save the table
        tableToUpdate = tableRepository.save(tableToUpdate);
        // Return the table
        return tableMapper.toDTO(tableToUpdate);
    }

    public void delete(Long id) throws NoContentException {
        // Check if the table exists
        Optional<Table> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + id + " does not exists",
                    49);
        }
        // As tables are not deleted, we just set the deleted attribute to true
        Table tableToDelete = table.get();
        tableToDelete.setDeleted(true);
        // Save the table
        tableToDelete = tableRepository.save(tableToDelete);

    }
}
