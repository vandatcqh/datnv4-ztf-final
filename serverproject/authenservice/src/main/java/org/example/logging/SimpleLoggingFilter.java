package org.example.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SimpleLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SimpleLoggingFilter.class);
    private final Tracer tracer;

    // Jackson mapper (Spring Boot cung cấp sẵn)
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Trường nhạy cảm cần mask nếu xuất hiện trong requestBody (tên phổ biến)
    private static final Set<String> SENSITIVE_FIELDS = new HashSet<String>(Arrays.asList(
            "password", "passwd", "pass", "token", "authorization", "auth", "accessToken", "refreshToken"
    ));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        Span span = tracer.spanBuilder(httpRequest.getMethod() + " " + httpRequest.getRequestURI())
                .setSpanKind(io.opentelemetry.api.trace.SpanKind.SERVER)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            MDC.put("traceId", span.getSpanContext().getTraceId());
            MDC.put("spanId", span.getSpanContext().getSpanId());

            chain.doFilter(requestWrapper, responseWrapper);

        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            long timestamp = System.currentTimeMillis();

            span.setAttribute("http.status_code", responseWrapper.getStatus());
            span.setAttribute("http.duration_ms", duration);
            span.end();

            // Lấy body (giới hạn 4KB)
            String rawRequestBody = getContentAsString(requestWrapper.getContentAsByteArray());
            String rawResponseBody = getContentAsString(responseWrapper.getContentAsByteArray());

            // copy response trở lại client (bắt buộc)
            responseWrapper.copyBodyToResponse();

            // headers -> map
            Map<String, String> headersMap = Collections.list(httpRequest.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            name -> name,
                            httpRequest::getHeader,
                            (a, b) -> a, // nếu trùng key (ít khả năng)
                            LinkedHashMap::new
                    ));

            // parse request/response body nếu là JSON, ngược lại giữ nguyên string
            Object parsedRequestBody = parseJsonSafe(rawRequestBody);
            Object parsedResponseBody = parseJsonSafe(rawResponseBody);

            // Nếu request body là Map -> mask các field nhạy cảm
            if (parsedRequestBody instanceof Map) {
                maskSensitiveFieldsInMap((Map<?, ?>) parsedRequestBody);
            }

            if (parsedResponseBody instanceof Map) {
                maskSensitiveFieldsInMap((Map<?, ?>) parsedResponseBody);
            }

            // Xây log map
            Map<String, Object> logMap = new LinkedHashMap<String, Object>();
            logMap.put("timestamp", timestamp);
            logMap.put("method", httpRequest.getMethod());
            logMap.put("uri", httpRequest.getRequestURI());
            logMap.put("status", responseWrapper.getStatus());
            logMap.put("durationMs", duration);
            logMap.put("headers", headersMap);
            logMap.put("requestBody", parsedRequestBody != null ? parsedRequestBody : rawRequestBody);
            logMap.put("responseBody", parsedResponseBody != null ? parsedResponseBody : rawResponseBody);

            // Convert to JSON string (fallback to plain message if serialization fails)
            try {
                String jsonLog = objectMapper.writeValueAsString(logMap);
                log.info(jsonLog);
            } catch (JsonProcessingException e) {
                // Không nên xảy ra, nhưng phòng trường hợp mapper lỗi -> log fallback
                log.info("{\"message\":\"failed_to_serialize_log\",\"traceId\":\"{}\",\"error\":\"{}\"}",
                        span.getSpanContext().getTraceId(), e.getMessage());
            }

            MDC.clear();
        }
    }

    private String getContentAsString(byte[] buf) {
        if (buf == null || buf.length == 0) return "";
        int length = Math.min(buf.length, 4096); // giới hạn 4KB
        return new String(buf, 0, length, StandardCharsets.UTF_8);
    }

    // Thử parse JSON; nếu không parse được trả về null (caller sẽ dùng raw string)
    private Object parseJsonSafe(String json) {
        if (json == null) return null;
        String trimmed = json.trim();
        if (trimmed.isEmpty()) return null;
        // nếu không bắt đầu bằng { hoặc [ -> có khả năng không phải JSON
        if (!(trimmed.startsWith("{") || trimmed.startsWith("["))) {
            return null;
        }
        try {
            return objectMapper.readValue(trimmed, Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Mask hoặc replace giá trị các key nhạy cảm trong Map (đệ quy cho nested maps)
    @SuppressWarnings("unchecked")
    private void maskSensitiveFieldsInMap(Map<?, ?> map) {
        if (map == null) return;
        for (Object keyObj : new ArrayList<Object>(map.keySet())) {
            if (!(keyObj instanceof String)) continue;
            String key = ((String) keyObj);
            Object val = map.get(keyObj);
            if (val == null) continue;

            // Nếu key nhạy cảm -> replace value
            if (SENSITIVE_FIELDS.contains(key.toLowerCase())) {
                // do cast
                ((Map<String, Object>) map).put(key, "***REDACTED***");
                continue;
            }

            // Nếu value là Map -> đệ quy
            if (val instanceof Map) {
                maskSensitiveFieldsInMap((Map<?, ?>) val);
            }

            // Nếu value là List -> duyệt phần tử là Map
            if (val instanceof List) {
                List<?> list = (List<?>) val;
                for (Object el : list) {
                    if (el instanceof Map) {
                        maskSensitiveFieldsInMap((Map<?, ?>) el);
                    }
                }
            }
        }
    }


}
