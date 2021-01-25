package com.xebia.solutions.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageResponse<T> {

	List<T> data;

	PaginationDetails details;

	public PageResponse(List<T> data, int pageNumber, int pageSize, int totalPages, long totalElements) {
		this.data = data;
		this.details = PaginationDetails.builder()
				.pageNumber(pageNumber)
				.pageSize(pageSize)
				.totalPages(totalPages)
				.totalSize(totalElements)
				.build();
	}
}
