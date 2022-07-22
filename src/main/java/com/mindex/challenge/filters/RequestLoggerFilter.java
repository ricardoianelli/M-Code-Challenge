package com.mindex.challenge.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestLoggerFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logRequestInformation(request);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        filterChain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();

        logResponseInformation(request, response, stopWatch);

        responseWrapper.copyBodyToResponse();
    }

    public void logRequestInformation(HttpServletRequest request) {
        try {
            String logMessage = "Request: " +
                    request.getMethod() +
                    " - " +
                    request.getRequestURI();

            LOG.info(logMessage);
        } catch (Exception ex) {
            LOG.error("There was an exception logging a request: " + ex);
        }

    }

    public void logResponseInformation(HttpServletRequest request, HttpServletResponse response, StopWatch currentStopWatch) {
        try {
            String logMessage = "Response: " +
                    "[" +
                    response.getStatus() +
                    "] " +
                    request.getMethod() +
                    " - " +
                    request.getRequestURI() +
                    " - " +
                    currentStopWatch.getTotalTimeMillis() +
                    " ms.";

            LOG.info(logMessage);
        } catch (Exception ex) {
            LOG.error("There was an exception logging a response: " + ex);
        }
    }
}