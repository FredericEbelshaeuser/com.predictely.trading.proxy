package com.predictely.trading.model;

import java.time.ZonedDateTime;

public class WebhookPayload {
	
	private String action;
	private String symbol;
	private String exchange;
	private Double signal_price;
	private String strategy;
	private ZonedDateTime timestamp;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public Double getSignal_price() {
		return signal_price;
	}

	public void setSignal_price(Double signal_price) {
		this.signal_price = signal_price;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "WebhookPayload{" + "action='" + action + '\'' + ", symbol='" + symbol + '\'' + ", exchange='" + exchange
				+ '\'' + ", signal_price=" + signal_price + ", strategy='" + strategy + '\'' + ", timestamp="
				+ timestamp + '}';
	}
}
