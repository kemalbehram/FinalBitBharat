package com.mobiloitte.ohlc.model;

public class OHLCDto {
	/** open */
	private Double o;
	/** high */
	private Double h;
	/** low */
	private Double l;
	/** close */
	private Double c;
	/** average */
	private Double a;
	/** volume */
	private Double v;
	/** time */
	private Long t;

	public Double getO() {
		return o;
	}

	public void setO(Double o) {
		this.o = o;
	}

	public Double getH() {
		return h;
	}

	public void setH(Double h) {
		this.h = h;
	}

	public Double getL() {
		return l;
	}

	public void setL(Double l) {
		this.l = l;
	}

	public Double getC() {
		return c;
	}

	public void setC(Double c) {
		this.c = c;
	}

	public Double getA() {
		return a;
	}

	public void setA(Double a) {
		this.a = a;
	}

	public Double getV() {
		return v;
	}

	public void setV(Double v) {
		this.v = v;
	}

	public Long getT() {
		return t;
	}

	public void setT(Long t) {
		this.t = t;
	}

}
