package com.xebia.solutions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDetails {

	private int pageNumber;
	private int pageSize;

	private int totalPages;
	private long totalSize;
}
