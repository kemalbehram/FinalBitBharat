package com.mobiloitte.microservice.wallet.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The Interface ApiUrlConstants.
 * 
 * @author Ankush Mohapatra
 */
@ConfigurationProperties("api.base-url")
@Component
public class ApiUrlConstants {

	/** The btc api base. */
	private String BTC_API_BASE;

	/** The ltc api base. */
	private String LTC_API_BASE;

	/** The xrp api base. */
	private String XRP_API_BASE;

	/** The neo api base. */
	private String NEO_API_BASE;

	/** The eth node api base. */
	private String ETH_NODE_API_BASE;

	/** The eos api base. */
	private String EOS_API_BASE;

	private String USDT_API_BASE;

	private String BCH_API_BASE;

	private String XLM_API_BASE;

	private String TRX_API_BASE;

	private String MIOTA_API_BASE;

	private String USDC_API_BASE;

	private String OMG_API_BASE;

	private String DASH_API_BASE;

	private String BNB_API_BASE;

	private String POLKADOT_API_BASE;

	private String MATIC_API_BASE;

	private String AVAX_API_BASE;

	private String SOLANA_API_BASE;

	/**
	 * Gets the btc api base.
	 *
	 * @return the btc api base
	 */
	public String getBTC_API_BASE() {
		return BTC_API_BASE;
	}

	/**
	 * Sets the btc api base.
	 *
	 * @param bTC_API_BASE the new btc api base
	 */
	public void setBTC_API_BASE(String bTC_API_BASE) {
		BTC_API_BASE = bTC_API_BASE;
	}

	/**
	 * Gets the xrp api base.
	 *
	 * @return the xrp api base
	 */
	public String getXRP_API_BASE() {
		return XRP_API_BASE;
	}

	/**
	 * Sets the xrp api base.
	 *
	 * @param xRP_API_BASE the new xrp api base
	 */
	public void setXRP_API_BASE(String xRP_API_BASE) {
		XRP_API_BASE = xRP_API_BASE;
	}

	/**
	 * Gets the neo api base.
	 *
	 * @return the neo api base
	 */
	public String getNEO_API_BASE() {
		return NEO_API_BASE;
	}

	/**
	 * Sets the neo api base.
	 *
	 * @param nEO_API_BASE the new neo api base
	 */
	public void setNEO_API_BASE(String nEO_API_BASE) {
		NEO_API_BASE = nEO_API_BASE;
	}

	/**
	 * Gets the eth node api base.
	 *
	 * @return the eth node api base
	 */
	public String getETH_NODE_API_BASE() {
		return ETH_NODE_API_BASE;
	}

	/**
	 * Sets the eth node api base.
	 *
	 * @param eTH_NODE_API_BASE the new eth node api base
	 */
	public void setETH_NODE_API_BASE(String eTH_NODE_API_BASE) {
		ETH_NODE_API_BASE = eTH_NODE_API_BASE;
	}

	/**
	 * Gets the eos api base.
	 *
	 * @return the eos api base
	 */
	public String getEOS_API_BASE() {
		return EOS_API_BASE;
	}

	/**
	 * Sets the eos api base.
	 *
	 * @param eOS_API_BASE the new eos api base
	 */
	public void setEOS_API_BASE(String eOS_API_BASE) {
		EOS_API_BASE = eOS_API_BASE;
	}

	/**
	 * Gets the ltc api base.
	 *
	 * @return the ltc api base
	 */
	public String getLTC_API_BASE() {
		return LTC_API_BASE;
	}

	/**
	 * Sets the ltc api base.
	 *
	 * @param lTC_API_BASE the new ltc api base
	 */
	public void setLTC_API_BASE(String lTC_API_BASE) {
		LTC_API_BASE = lTC_API_BASE;
	}

	public String getUSDT_API_BASE() {
		return USDT_API_BASE;
	}

	public void setUSDT_API_BASE(String uSDT_API_BASE) {
		USDT_API_BASE = uSDT_API_BASE;
	}

	public String getBCH_API_BASE() {
		return BCH_API_BASE;
	}

	public void setBCH_API_BASE(String bCH_API_BASE) {
		BCH_API_BASE = bCH_API_BASE;
	}

	public String getXLM_API_BASE() {
		return XLM_API_BASE;
	}

	public void setXLM_API_BASE(String xLM_API_BASE) {
		XLM_API_BASE = xLM_API_BASE;
	}

	public String getTRX_API_BASE() {
		return TRX_API_BASE;
	}

	public void setTRX_API_BASE(String tRX_API_BASE) {
		TRX_API_BASE = tRX_API_BASE;
	}

	public String getMIOTA_API_BASE() {
		return MIOTA_API_BASE;
	}

	public void setMIOTA_API_BASE(String mIOTA_API_BASE) {
		MIOTA_API_BASE = mIOTA_API_BASE;
	}

	public String getUSDC_API_BASE() {
		return USDC_API_BASE;
	}

	public void setUSDC_API_BASE(String uSDC_API_BASE) {
		USDC_API_BASE = uSDC_API_BASE;
	}

	public String getOMG_API_BASE() {
		return OMG_API_BASE;
	}

	public void setOMG_API_BASE(String oMG_API_BASE) {
		OMG_API_BASE = oMG_API_BASE;
	}

	public String getDASH_API_BASE() {
		return DASH_API_BASE;
	}

	public void setDASH_API_BASE(String dASH_API_BASE) {
		DASH_API_BASE = dASH_API_BASE;
	}

	public String getBNB_API_BASE() {
		return BNB_API_BASE;
	}

	public void setBNB_API_BASE(String bNB_API_BASE) {
		BNB_API_BASE = bNB_API_BASE;
	}

	public String getPOLKADOT_API_BASE() {
		return POLKADOT_API_BASE;
	}

	public void setPOLKADOT_API_BASE(String pOLKADOT_API_BASE) {
		POLKADOT_API_BASE = pOLKADOT_API_BASE;
	}

	public final String getMATIC_API_BASE() {
		return MATIC_API_BASE;
	}

	public final void setMATIC_API_BASE(String mATIC_API_BASE) {
		MATIC_API_BASE = mATIC_API_BASE;
	}

	public final String getAVAX_API_BASE() {
		return AVAX_API_BASE;
	}

	public final void setAVAX_API_BASE(String aVAX_API_BASE) {
		AVAX_API_BASE = aVAX_API_BASE;
	}

	public final String getSOLANA_API_BASE() {
		return SOLANA_API_BASE;
	}

	public final void setSOLANA_API_BASE(String sOLANA_API_BASE) {
		SOLANA_API_BASE = sOLANA_API_BASE;
	}

}
