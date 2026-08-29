package com.tickettriage.backend.service;

import com.tickettriage.backend.dto.TicketRequest;
import com.tickettriage.backend.exception.TicketNotFoundException;
import com.tickettriage.backend.model.Ticket;
import com.tickettriage.backend.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private TicketRequest request;

    @BeforeEach
    void setUp() {
        request = new TicketRequest();
        request.setTitle("Login page returns 500");
        request.setDescription("Users see a server error when submitting the login form.");
    }

    @Test
    void createTicket_savesAndReturnsTicket() {
        Ticket saved = new Ticket();
        saved.setTitle(request.getTitle());
        saved.setDescription(request.getDescription());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(saved);

        Ticket result = ticketService.createTicket(request);

        assertThat(result.getTitle()).isEqualTo("Login page returns 500");
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void getTicketById_returnsTicket_whenFound() {
        Ticket existing = new Ticket();
        existing.setTitle("Existing ticket");
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existing));

        Ticket result = ticketService.getTicketById(1L);

        assertThat(result.getTitle()).isEqualTo("Existing ticket");
    }

    @Test
    void getTicketById_throws_whenNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getTicketById(99L))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllTickets_returnsAllFromRepository() {
        when(ticketRepository.findAll()).thenReturn(List.of(new Ticket(), new Ticket()));

        List<Ticket> result = ticketService.getAllTickets();

        assertThat(result).hasSize(2);
    }

    @Test
    void deleteTicket_deletes_whenFound() {
        Ticket existing = new Ticket();
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existing));

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).delete(existing);
    }

    @Test
    void deleteTicket_throws_whenNotFound() {
        when(ticketRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.deleteTicket(42L))
                .isInstanceOf(TicketNotFoundException.class);

        verify(ticketRepository, never()).delete(any());
    }
}
