package com.predictely.trading.model;

public class OrderblocksPayload {

	private String type; // e.g. "sellob", "buyob"
	private String symbol; // {{ticker}}
	private String exchange; // {{exchange}}
	private String interval; // {{interval}}
	private String time; // {{time}}
	private double top; // {{plot("sell_top")}} or {{plot("buy_top")}}
	private double mid; // {{plot("sell_mid")}} or {{plot("buy_mid")}}
	private double bottom; // {{plot("sell_bot")}} or {{plot("buy_bot")}}

	public OrderblocksPayload() {
	}

	public OrderblocksPayload(String type, String symbol, String exchange, String interval, String time, double top,
			double mid, double bottom) {
		this.type = type;
		this.symbol = symbol;
		this.exchange = exchange;
		this.interval = interval;
		this.time = time;
		this.top = top;
		this.mid = mid;
		this.bottom = bottom;
	}

	public static OrderblocksPayload of(String type, String symbol, String exchange, String interval, String time,
			double top, double mid, double bottom) {
		return new OrderblocksPayload(type, symbol, exchange, interval, time, top, mid, bottom);
	}

	// Getters and setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getTop() {
		return top;
	}

	public void setTop(double top) {
		this.top = top;
	}

	public double getMid() {
		return mid;
	}

	public void setMid(double mid) {
		this.mid = mid;
	}

	public double getBottom() {
		return bottom;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	@Override
	public String toString() {
		return "OrderblocksPayload [type=" + type + ", symbol=" + symbol + ", exchange=" + exchange + ", interval="
				+ interval + ", time=" + time + ", top=" + top + ", mid=" + mid + ", bottom=" + bottom + "]";
	}

}
