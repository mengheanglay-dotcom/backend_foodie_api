package com.api.v1.bearerauth.service;
import com.api.v1.bearerauth.model.response.FavoriteResponse;
import java.util.List;
public interface FavoriteService { List<String> ids(); List<FavoriteResponse> list(); boolean toggle(Long productId); }
