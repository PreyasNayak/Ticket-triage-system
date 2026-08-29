package com.tickettriage.backend.service;

import com.tickettriage.backend.dto.TicketRequest;
import com.tickettriage.backend.exception.TicketNotFoundException;
import com.tickettriage.backend.model.Ticket;
import com.tickettriage.backend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(TicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public Ticket updateTicket(Long id, TicketRequest request) {
        Ticket ticket = getTicketById(id);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }
}
