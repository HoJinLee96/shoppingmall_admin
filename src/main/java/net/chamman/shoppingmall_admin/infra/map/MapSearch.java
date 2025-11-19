package net.chamman.shoppingmall_admin.infra.map;

import net.chamman.shoppingmall_admin.infra.map.dto.RoadSearchApiResponseDto;

public interface MapSearch {
	RoadSearchApiResponseDto roadSearch(String road, String analyzeType, int page, int size);
}
