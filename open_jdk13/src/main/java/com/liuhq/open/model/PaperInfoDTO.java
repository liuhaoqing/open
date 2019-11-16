package com.liuhq.open.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaperInfoDTO {

	private ModelDTO model;
	
	private List<ItemDTO> items;
	
	private List<SectionDTO> sections;
	
}
