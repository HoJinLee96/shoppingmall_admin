package net.chamman.shoppingmall_admin.infra.map;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.domain.address.AddressInvalidException;
import net.chamman.shoppingmall_admin.infra.map.dto.RoadSearchApiResponseDto;
import net.chamman.shoppingmall_admin.infra.map.dto.StandardAddress;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapSearchService {
	
	private final MapSearch mapSearch;

	private static final int MAX_SIZE = 30;

	private boolean isPostcodeInSearchResult(String postcode, String address, String analyzeType) {
		int page = 1;

		while (true) {
			RoadSearchApiResponseDto roadSearchApiResponseDto = mapSearch.roadSearch(address, analyzeType, page, MAX_SIZE);

			if (roadSearchApiResponseDto.isEmpty()) {
				return false;
			}

			List<StandardAddress> standardAddresses = roadSearchApiResponseDto.getStandardAddresses();

			boolean match = standardAddresses.stream().map(StandardAddress::zoneCode).anyMatch(postcode::equals);
			if (match) return true;

			if (roadSearchApiResponseDto.isEnd()) {
				return false;
			}

			page++;
		}
	}

	public boolean validateAddress(String postcode, String mainAddress) {
		log.debug("* 주소 검증 시작. mainAddress: [{}]", LogMaskingUtil.maskAddress(mainAddress, MaskLevel.NONE));

		// 1차: exact 시도
		if (isPostcodeInSearchResult(postcode, mainAddress, "exact"))
			return true;

		// 2차: similar 시도
		if (isPostcodeInSearchResult(postcode, mainAddress, "similar"))
			return true;

		// 3차: mainAddress가 길거나 숫자로 끝나는 경우 제거해서 다시 시도
		String fallbackAddress = mainAddress.replaceFirst("\\s\\d{1,4}(-\\d{1,4})?$", "");  // ex) "동교로 7" → "동교로"
		if (isPostcodeInSearchResult(postcode, fallbackAddress, "similar"))
			return true;

		throw new AddressInvalidException("API 검증 결과 입력한 주소와 우편번호에 일치하는 값이 없습니다.");
	}
	
}
