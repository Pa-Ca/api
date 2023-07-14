package com.paca.paca.branch;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.service.TableService;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private TableMapper tableMapper;


    @InjectMocks
    private TableService tableService;
    

    private TestUtils utils = TestUtils.builder().build();


    @Test
    void shouldSave() {
        TableDTO  tableDTO = utils.createTableDTO(null);
        Table table = utils.createTable(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(tableRepository.save(any())).thenReturn(table);
        when(tableMapper.toDTO(any())).thenReturn(tableDTO);
        
        TableDTO  response = tableService.save(tableDTO);

        assertThat(response).isNotNull();
    }


    @Test
    void shouldUpdate(){
        TableDTO  tableDTO = utils.createTableDTO(null);
        Table table = utils.createTable(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(tableRepository.save(any())).thenReturn(table);
        when(tableMapper.toDTO(any())).thenReturn(tableDTO);
        
        TableDTO  response = tableService.update(1L ,tableDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldDelete(){
        Table table = utils.createTable(null);

        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));

        tableService.delete(1L);
        // Yes the method to test is the save method not the delete method.
        // This is beacause 
        verify(tableRepository, times(1)).save(table);
    }

}
