package com.springboot.insurance.service;

import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Addon;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.model.ProposalAddon;
import com.springboot.insurance.repository.AddonRepository;
import com.springboot.insurance.repository.ProposalAddonRepository;
import com.springboot.insurance.repository.ProposalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalAddonServiceTest {

    @InjectMocks
    private ProposalAddonService proposalAddonService;

    @Mock
    private ProposalAddonRepository proposalAddonRepository;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private AddonRepository addonRepository;

    private Proposal proposal1;
    private Addon addon1;
    private ProposalAddon proposalAddon1;

    @BeforeEach
    public void init() {

        proposal1 = new Proposal();
        proposal1.setId(1L);
        proposal1.setPremiumAmount(5000);

        addon1 = new Addon(
                1L,
                "Road Side Assistance",
                1000,
                "24/7 Support",
                true
        );

        proposalAddon1 = new ProposalAddon(
                1L,
                1000,
                true,
                proposal1,
                addon1
        );
    }

    @Test
    public void addTest() {

        when(proposalRepository.findById(1L))
                .thenReturn(Optional.of(proposal1));

        when(addonRepository.findById(1L))
                .thenReturn(Optional.of(addon1));

        proposalAddonService.add(1L,1L,"john@gmail.com");

        ArgumentCaptor<ProposalAddon> captor =
                ArgumentCaptor.forClass(ProposalAddon.class);

        verify(proposalAddonRepository,times(1))
                .save(captor.capture());

        Assertions.assertEquals(proposal1,captor.getValue().getProposal());
        Assertions.assertEquals(addon1,captor.getValue().getAddon());
        Assertions.assertTrue(captor.getValue().isActive());

        verify(proposalRepository,times(1)).save(proposal1);
    }

//    @Test
//    public void addInvalidProposalId(){
//
//        when(proposalRepository.findById(10L))
//                .thenReturn(Optional.empty());
//
//        Assertions.assertEquals(
//                "Proposal Id is invalid",
//                Assertions.assertThrows(
//                        ResourceNotFoundException.class,
//                        ()->proposalAddonService.add(10L,1L,"john@gmail.com")
//                ).getMessage()
//        );
//
//        verify(proposalAddonRepository,never()).save(any());
//    }

    @Test
    public void getByIdPresent(){

        when(proposalAddonRepository.findByProposal_IdAndAddon_IdAndProposal_PolicyHolder_User_Username(1L,1L,"john@gmail.com"))
                .thenReturn(Optional.of(proposalAddon1));

        ProposalAddonResponseDto dto = proposalAddonService.getById(1L,1L,"john@gmail.com");

        Assertions.assertEquals(1000,dto.addonPrice());

        verify(proposalAddonRepository,times(1))
                .findByProposal_IdAndAddon_IdAndProposal_PolicyHolder_User_Username(
                        1L,1L,"john@gmail.com");
    }

    @Test
    public void getByIdAbsent(){

        when(proposalAddonRepository.findByProposal_IdAndAddon_IdAndProposal_PolicyHolder_User_Username(1L,1L,"john@gmail.com"))
                .thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Proposal id Invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        ()->proposalAddonService.getById(1L,1L,"john@gmail.com")
                ).getMessage()
        );
    }

    @Test
    public void deleteTest(){

        when(proposalAddonRepository.findById(1L)).thenReturn(Optional.of(proposalAddon1));
        when(proposalAddonRepository.save(proposalAddon1)).thenReturn(proposalAddon1);

        proposalAddonService.delete(1L);

        verify(proposalAddonRepository,times(1)).save(proposalAddon1);
    }

    @Test
    public void deleteInvalidId(){

        when(proposalAddonRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Proposal Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        ()->proposalAddonService.delete(10L)
                ).getMessage()
        );

        verify(proposalAddonRepository,never()).save(any());
    }



}
