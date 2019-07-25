package com.st.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Product {

	@Id
	private String id;
	private Integer pid;
	private String pname;
	private double pcost;
	private Integer qty;
	private boolean status;
}
