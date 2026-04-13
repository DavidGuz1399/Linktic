package com.products.products_service.config;

import com.products.products_service.services.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimitService.resolveBucket(ip);

        if (bucket.tryConsume(1)){
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setHeader("x-Rate-Limit-Retry-After-Seconds", "60");
            response.getWriter().write("Demasiadas peticiones. Intenta de nuevo mas tarde");
        }
    }
}
