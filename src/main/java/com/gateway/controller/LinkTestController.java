package com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/linktest")
public class LinkTestController {

	private static final Map<String, TestEndpoint> TEST_REGIONS = new LinkedHashMap<>();
	
	static {
		TEST_REGIONS.put("北美", new TestEndpoint("https://www.google.com", "8.8.8.8"));
		TEST_REGIONS.put("欧洲", new TestEndpoint("https://www.cloudflare.com", "1.1.1.1"));
		TEST_REGIONS.put("东南亚", new TestEndpoint("https://www.yahoo.com", "208.67.222.222"));
		TEST_REGIONS.put("拉美", new TestEndpoint("https://www.wikipedia.org", "9.9.9.9"));
		TEST_REGIONS.put("中东", new TestEndpoint("https://www.github.com", "149.112.112.112"));
	}

	private static class TestEndpoint {
		String url;
		String ip;
		
		TestEndpoint(String url, String ip) {
			this.url = url;
			this.ip = ip;
		}
	}

	@GetMapping("/regions")
	public ResponseEntity<Map<String, Object>> getTestRegions() {
		Map<String, Object> response = new HashMap<>();
		response.put("regions", TEST_REGIONS.keySet());
		response.put("total", TEST_REGIONS.size());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/nodes")
	public ResponseEntity<String> getNodesPage() {
		String html = "<!DOCTYPE html>" +
			"<html lang='zh-CN'>" +
			"<head>" +
			"    <meta charset='UTF-8'>" +
			"    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
			"    <title>🌍 测试节点列表</title>" +
			"    <style>" +
			"        * { margin: 0; padding: 0; box-sizing: border-box; }" +
			"        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }" +
			"        .container { background: white; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 40px; max-width: 900px; margin: 0 auto; }" +
			"        h1 { color: #333; margin-bottom: 10px; font-size: 32px; text-align: center; }" +
			"        .subtitle { color: #666; margin-bottom: 30px; font-size: 14px; text-align: center; }" +
			"        .back-link { display: inline-block; margin-top: 20px; color: #667eea; text-decoration: none; font-size: 14px; }" +
			"        .back-link:hover { text-decoration: underline; }" +
			"        .nodes-table { width: 100%; border-collapse: collapse; margin-top: 20px; }" +
			"        .nodes-table th { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 15px; text-align: left; font-weight: 600; }" +
			"        .nodes-table th:first-child { border-radius: 10px 0 0 0; }" +
			"        .nodes-table th:last-child { border-radius: 0 10px 0 0; }" +
			"        .nodes-table td { padding: 15px; border-bottom: 1px solid #e0e0e0; }" +
			"        .nodes-table tr:hover { background: #f8f9fa; }" +
			"        .nodes-table tr:last-child td:first-child { border-radius: 0 0 0 10px; }" +
			"        .nodes-table tr:last-child td:last-child { border-radius: 0 0 10px 0; }" +
			"        .region-cell { display: flex; align-items: center; font-weight: 600; color: #333; }" +
			"        .region-flag { font-size: 24px; margin-right: 10px; }" +
			"        .region-name { font-size: 16px; }" +
			"        .host-cell { font-family: monospace; color: #667eea; font-size: 13px; }" +
			"        .ip-cell { font-family: monospace; color: #666; font-size: 13px; }" +
			"        .desc-cell { color: #666; font-size: 13px; }" +
			"        .status-badge { display: inline-block; padding: 5px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; }" +
			"        .status-badge.active { background: #d4edda; color: #155724; }" +
			"        .status-badge.test { background: #cce5ff; color: #004085; }" +
			"        .test-btn { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; padding: 8px 20px; border-radius: 20px; cursor: pointer; font-size: 13px; transition: all 0.3s; }" +
			"        .test-btn:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,0,0,0.2); }" +
			"        .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0; color: #999; font-size: 12px; }" +
			"    </style>" +
			"</head>" +
			"<body>" +
			"    <div class='container'>" +
			"        <h1>🌍 测试节点列表</h1>" +
			"        <p class='subtitle'>全球网络连接质量测试节点</p>" +
			"        " +
			"        <table class='nodes-table'>" +
			"            <thead>" +
			"                <tr>" +
			"                    <th>地区</th>" +
			"                    <th>测试地址</th>" +
			"                    <th>DNS IP</th>" +
			"                    <th>说明</th>" +
			"                    <th>状态</th>" +
			"                    <th>操作</th>" +
			"                </tr>" +
			"            </thead>" +
			"            <tbody>" +
			"                <tr>" +
			"                    <td><div class='region-cell'><span class='region-flag'>🇺🇸</span><span class='region-name'>北美</span></div></td>" +
			"                    <td><span class='host-cell'>https://www.google.com</span></td>" +
			"                    <td><span class='ip-cell'>8.8.8.8</span></td>" +
			"                    <td><span class='desc-cell'>Google DNS，全球最大搜索引擎</span></td>" +
			"                    <td><span class='status-badge active'>活跃</span></td>" +
			"                    <td><button class='test-btn' onclick=\"testRegion('北美')\">测试</button></td>" +
			"                </tr>" +
			"                <tr>" +
			"                    <td><div class='region-cell'><span class='region-flag'>🇪🇺</span><span class='region-name'>欧洲</span></div></td>" +
			"                    <td><span class='host-cell'>https://www.cloudflare.com</span></td>" +
			"                    <td><span class='ip-cell'>1.1.1.1</span></td>" +
			"                    <td><span class='desc-cell'>Cloudflare DNS，全球领先 CDN</span></td>" +
			"                    <td><span class='status-badge active'>活跃</span></td>" +
			"                    <td><button class='test-btn' onclick=\"testRegion('欧洲')\">测试</button></td>" +
			"                </tr>" +
			"                <tr>" +
			"                    <td><div class='region-cell'><span class='region-flag'>🇸🇬</span><span class='region-name'>东南亚</span></div></td>" +
			"                    <td><span class='host-cell'>https://www.yahoo.com</span></td>" +
			"                    <td><span class='ip-cell'>208.67.222.222</span></td>" +
			"                    <td><span class='desc-cell'>OpenDNS，亚太地区重要节点</span></td>" +
			"                    <td><span class='status-badge active'>活跃</span></td>" +
			"                    <td><button class='test-btn' onclick=\"testRegion('东南亚')\">测试</button></td>" +
			"                </tr>" +
			"                <tr>" +
			"                    <td><div class='region-cell'><span class='region-flag'>🇧🇷</span><span class='region-name'>拉美</span></div></td>" +
			"                    <td><span class='host-cell'>https://www.wikipedia.org</span></td>" +
			"                    <td><span class='ip-cell'>9.9.9.9</span></td>" +
			"                    <td><span class='desc-cell'>Quad9 DNS，拉美地区节点</span></td>" +
			"                    <td><span class='status-badge active'>活跃</span></td>" +
			"                    <td><button class='test-btn' onclick=\"testRegion('拉美')\">测试</button></td>" +
			"                </tr>" +
			"                <tr>" +
			"                    <td><div class='region-cell'><span class='region-flag'>🇦🇪</span><span class='region-name'>中东</span></div></td>" +
			"                    <td><span class='host-cell'>https://www.github.com</span></td>" +
			"                    <td><span class='ip-cell'>149.112.112.112</span></td>" +
			"                    <td><span class='desc-cell'>GitHub，中东地区重要节点</span></td>" +
			"                    <td><span class='status-badge active'>活跃</span></td>" +
			"                    <td><button class='test-btn' onclick=\"testRegion('中东')\">测试</button></td>" +
			"                </tr>" +
			"            </tbody>" +
			"        </table>" +
			"        " +
			"        <div class='footer'>" +
			"            共 5 个测试节点 | 数据更新于 " + java.time.LocalDateTime.now().toString().replace("T", " ") + 
			"        </div>" +
			"        " +
			"        <a href='/' class='back-link'>← 返回主页</a>" +
			"    </div>" +
			"    " +
			"    <script>" +
			"        async function testRegion(region) {" +
			"            if (!confirm('确定要测试 ' + region + ' 吗？')) return;" +
			"            " +
			"            const btn = event.target;" +
			"            btn.textContent = '测试中...';" +
			"            btn.disabled = true;" +
			"            " +
			"            try {" +
			"                const response = await fetch('/api/v1/linktest/ping/' + region);" +
			"                const data = await response.json();" +
			"                " +
			"                let result = '地区: ' + region + '\\n';" +
			"                result += '主机: ' + data.host + '\\n';" +
			"                result += '发送包: ' + data.packetsSent + '\\n';" +
			"                result += '接收包: ' + data.packetsReceived + '\\n';" +
			"                result += '丢包率: ' + data.packetLoss + '\\n';" +
			"                result += '平均延迟: ' + data.avgPing + '\\n';" +
			"                result += '状态: ' + data.status;" +
			"                " +
			"                alert(result);" +
			"            } catch (error) {" +
			"                alert('测试失败: ' + error.message);" +
			"            } finally {" +
			"                btn.textContent = '测试';" +
			"                btn.disabled = false;" +
			"            }" +
			"        }" +
			"    </script>" +
			"</body>" +
			"</html>";
		
		return ResponseEntity.ok(html);
	}

	@GetMapping("/ping/{region}")
	public ResponseEntity<Map<String, Object>> pingTest(@PathVariable String region) {
		Map<String, Object> response = new HashMap<>();
		
		TestEndpoint endpoint = TEST_REGIONS.get(region);
		if (endpoint == null) {
			response.put("error", "Region not found");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			List<Long> pingTimes = new ArrayList<>();
			int packetsSent = 4;
			int packetsReceived = 0;

			for (int i = 0; i < packetsSent; i++) {
				try {
					long startTime = System.currentTimeMillis();
					boolean reachable = testHttpConnection(endpoint.url);
					long endTime = System.currentTimeMillis();

					if (reachable) {
						pingTimes.add(endTime - startTime);
						packetsReceived++;
					}
					Thread.sleep(100);
				} catch (Exception e) {
					// Packet lost
				}
			}

			double packetLoss = ((packetsSent - packetsReceived) / (double) packetsSent) * 100;
			
			response.put("region", region);
			response.put("host", endpoint.url);
			response.put("packetsSent", packetsSent);
			response.put("packetsReceived", packetsReceived);
			response.put("packetLoss", String.format("%.1f", packetLoss) + "%");
			
			if (!pingTimes.isEmpty()) {
				double avgPing = pingTimes.stream().mapToLong(Long::longValue).average().orElse(0);
				long minPing = Collections.min(pingTimes);
				long maxPing = Collections.max(pingTimes);
				
				response.put("avgPing", String.format("%.0f", avgPing) + "ms");
				response.put("minPing", minPing + "ms");
				response.put("maxPing", maxPing + "ms");
				response.put("status", packetsReceived > 0 ? "reachable" : "unreachable");
			} else {
				response.put("avgPing", "--");
				response.put("minPing", "--");
				response.put("maxPing", "--");
				response.put("status", "unreachable");
			}

		} catch (Exception e) {
			response.put("error", e.getMessage());
			response.put("status", "error");
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/pingall")
	public ResponseEntity<Map<String, Object>> pingAllRegions() {
		Map<String, Object> response = new HashMap<>();
		Map<String, Map<String, Object>> results = new LinkedHashMap<>();

		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<Map<String, Object>>> futures = new ArrayList<>();

		for (Map.Entry<String, TestEndpoint> entry : TEST_REGIONS.entrySet()) {
			String region = entry.getKey();
			TestEndpoint endpoint = entry.getValue();

			futures.add(executor.submit(() -> {
				Map<String, Object> result = new HashMap<>();
				try {
					List<Long> pingTimes = new ArrayList<>();
					int packetsSent = 3;
					int packetsReceived = 0;

					for (int i = 0; i < packetsSent; i++) {
						try {
							long startTime = System.currentTimeMillis();
							boolean reachable = testHttpConnection(endpoint.url);
							long endTime = System.currentTimeMillis();

							if (reachable) {
								pingTimes.add(endTime - startTime);
								packetsReceived++;
							}
							Thread.sleep(50);
						} catch (Exception e) {
							// Packet lost
						}
					}

					double packetLoss = ((packetsSent - packetsReceived) / (double) packetsSent) * 100;
					
					result.put("host", endpoint.url);
					result.put("packetLoss", String.format("%.0f", packetLoss) + "%");
					
					if (!pingTimes.isEmpty()) {
						double avgPing = pingTimes.stream().mapToLong(Long::longValue).average().orElse(0);
						result.put("avgPing", String.format("%.0f", avgPing));
						result.put("status", "good");
					} else {
						result.put("avgPing", "--");
						result.put("status", "timeout");
					}

				} catch (Exception e) {
					result.put("error", e.getMessage());
					result.put("status", "error");
					result.put("avgPing", "--");
					result.put("packetLoss", "100%");
				}

				return result;
			}));
		}

		try {
			for (int i = 0; i < futures.size(); i++) {
				String region = (String) TEST_REGIONS.keySet().toArray()[i];
				results.put(region, futures.get(i).get(10, TimeUnit.SECONDS));
			}
		} catch (Exception e) {
			response.put("error", e.getMessage());
		} finally {
			executor.shutdown();
		}

		response.put("results", results);
		response.put("timestamp", System.currentTimeMillis());
		return ResponseEntity.ok(response);
	}

	private boolean testHttpConnection(String urlStr) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);
			connection.setInstanceFollowRedirects(false);
			
			int responseCode = connection.getResponseCode();
			return responseCode > 0;
		} catch (IOException e) {
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	@GetMapping("/myip")
	public ResponseEntity<Map<String, Object>> getMyIP(@RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
			                                           @RequestHeader(value = "X-Real-IP", required = false) String realIP,
			                                           @RequestHeader(value = "CF-Connecting-IP", required = false) String cfIP) {
		Map<String, Object> response = new HashMap<>();
		
		String clientIP = null;
		
		if (cfIP != null && !cfIP.isEmpty()) {
			clientIP = cfIP.trim();
		} else if (forwardedFor != null && !forwardedFor.isEmpty()) {
			clientIP = forwardedFor.split(",")[0].trim();
		} else if (realIP != null && !realIP.isEmpty()) {
			clientIP = realIP.trim();
		}
		
		if (clientIP == null || clientIP.isEmpty()) {
			clientIP = getPublicIP();
		}

		if (clientIP != null && !clientIP.isEmpty()) {
			response.put("ip", clientIP);
			response.put("location", detectLocation(clientIP));
		} else {
			response.put("ip", "检测中");
			response.put("location", "未知");
		}

		response.put("timestamp", System.currentTimeMillis());
		return ResponseEntity.ok(response);
	}

	private String getPublicIP() {
		String[] ipServices = {
			"https://api.ipify.org",
			"https://icanhazip.com",
			"https://ifconfig.me/ip"
		};
		
		for (String service : ipServices) {
			HttpURLConnection connection = null;
			try {
				URL url = new URL(service);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(2000);
				connection.setReadTimeout(2000);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String ip = reader.readLine().trim();
				reader.close();
				
				if (ip != null && !ip.isEmpty()) {
					return ip;
				}
			} catch (Exception e) {
				// Try next service
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
		
		return null;
	}

	private String detectLocation(String ip) {
		if (ip == null || ip.isEmpty()) {
			return "未知";
		}
		
		if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
			return "本地网络";
		}
		
		if (ip.contains(".")) {
			String[] parts = ip.split("\\.");
			if (parts.length >= 1) {
				try {
					int firstOctet = Integer.parseInt(parts[0]);
					
					if (firstOctet >= 1 && firstOctet <= 50) return "北美";
					if (firstOctet >= 51 && firstOctet <= 100) return "欧洲";
					if (firstOctet >= 101 && firstOctet <= 150) return "东南亚";
					if (firstOctet >= 151 && firstOctet <= 180) return "拉美";
					if (firstOctet >= 181 && firstOctet <= 200) return "中东";
					if (firstOctet >= 201 && firstOctet <= 223) return "亚太";
				} catch (NumberFormatException e) {
					return "未知";
				}
			}
		}
		
		return "未知";
	}

}
