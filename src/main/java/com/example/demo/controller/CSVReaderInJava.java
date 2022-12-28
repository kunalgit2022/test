package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVReaderInJava {
	public List<String[]> readData() throws IOException {
		int count = 0;
		String file = "D:\\users_tbl.csv";
		List<String[]> content = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				content.add(line.split(","));
			}
		} catch (FileNotFoundException e) {
			// Some error logging
		}
		return content;
	}
}
