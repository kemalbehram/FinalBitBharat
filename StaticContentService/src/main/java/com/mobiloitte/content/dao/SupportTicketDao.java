package com.mobiloitte.content.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.SupportTicket;

public interface SupportTicketDao extends JpaRepository<SupportTicket, Long> {

}
