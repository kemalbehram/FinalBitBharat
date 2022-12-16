package com.mobiloitte.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Blog;

public interface BlogDao extends JpaRepository<Blog, Long> {

	List<Blog> findByOrderByCreatedAtAsc();

	List<Blog> findByOrderByCreatedAtAsc(Pageable of);

	List<Blog> findByTitleOrderByCreatedAtAsc(String title, Pageable of);

	List<Blog> findByTitleContainsOrderByCreatedAtAsc(String title, Pageable of);

}
