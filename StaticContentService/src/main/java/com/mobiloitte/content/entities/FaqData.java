package com.mobiloitte.content.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table
//@Where(clause = "is_deleted!=true")
public class FaqData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long faqDataId;

	private String question;

	private String topicKey;

	private String topicName;

	@Lob
	@Column(length = 20971520)
	private String answer;

	private Boolean isDeleted;

	private Long fkFaqId;

	public Long getFaqDataId() {
		return faqDataId;
	}

	public void setFaqDataId(Long faqDataId) {
		this.faqDataId = faqDataId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getTopicKey() {
		return topicKey;
	}

	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getFkFaqId() {
		return fkFaqId;
	}

	public void setFkFaqId(Long fkFaqId) {
		this.fkFaqId = fkFaqId;
	}

}
