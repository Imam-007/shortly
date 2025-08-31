package com.url.shortener.services.impl;

import com.url.shortener.dto.ClickEventDTO;
import com.url.shortener.dto.UrlMappingDTO;
import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;
import com.url.shortener.repositories.ClickEventRepository;
import com.url.shortener.repositories.UrlMappingRepository;
import com.url.shortener.services.UrlMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingServiceImpl implements UrlMappingService {
    private UrlMappingRepository urlMappingRepository;

    private ClickEventRepository clickEventRepository;

    @Override
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedAt(LocalDateTime.now());
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return convertToDto(savedUrlMapping);
    }

    private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();

        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedAt(urlMapping.getCreatedAt());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());

        return urlMappingDTO;
    }

    private String generateShortUrl() {
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    @Override
    public List<UrlMappingDTO> getUrlsByUser(User user){

        return urlMappingRepository.findByUser(user)
                .stream().map(this::convertToDto)
                .toList();
    }

    @Override
    public List<ClickEventDTO> getClickEventByDate(String shortUrl, LocalDate start, LocalDate end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMapping != null) {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start.atStartOfDay(), end.plusDays(1).atStartOfDay()).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> {
                        ClickEventDTO clickEventDTO = new ClickEventDTO();
                        clickEventDTO.setClickDate(entry.getKey());
                        clickEventDTO.setCount(entry.getValue());

                        return clickEventDTO;
                    })
                    .collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public Map<LocalDate, Long> getTotalClickByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());

        return clickEvents.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
    }
}
