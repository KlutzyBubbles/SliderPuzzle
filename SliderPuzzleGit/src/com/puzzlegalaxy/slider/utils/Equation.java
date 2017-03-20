package com.puzzlegalaxy.slider.utils;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.puzzlegalaxy.slider.Main;
import com.puzzlegalaxy.slider.exceptions.InvalidExpressionException;

public class Equation {

	private String expression;
	private LinkedList<String> tokens;
	private int num;
	private double result = 69.69;
	private boolean tokenized = false;
	
	public Equation(String expression, int num) {
		this.setExpression(expression);
		this.setNum(num);
		this.tokens = new LinkedList<String>();
	}
	
	public Equation(String expression) {
		this(expression, 0);
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
		this.tokenized = false;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		this.tokenized = false;
	}
	
	public double getRawResult() throws InvalidExpressionException {
		if (this.result == 69.69D)
			throw new InvalidExpressionException("There is no result to return (Code: 0)");
		return this.result;
	}
	
	public int getRoundedResult() throws InvalidExpressionException {
		if (this.result == 69.69D)
			throw new InvalidExpressionException("There is no result to return (Code: 0)");
		return (int)Math.round(this.result);
	}
	
	public boolean isValid() {
		try {
			Main.debug("RESULT: " + this.getRawResult());
		} catch (InvalidExpressionException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public double evaluate() throws InvalidExpressionException {
		if (!this.tokenized)
			this.tokenize();
		double value = this.evaluateTerm();
		while (this.tokens.size() >= 1 && (this.tokens.peek().equals("-") || this.tokens.peek().equals("+"))) {
			String current = this.tokens.pop();
			if (this.tokens.size() == 0)
				throw new InvalidExpressionException("The equation cannot end with an operator (Code: 2)");
			if (current.equals("-")) {
				value -= this.evaluateTerm();
			} else {
				value += this.evaluateTerm();
			}
		}
		this.result = value;
		return value;
	}
	
	private double evaluateTerm() throws InvalidExpressionException {
		double value = this.evaluateFactor();
		while (this.tokens.size() >= 1 && (this.tokens.peek().equals("*") || this.tokens.peek().equals("/"))) {
			String current = this.tokens.pop();
			if (this.tokens.size() == 0)
				throw new InvalidExpressionException("The equation cannot end with an operator (Code: 2)");
			if (current.equals("*")) {
				value *= this.evaluateFactor();
			} else {
				value /= this.evaluateFactor();
			}
		}
		return value;
	}
	
	private double evaluateFactor() throws InvalidExpressionException {
		try {
			String token = this.tokens.peek();
			double value;
			if (token.equals("(")) {
				this.tokens.pop();
				value = this.evaluate();
				token = this.tokens.pop();
				if (!token.equals(")"))
					throw new InvalidExpressionException("The last token found is illegal (Code: 4)");
				return value;
			}
			return this.evaluateNumber();
		} catch (NoSuchElementException e) {
			throw new InvalidExpressionException("The token to pop doesnt exist (Code: 3)");
		} catch (NullPointerException e) {
			throw new InvalidExpressionException("A NPE occurred while evaluating the expression (Code: Unknown)");
		}
	}
	
	private double evaluateNumber() throws InvalidExpressionException {
		boolean negative = false;
		try {
			if (this.tokens.get(0).equals("-")) {
				negative = true;
				this.tokens.pop();
			}
			double value = Double.parseDouble(this.tokens.pop());
			return negative ? -value : value;
		} catch (NumberFormatException e) {
			throw new InvalidExpressionException("The negative value must be a number (Code: 5)");
		}
	}
	
	public void tokenize() throws InvalidExpressionException {
		StringBuilder current = new StringBuilder();
		String value = this.expression.replace("x", "" + this.num);
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			switch (ch) {
			case ' ':
				if (!current.toString().isEmpty()) {
					this.tokens.add(current.toString());
					current = new StringBuilder();
				}
				break;
			case '+':
			case '*':
			case '/':
			case '(':
			case ')':
			case '-':
				if (!current.toString().isEmpty()) {
					this.tokens.add(current.toString());
					current = new StringBuilder();
				}
				this.tokens.add("" + ch);
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6': 
			case '7': 
			case '8':
			case '9':
			case '.':
				current.append(ch);
				break;
			default:
				throw new InvalidExpressionException("The expression contains invalid characters (Code: 6)");
			}
		}
		if (!current.toString().isEmpty())
			this.tokens.add(current.toString());
		this.tokenized = true;
	}
	
	public void removeSpaces() {
		if (this.expression.contains(" ")) {
			this.expression.replace(" ", "");
			this.tokenized = false;
		}
	}
	
	public void replaceVariables() {
		if (this.expression.contains("x") || this.expression.contains("X")) {
			this.expression = this.expression.replace("x", "" + this.num).replace("x", "" + this.num);
			this.tokenized = false;
		}
	}
	
}
