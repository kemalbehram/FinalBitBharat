package com.mobiloitte.content.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.TicketReplyData;

public interface TicketReplyDao extends JpaRepository<TicketReplyData, Long> {

	List<TicketReplyData> findByTicketId(Long ticketId);

//	List<TicketReplyData> findByTicketIdOrderByTicketReplyIdDesc(Long ticketId);

//	List<TicketReplyData> findByTicketId(Long ticketId);

}
