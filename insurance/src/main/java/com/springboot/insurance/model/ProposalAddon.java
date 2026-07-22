package com.springboot.insurance.model;


import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "proposal_addon")
public class ProposalAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double addonPrice;

    @ManyToOne
    @JoinColumn(name = "proposal_id", nullable = false)
    private Proposal proposal;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private Addon addon;

    public ProposalAddonResponseDto convertEntityToDto(ProposalAddon proposalAddon) {
        ProposalAddonResponseDto dto = new ProposalAddonResponseDto(
                proposalAddon.getAddonPrice()
        );
        return dto;
    }
}
