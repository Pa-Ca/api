package com.paca.paca.branch;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import com.paca.paca.ServiceTest;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.service.TableService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;

public class TableServiceTest extends ServiceTest {

    @InjectMocks
    private TableService tableService;

    @Test
    void shouldSave() {
        TableDTO tableDTO = utils.createTableDTO(null);
        Table table = utils.createTable(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(tableRepository.save(any())).thenReturn(table);
        when(tableMapper.toDTO(any())).thenReturn(tableDTO);

        TableDTO response = tableService.save(tableDTO);

        assertThat(response).isEqualTo(tableDTO);
    }

    @Test
    void shouldGetNotFoundExceptionDueToBranchNoExistingInSave() {
        TableDTO tableDTO = utils.createTableDTO(null);
        long branchId = tableDTO.getBranchId();

        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            tableService.save(tableDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branchId + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetConflictExceptionDueToTableAlreadyExistingInSave() {
        TableDTO tableDTO = utils.createTableDTO(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));

        try {
            tableService.save(tableDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Table with id " + tableDTO.getId() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 55);
        }
    }

    @Test
    void shouldUpdate() {
        TableDTO tableDTO = utils.createTableDTO(null);
        Table table = utils.createTable(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(tableRepository.save(any())).thenReturn(table);
        when(tableMapper.toDTO(any())).thenReturn(tableDTO);

        TableDTO response = tableService.update(1L, tableDTO);

        assertThat(response).isEqualTo(tableDTO);
    }

    @Test
    void shouldGetNotFoundExceptionDueToTableNotExisistingInUpdate() {
        TableDTO tableDTO = utils.createTableDTO(null);
        long tableId = tableDTO.getId();

        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            tableService.update(tableId, tableDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Table with id " + tableId + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 49);
        }
    }

    @Test
    void shouldGetNotFoundExceptionDueToBranchNotExisistingInUpdate() {
        TableDTO tableDTO = utils.createTableDTO(null);
        long tableId = tableDTO.getId();

        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            tableService.update(tableId, tableDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Table with id " + tableId + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 49);
        }
    }

    @Test
    void shouldGetConflictExceptionDueToTableAlreadyExistingInUpdate() {
        TableDTO tableDTO = utils.createTableDTO(null);
        Table table = utils.createTable(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(tableRepository.existsByBranchIdAndName(anyLong(), any())).thenReturn(true);

        try {
            tableService.update(1L, tableDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Table with name " + tableDTO.getName() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 55);
        }
    }

    @Test
    void shouldDelete() {
        Table table = utils.createTable(null);

        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(table));

        tableService.delete(table.getId());

        verify(tableRepository, times(1)).deleteById(table.getId());
    }

    @Test
    void shouldGetNotFoundExceptionDueToTableNotExisistingInDelete(){
        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty()); 

        try {
            tableService.delete(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NotFoundException);
            Assert.assertEquals(e.getMessage(), "Table with id " + 1L + " does not exists");
            Assert.assertEquals(((NotFoundException) e).getCode(), (Integer) 49);
        }
    }

}
