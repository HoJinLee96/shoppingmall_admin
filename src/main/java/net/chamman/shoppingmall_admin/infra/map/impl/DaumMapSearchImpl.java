package net.chamman.shoppingmall_admin.infra.map.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import net.chamman.shoppingmall_admin.exception.infra.map.MapSearchException;
import net.chamman.shoppingmall_admin.infra.map.MapSearch;
import net.chamman.shoppingmall_admin.infra.map.dto.KakaoRoadSearchApiResponseDto;
import net.chamman.shoppingmall_admin.infra.map.dto.RoadSearchApiResponseDto;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil;
import net.chamman.shoppingmall_admin.support.util.LogMaskingUtil.MaskLevel;

@Component
@Slf4j
@PropertySource("classpath:application.properties")
public class DaumMapSearchImpl implements MapSearch {

	@Value("${kakao-addressSearch.baseUrl}")
	private String baseUrl;
	@Value("${kakao-api.restApiKey}")
	private String kakaoApiRestApiKey;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public RoadSearchApiResponseDto roadSearch(String road, String analyzeType, int page, int size) {
		try {
			UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUrl).queryParam("query", road)
					.queryParam("analyze_type", analyzeType).queryParam("page", page).queryParam("size", size).build();

			log.debug("* 카카오 주소 검색 요청. road: [{}], analyzeType: [{}], page: [{}], size: [{}]",
					LogMaskingUtil.maskAddress(road, MaskLevel.NONE), analyzeType, page, size);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "KakaoAK " + kakaoApiRestApiKey);

			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<KakaoRoadSearchApiResponseDto> response = restTemplate.exchange(uriComponents.toUriString(),
					HttpMethod.GET, entity, KakaoRoadSearchApiResponseDto.class);

			KakaoRoadSearchApiResponseDto body = response.getBody();

			if (body == null) {
				throw new MapSearchException("카카오 주소 검색 api 응답 body가 null입니다.");
			}
			
			log.debug("* 카카오 주소 검색 성공: [{}]", body);
			return body;
		} catch (Exception e) {
			log.error("* 카카오 주소 검색 실패. road: [{}], analyzeType: [{}], page: [{}], size: [{}]", road, analyzeType, page,
					size, e);
			throw new MapSearchException("카카오 주소 검색 api 도중 익셉션 발생");
		}
	}

}
