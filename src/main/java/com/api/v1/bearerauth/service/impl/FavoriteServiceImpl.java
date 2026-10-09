package com.api.v1.bearerauth.service.impl;

import com.api.v1.bearerauth.common.exception.CustomException;
import com.api.v1.bearerauth.model.entity.*;
import com.api.v1.bearerauth.model.response.FavoriteResponse;
import com.api.v1.bearerauth.repository.*;
import com.api.v1.bearerauth.security.CurrentUserService;
import com.api.v1.bearerauth.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime; import java.util.List;

@Service @RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository; private final ProductRepository productRepository; private final CurrentUserService currentUserService;
    @Override @Transactional(readOnly=true) public List<String> ids(){ return favoriteRepository.findAllByUserOrderByCreatedAtDesc(currentUserService.getCurrentUser()).stream().map(f->String.valueOf(f.getProduct().getProductId())).toList(); }
    @Override @Transactional(readOnly=true) public List<FavoriteResponse> list(){ return favoriteRepository.findAllByUserOrderByCreatedAtDesc(currentUserService.getCurrentUser()).stream().map(f->{ FavoriteResponse r=new FavoriteResponse(); r.setId(f.getId()); r.setProductId(f.getProduct().getProductId()); r.setName(f.getProduct().getProductName()); r.setFavorite(true); return r; }).toList(); }
    @Override @Transactional public boolean toggle(Long productId){ AppUser u=currentUserService.getCurrentUser(); Product p=productRepository.findById(productId).orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Product not found")); var existing=favoriteRepository.findByUserAndProduct(u,p); if(existing.isPresent()){favoriteRepository.delete(existing.get()); return false;} Favorite f=new Favorite(); f.setUser(u); f.setProduct(p); f.setCreatedAt(LocalDateTime.now()); favoriteRepository.save(f); return true; }
}
