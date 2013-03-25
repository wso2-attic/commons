package com.mkyong.common;

/**
 * Model class for Stock
 */
public class Stock implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer stockId;
	private String stockCode = "defaultCode";
	private String stockName = "defaultName";

	public Stock() {
	}

	public Stock(String stockCode, String stockName) {
		if (stockCode!=null) this.stockCode = stockCode;
		if (stockName!=null) this.stockName = stockName;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}



}
