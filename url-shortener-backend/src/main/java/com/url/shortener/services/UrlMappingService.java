package com.url.shortener.services;

import com.url.shortener.dto.UrlMappingDTO;
import com.url.shortener.models.User;

public interface UrlMappingService {
   public UrlMappingDTO createShortUrl(String originalUrl, User user);
}
