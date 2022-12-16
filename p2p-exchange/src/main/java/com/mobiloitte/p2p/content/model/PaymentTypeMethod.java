package com.mobiloitte.p2p.content.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.mobiloitte.usermanagement.model.User;

@Entity
@Table
public class PaymentTypeMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentTypeId;

	private String typeName;
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	private P2PAdvertisement p2pAdvertisement;

	public Long getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public P2PAdvertisement getP2pAdvertisement() {
		return p2pAdvertisement;
	}

	public void setP2pAdvertisement(P2PAdvertisement p2pAdvertisement) {
		this.p2pAdvertisement = p2pAdvertisement;
	}

}
