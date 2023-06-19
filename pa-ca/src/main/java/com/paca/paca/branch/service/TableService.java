package com.paca.paca.branch.service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;

// Import Table Model, DTO, repository and mapper
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.exception.exceptions.NoContentException;
// Import the branch model and repository
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;

@Service
@RequiredArgsConstructor
public class TableService {

    // Initialize the repository, mapper and repository
    private final TableRepository tableRepository;
    private final TableMapper tableMapper;
    private final BranchRepository branchRepository;

    // Create a method that saves a table (from the DTO)
    public TableDTO save(TableDTO tableDTO) throws NoContentException{
        // Check if the branch exists
        long branchId = tableDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
                    throw new NoContentException(
                            "Branch with id " + branchId + " does not exists",
                            20);
                }
        // Create a table
        Table table = tableMapper.toEntity(tableDTO, branch.get());
        // Save the table
        table = tableRepository.save(table);
        // Return the table
        return tableMapper.toDTO(table);
    }

    // Create a method that updates a table (from the DTO)
    public TableDTO update(TableDTO tableDTO) throws NoContentException{
        // Check if the table exists
        long tableId = tableDTO.getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
                    throw new NoContentException(
                            "Table with id " + tableId + " does not exists",
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
        Table tableToUpdate = table.get();
        // Update the table
        tableToUpdate = tableMapper.toEntity(tableDTO, branch.get());

        // Save the table
        tableToUpdate = tableRepository.save(tableToUpdate);
        // Return the table
        return tableMapper.toDTO(tableToUpdate);
    }

    // Create a method that deletes a table (from the id)
    // As tables are not deleted, we just set the deleted attribute to true
    public void delete(Long id) throws NoContentException{
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
