package com.process.service;

public interface BinaryClassify {
//	Classify a message by 0 or 1;
//	0 denote non-finance and 1 denote finance
	public double BinaryClassify(String title,String content);
}
