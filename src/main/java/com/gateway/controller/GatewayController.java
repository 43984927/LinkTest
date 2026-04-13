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

	@GetMapping("/")
	public String welcome() {
		return "<!DOCTYPE html>" +
		       "<html lang='zh-CN'>" +
		       "<head>" +
		       "    <meta charset='UTF-8'>" +
		       "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
		       "    <title>🛡️ Resilience-Gateway</title>" +
		       "    <style>" +
		       "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
		       "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; justify-content: center; align-items: center; padding: 20px; }" +
		       "        .container { background: white; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 40px; max-width: 600px; width: 100%; text-align: center; }" +
		       "        h1 { color: #333; margin-bottom: 20px; font-size: 28px; }" +
		       "        p { color: #666; margin-bottom: 30px; font-size: 16px; line-height: 1.6; }" +
		       "        .test-links { background: #f8f9fa; border-radius: 10px; padding: 20px; margin-top: 20px; }" +
		       "        .test-links h3 { color: #333; margin-bottom: 15px; font-size: 18px; }" +
		       "        .link-item { display: block; margin: 10px 0; }" +
		       "        .link-item a { color: #667eea; text-decoration: none; font-size: 15px; transition: all 0.3s; }" +
		       "        .link-item a:hover { color: #764ba2; text-decoration: underline; }" +
		       "        .status { display: inline-block; padding: 5px 15px; background: #28a745; color: white; border-radius: 20px; font-size: 14px; margin-bottom: 20px; }" +
		       "    </style>" +
		       "</head>" +
		       "<body>" +
		       "    <div class='container'>" +
		       "        <div class='status'>✅ 运行中</div>" +
		       "        <h1>🛡️ Resilience-Gateway</h1>" +
		       "        <p>这是一个具备混沌工程能力的网关原型，支持故障注入和延迟测试。</p>" +
		       "        <div class='test-links'>" +
		       "            <h3>🧪 测试接口</h3>" +
		       "            <div class='link-item'>📄 <a href='/api/v1/proxy'>/api/v1/proxy</a> - 正常请求</div>" +
		       "            <div class='link-item'>⏳ <a href='/api/v1/proxy?delayMs=3000'>/api/v1/proxy?delayMs=3000</a> - 测试3秒延迟</div>" +
		       "            <div class='link-item'>❌ <a href='/api/v1/proxy?forceError=true'>/api/v1/proxy?forceError=true</a> - 测试故障注入</div>" +
		       "        </div>" +
		       "    </div>" +
		       "</body>" +
		       "</html>";
	}

}
