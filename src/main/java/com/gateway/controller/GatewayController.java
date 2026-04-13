package com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

	/**
	 * 核心路由代理接口：内置混沌工程（Chaos Engineering）延迟注入功能。
	 * 用于模拟跨境网络抖动或后端微服务处理阻塞，验证前端或调用方的熔断降级机制。
	 */
// 在原来的类里增加这个方法
@GetMapping("/")
public String welcome() {
    return "<h1>🛡️ Resilience-Gateway 运行中</h1>" +
           "<p>这是一个具备混沌工程能力的网关原型。</p>" +
           "<ul>" +
           "<li>测试正常路径: <a href='/api/v1/proxy'>/api/v1/proxy</a></li>" +
           "<li>测试3秒延迟: <a href='/api/v1/proxy?delayMs=3000'>/api/v1/proxy?delayMs=3000</a></li>" +
           "<li>测试故障注入: <a href='/api/v1/proxy?forceError=true'>/api/v1/proxy?forceError=true</a></li>" +
           "</ul>";
}
}
	@GetMapping("/proxy")
	public ResponseEntity<Map<String, Object>> proxyRequest(
			@RequestParam(defaultValue = "0") long delayMs,
			@RequestParam(defaultValue = "false") boolean forceError) throws InterruptedException {

		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", Instant.now().toString());

		if (forceError) {
			response.put("status", "500");
			response.put("message", "Simulated Internal Server Error (Chaos Test)");
			return ResponseEntity.status(500).body(response);
		}

		if (delayMs > 0) {
			Thread.sleep(delayMs);
			response.put("latency_injected", delayMs + "ms");
		}

		response.put("status", "200");
		response.put("message", "Gateway Route Success");

		return ResponseEntity.ok(response);
	}

}
