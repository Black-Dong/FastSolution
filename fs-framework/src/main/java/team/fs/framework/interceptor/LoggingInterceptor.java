package team.fs.framework.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ClientHttpRequestInterceptor
 *
 * @author gaowei
 * @version 1.0
 * @date 2022/9/5 9:57
 */

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        displayRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        // 包装代理一下
        response = new ClientHttpResponseWrapper(response);
        displayResponse(response);
        return response;
    }

    /**
     * 显示请求相关信息
     *
     * @param request
     * @param body
     */
    private void displayRequest(HttpRequest request, byte[] body) {
        log.debug("====request info====");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("Req Headers : {}", this.headersToString(request.getHeaders()));
        log.debug("Request body: {}", body == null ? "" : new String(body, StandardCharsets.UTF_8));
    }

    /**
     * 显示响应相关信息
     *
     * @param response
     * @throws IOException
     */
    private void displayResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                inputStringBuilder.append(line);
                inputStringBuilder.append('\n');
                line = bufferedReader.readLine();
            }
        }
        log.debug("====response info====");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Status text  : {}", response.getStatusText());
        log.debug("Resp Headers : {}", headersToString(response.getHeaders()));
        log.debug("Response body: {}", inputStringBuilder.toString());
    }

    /**
     * 将Http头信息格式化处理
     *
     * @param httpHeaders
     * @return
     */
    private String headersToString(HttpHeaders httpHeaders) {
        if (Objects.isNull(httpHeaders)) {
            return "[]";
        }
        return httpHeaders.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return "\t" + entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", \n", "\n[\n", "\n]\n"));
    }


    private class ClientHttpResponseWrapper implements ClientHttpResponse {
        private ClientHttpResponse clientHttpResponse;
        private byte[] body;

        public ClientHttpResponseWrapper(ClientHttpResponse clientHttpResponse) {
            this.clientHttpResponse = clientHttpResponse;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return this.clientHttpResponse.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.clientHttpResponse.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.clientHttpResponse.getStatusText();
        }

        @Override
        public void close() {
            this.clientHttpResponse.close();
        }

        /**
         * 缓存body每次返回一个新的输入流
         *
         * @return
         * @throws IOException
         */
        @Override
        public InputStream getBody() throws IOException {
            if (Objects.isNull(this.body)) {
                this.body = StreamUtils.copyToByteArray(this.clientHttpResponse.getBody());
            }
            return new ByteArrayInputStream(this.body == null ? new byte[0] : this.body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.clientHttpResponse.getHeaders();
        }
    }
}
