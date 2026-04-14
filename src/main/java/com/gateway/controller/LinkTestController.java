package com.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/linktest")
public class LinkTestController {

	private static final Map<String, String> TEST_REGIONS = new LinkedHashMap<>();
	
	static {
		TEST_REGIONS.put("北美", "8.8.8.8");
		TEST_REGIONS.put("欧洲", "1.1.1.1");
		TEST_REGIONS.put("东南亚", "208.67.222.222");
		TEST_REGIONS.put("拉美", "9.9.9.9");
		TEST_REGIONS.put("中东", "149.112.112.112");
	}

	@GetMapping("/regions")
	public ResponseEntity<Map<String, Object>> getTestRegions() {
		Map<String, Object> response = new HashMap<>();
		response.put("regions", TEST_REGIONS.keySet());
		response.put("total", TEST_REGIONS.size());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/ping/{region}")
	public ResponseEntity<Map<String, Object>> pingTest(@PathVariable String region) {
		Map<String, Object> response = new HashMap<>();
		
		String host = TEST_REGIONS.get(region);
		if (host == null) {
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
					InetAddress address = InetAddress.getByName(host);
					boolean reachable = address.isReachable(2000);
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
			response.put("host", host);
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

		for (Map.Entry<String, String> entry : TEST_REGIONS.entrySet()) {
			String region = entry.getKey();
			String host = entry.getValue();

			futures.add(executor.submit(() -> {
				Map<String, Object> result = new HashMap<>();
				try {
					List<Long> pingTimes = new ArrayList<>();
					int packetsSent = 3;
					int packetsReceived = 0;

					for (int i = 0; i < packetsSent; i++) {
						try {
							long startTime = System.currentTimeMillis();
							InetAddress address = InetAddress.getByName(host);
							boolean reachable = address.isReachable(2000);
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
					
					result.put("host", host);
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

	@GetMapping("/myip")
	public ResponseEntity<Map<String, Object>> getMyIP(@RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
			                                           @RequestHeader(value = "X-Real-IP", required = false) String realIP) {
		Map<String, Object> response = new HashMap<>();
		
		String clientIP = null;
		if (forwardedFor != null && !forwardedFor.isEmpty()) {
			clientIP = forwardedFor.split(",")[0].trim();
		} else if (realIP != null && !realIP.isEmpty()) {
			clientIP = realIP;
		}

		if (clientIP != null) {
			response.put("ip", clientIP);
			response.put("location", detectLocation(clientIP));
		} else {
			response.put("ip", "Unknown");
			response.put("location", "Unknown");
		}

		response.put("timestamp", System.currentTimeMillis());
		return ResponseEntity.ok(response);
	}

	private String detectLocation(String ip) {
		if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
			return "本地网络";
		}
		
		if (ip.contains(".")) {
			String[] parts = ip.split("\\.");
			if (parts.length >= 1) {
				int firstOctet = Integer.parseInt(parts[0]);
				
				if (firstOctet >= 1 && firstOctet <= 50) return "北美";
				if (firstOctet >= 51 && firstOctet <= 100) return "欧洲";
				if (firstOctet >= 101 && firstOctet <= 150) return "东南亚";
				if (firstOctet >= 151 && firstOctet <= 180) return "拉美";
				if (firstOctet >= 181 && firstOctet <= 200) return "中东";
				if (firstOctet >= 201 && firstOctet <= 223) return "亚太";
			}
		}
		
		return "未知地区";
	}

}
