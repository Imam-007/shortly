package com.url.shortener.services;

import com.url.shortener.dto.UrlMappingDTO;
import com.url.shortener.models.User;

import java.util.List;

public interface UrlMappingService {
   public UrlMappingDTO createShortUrl(String originalUrl, User user);

   public List<UrlMappingDTO> getUrlsByUser(User user);
}
