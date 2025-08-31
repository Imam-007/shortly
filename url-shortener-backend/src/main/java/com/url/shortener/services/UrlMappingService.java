package com.url.shortener.services;

import com.url.shortener.dto.ClickEventDTO;
import com.url.shortener.dto.UrlMappingDTO;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UrlMappingService {
   public UrlMappingDTO createShortUrl(String originalUrl, User user);

   public List<UrlMappingDTO> getUrlsByUser(User user);

   public List<ClickEventDTO> getClickEventByDate(String shortUrl, LocalDate start, LocalDate end);

   public Map<LocalDate, Long> getTotalClickByUserAndDate(User user, LocalDate start, LocalDate end);

   public UrlMapping getOriginalUrl(String shortUrl);
}
