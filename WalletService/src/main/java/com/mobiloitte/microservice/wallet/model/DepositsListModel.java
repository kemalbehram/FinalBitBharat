package com.mobiloitte.microservice.wallet.model;

/**
 * The Class DepositsListModel.
 * @author Ankush Mohapatra
 */
public class DepositsListModel {

	/** The transaction hash. */
	private String transaction_hash;
	
	/** The balance. */
	private String balance;
	
	/** The from. */
	private String from;
	
	/** The to. */
	private String to;
	
	/** The created at. */
	private String created_at;
	
	/** The failure message. */
	private String failureMessage;
	
	/**
	 * Gets the failure message.
	 *
	 * @return the failure message
	 */
	public String getFailureMessage() {
		return failureMessage;
	}
	
	/**
	 * Sets the failure message.
	 *
	 * @param failureMessage the new failure message
	 */
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
	
	/**
	 * Gets the transaction hash.
	 *
	 * @return the transaction_hash
	 */
	public String getTransaction_hash() {
		return transaction_hash;
	}
	
	/**
	 * Sets the transaction hash.
	 *
	 * @param transaction_hash the transaction_hash to set
	 */
	public void setTransaction_hash(String transaction_hash) {
		this.transaction_hash = transaction_hash;
	}
	
	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}
	
	/**
	 * Sets the balance.
	 *
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * Sets the from.
	 *
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * Sets the to.
	 *
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * Gets the created at.
	 *
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}
	
	/**
	 * Sets the created at.
	 *
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DepositsListModel [transaction_hash=" + transaction_hash + ", balance=" + balance + ", from=" + from
				+ ", to=" + to + ", created_at=" + created_at + ", failureMessage=" + failureMessage + "]";
	}

}
