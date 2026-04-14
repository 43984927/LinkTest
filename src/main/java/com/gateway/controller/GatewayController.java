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

	@GetMapping("/chaos")
	public String chaosTest() {
		return "<!DOCTYPE html>" +
		       "<html lang='zh-CN'>" +
		       "<head>" +
		       "    <meta charset='UTF-8'>" +
		       "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
		       "    <title>🛡️ 混沌工程测试</title>" +
		       "    <style>" +
		       "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
		       "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }" +
		       "        .container { background: white; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 40px; max-width: 800px; margin: 0 auto; }" +
		       "        .status { display: inline-block; background: #28a745; color: white; padding: 5px 15px; border-radius: 20px; font-size: 12px; margin-bottom: 20px; }" +
		       "        h1 { color: #333; margin-bottom: 10px; font-size: 32px; }" +
		       "        .subtitle { color: #666; margin-bottom: 30px; font-size: 14px; }" +
		       "        .test-links { background: #f8f9fa; border-radius: 15px; padding: 25px; margin-top: 20px; }" +
		       "        .test-links h3 { color: #333; margin-bottom: 20px; font-size: 18px; }" +
		       "        .link-item { background: white; border: 2px solid #e0e0e0; border-radius: 10px; padding: 15px 20px; margin-bottom: 15px; transition: all 0.3s; }" +
		       "        .link-item:hover { border-color: #667eea; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }" +
		       "        .link-item a { color: #667eea; text-decoration: none; font-weight: 500; font-size: 16px; display: block; }" +
		       "        .link-item a:hover { color: #764ba2; }" +
		       "        .link-desc { color: #666; font-size: 13px; margin-top: 5px; }" +
		       "        .back-link { display: inline-block; margin-top: 20px; color: #667eea; text-decoration: none; font-size: 14px; }" +
		       "        .back-link:hover { text-decoration: underline; }" +
		       "        .icon { font-size: 20px; margin-right: 10px; }" +
		       "    </style>" +
		       "</head>" +
		       "<body>" +
		       "    <div class='container'>" +
		       "        <div class='status'>✅ 运行中</div>" +
		       "        <h1>🛡️ Resilience-Gateway</h1>" +
		       "        <p class='subtitle'>这是一个具备混沌工程能力的网关原型，支持故障注入和延迟测试。</p>" +
		       "        " +
		       "        <div class='test-links'>" +
		       "            <h3>🧪 测试接口</h3>" +
		       "            <div class='link-item'>" +
		       "                <a href='/api/v1/proxy'><span class='icon'>📄</span>/api/v1/proxy</a>" +
		       "                <div class='link-desc'>正常请求 - 测试网关基本功能</div>" +
		       "            </div>" +
		       "            <div class='link-item'>" +
		       "                <a href='/api/v1/proxy?delayMs=3000'><span class='icon'>⏳</span>/api/v1/proxy?delayMs=3000</a>" +
		       "                <div class='link-desc'>测试3秒延迟 - 模拟网络延迟</div>" +
		       "            </div>" +
		       "            <div class='link-item'>" +
		       "                <a href='/api/v1/proxy?forceError=true'><span class='icon'>❌</span>/api/v1/proxy?forceError=true</a>" +
		       "                <div class='link-desc'>测试故障注入 - 模拟服务故障</div>" +
		       "            </div>" +
		       "        </div>" +
		       "        " +
		       "        <a href='/' class='back-link'>← 返回主页</a>" +
		       "    </div>" +
		       "</body>" +
		       "</html>";
	}

	@GetMapping("/home")
	public String home() {
		return "<!DOCTYPE html>" +
		       "<html lang='zh-CN'>" +
		       "<head>" +
		       "    <meta charset='UTF-8'>" +
		       "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
		       "    <title>🌐 链路测速工具</title>" +
		       "    <style>" +
		       "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
		       "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; display: flex; align-items: center; justify-content: center; }" +
		       "        .container { background: white; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 50px; max-width: 600px; width: 100%; }" +
		       "        h1 { color: #333; margin-bottom: 10px; font-size: 36px; text-align: center; }" +
		       "        .subtitle { color: #666; margin-bottom: 40px; font-size: 14px; text-align: center; }" +
		       "        .menu { display: flex; flex-direction: column; gap: 20px; }" +
		       "        .menu-item { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 25px 30px; border-radius: 15px; text-decoration: none; transition: all 0.3s; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 5px 15px rgba(0,0,0,0.2); }" +
		       "        .menu-item:hover { transform: translateY(-3px); box-shadow: 0 10px 25px rgba(0,0,0,0.3); }" +
		       "        .menu-item .icon { font-size: 40px; }" +
		       "        .menu-item .content { flex: 1; margin-left: 20px; }" +
		       "        .menu-item .title { font-size: 20px; font-weight: bold; margin-bottom: 5px; }" +
		       "        .menu-item .desc { font-size: 13px; opacity: 0.9; }" +
		       "        .menu-item .arrow { font-size: 24px; opacity: 0.7; }" +
		       "        .footer { text-align: center; margin-top: 30px; color: #999; font-size: 12px; }" +
		       "    </style>" +
		       "</head>" +
		       "<body>" +
		       "    <div class='container'>" +
		       "        <h1>🌐 链路测速工具</h1>" +
		       "        <p class='subtitle'>全球网络连接质量测试平台</p>" +
		       "        " +
		       "        <div class='menu'>" +
		       "            <a href='/speedtest' class='menu-item'>" +
		       "                <span class='icon'>🚀</span>" +
		       "                <div class='content'>" +
		       "                    <div class='title'>链路测速</div>" +
		       "                    <div class='desc'>测试到全球各地区的网络延迟和丢包率</div>" +
		       "                </div>" +
		       "                <span class='arrow'>→</span>" +
		       "            </a>" +
		       "            " +
		       "            <a href='/api/v1/chaos' class='menu-item'>" +
		       "                <span class='icon'>🛡️</span>" +
		       "                <div class='content'>" +
		       "                    <div class='title'>混沌工程测试</div>" +
		       "                    <div class='desc'>模拟故障和延迟，测试系统稳定性</div>" +
		       "                </div>" +
		       "                <span class='arrow'>→</span>" +
		       "            </a>" +
		       "            " +
		       "            <a href='/api/v1/linktest/nodes' class='menu-item'>" +
		       "                <span class='icon'>🌍</span>" +
		       "                <div class='content'>" +
		       "                    <div class='title'>测试节点列表</div>" +
		       "                    <div class='desc'>查看全球测试节点详细信息</div>" +
		       "                </div>" +
		       "                <span class='arrow'>→</span>" +
		       "            </a>" +
		       "        </div>" +
		       "        " +
		       "        <div class='footer'>" +
		       "            Powered by Spring Boot | Deployed on Hugging Face Spaces" +
		       "        </div>" +
		       "    </div>" +
		       "</body>" +
		       "</html>";
	}

	@GetMapping("/speedtest")
	public String speedtest() {
		return "<!DOCTYPE html>" +
		       "<html lang='zh-CN'>" +
		       "<head>" +
		       "    <meta charset='UTF-8'>" +
		       "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
		       "    <title>🚀 链路测速</title>" +
		       "    <style>" +
		       "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
		       "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }" +
		       "        .container { background: white; border-radius: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3); padding: 40px; max-width: 1000px; margin: 0 auto; }" +
		       "        h1 { color: #333; margin-bottom: 10px; font-size: 32px; text-align: center; }" +
		       "        .subtitle { color: #666; margin-bottom: 30px; font-size: 14px; text-align: center; }" +
		       "        .ip-info { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 15px; margin-bottom: 30px; text-align: center; }" +
		       "        .ip-address { font-size: 28px; font-weight: bold; margin-bottom: 5px; }" +
		       "        .ip-location { font-size: 16px; opacity: 0.9; }" +
		       "        .btn { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; padding: 15px 40px; font-size: 18px; border-radius: 50px; cursor: pointer; transition: all 0.3s; display: block; margin: 20px auto; }" +
		       "        .btn:hover { transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0,0,0,0.2); }" +
		       "        .btn:disabled { opacity: 0.6; cursor: not-allowed; transform: none; }" +
		       "        .regions { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 20px; margin-top: 30px; }" +
		       "        .region-card { background: #f8f9fa; border-radius: 15px; padding: 20px; border: 2px solid transparent; transition: all 0.3s; }" +
		       "        .region-card.testing { border-color: #667eea; background: #f0f4ff; }" +
		       "        .region-card.good { border-color: #28a745; }" +
		       "        .region-card.warning { border-color: #ffc107; }" +
		       "        .region-card.error { border-color: #dc3545; }" +
		       "        .region-name { font-size: 18px; font-weight: bold; color: #333; margin-bottom: 15px; display: flex; align-items: center; }" +
		       "        .region-flag { font-size: 24px; margin-right: 10px; }" +
		       "        .metrics { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }" +
		       "        .metric { text-align: center; }" +
		       "        .metric-label { font-size: 11px; color: #999; margin-bottom: 5px; }" +
		       "        .metric-value { font-size: 18px; font-weight: bold; color: #333; }" +
		       "        .metric-value.good { color: #28a745; }" +
		       "        .metric-value.warning { color: #ffc107; }" +
		       "        .metric-value.error { color: #dc3545; }" +
		       "        .progress { width: 100%; height: 4px; background: #e0e0e0; border-radius: 2px; margin-top: 15px; overflow: hidden; }" +
		       "        .progress-bar { height: 100%; background: linear-gradient(90deg, #667eea, #764ba2); transition: width 0.3s; }" +
		       "        .status { text-align: center; margin-top: 20px; font-size: 14px; color: #666; }" +
		       "        .back-link { display: inline-block; margin-top: 20px; color: #667eea; text-decoration: none; font-size: 14px; }" +
		       "        .back-link:hover { text-decoration: underline; }" +
		       "        @keyframes pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }" +
		       "        .testing .region-name::after { content: '测试中...'; font-size: 12px; color: #667eea; margin-left: auto; animation: pulse 1.5s infinite; }" +
		       "    </style>" +
		       "</head>" +
		       "<body>" +
		       "    <div class='container'>" +
		       "        <h1>🚀 链路测速</h1>" +
		       "        <p class='subtitle'>测试您到全球各地区的网络连接质量</p>" +
		       "        " +
		       "        <div class='ip-info' id='ipInfo'>" +
		       "            <div class='ip-address' id='ipAddress'>检测中...</div>" +
		       "            <div class='ip-location' id='ipLocation'>正在获取位置信息</div>" +
		       "        </div>" +
		       "        " +
		       "        <button class='btn' id='startBtn' onclick='startTest()'>开始测试</button>" +
		       "        " +
		       "        <div class='status' id='status'>点击开始测试</div>" +
		       "        " +
		       "        <div class='regions' id='regions'></div>" +
		       "        " +
		       "        <a href='/' class='back-link'>← 返回主页</a>" +
		       "    </div>" +
		       "    " +
		       "    <script>" +
		       "        const regions = ['北美', '欧洲', '东南亚', '拉美', '中东'];" +
		       "        const flags = { '北美': '🇺🇸', '欧洲': '🇪🇺', '东南亚': '🇸🇬', '拉美': '🇧🇷', '中东': '🇦🇪' };" +
		       "        " +
		       "        async function loadIPInfo() {" +
		       "            try {" +
		       "                const response = await fetch('/api/v1/linktest/myip');" +
		       "                const data = await response.json();" +
		       "                document.getElementById('ipAddress').textContent = data.ip || 'Unknown';" +
		       "                document.getElementById('ipLocation').textContent = '📍 ' + (data.location || 'Unknown');" +
		       "            } catch (error) {" +
		       "                document.getElementById('ipAddress').textContent = '获取失败';" +
		       "                document.getElementById('ipLocation').textContent = '无法获取位置信息';" +
		       "            }" +
		       "        }" +
		       "        " +
		       "        function createRegionCards() {" +
		       "            const container = document.getElementById('regions');" +
		       "            container.innerHTML = '';" +
		       "            regions.forEach(region => {" +
		       "                const card = document.createElement('div');" +
		       "                card.className = 'region-card';" +
		       "                card.id = 'region-' + region;" +
		       "                card.innerHTML = `" +
		       "                    <div class='region-name'>" +
		       "                        <span class='region-flag'>${flags[region]}</span>" +
		       "                        <span>${region}</span>" +
		       "                    </div>" +
		       "                    <div class='metrics'>" +
		       "                        <div class='metric'>" +
		       "                            <div class='metric-label'>延迟</div>" +
		       "                            <div class='metric-value' id='ping-${region}'>--</div>" +
		       "                        </div>" +
		       "                        <div class='metric'>" +
		       "                            <div class='metric-label'>丢包率</div>" +
		       "                            <div class='metric-value' id='loss-${region}'>--</div>" +
		       "                        </div>" +
		       "                        <div class='metric'>" +
		       "                            <div class='metric-label'>状态</div>" +
		       "                            <div class='metric-value' id='status-${region}'>--</div>" +
		       "                        </div>" +
		       "                    </div>" +
		       "                    <div class='progress'>" +
		       "                        <div class='progress-bar' style='width: 0%'></div>" +
		       "                    </div>" +
		       "                `;" +
		       "                container.appendChild(card);" +
		       "            });" +
		       "        }" +
		       "        " +
		       "        async function startTest() {" +
		       "            const btn = document.getElementById('startBtn');" +
		       "            const status = document.getElementById('status');" +
		       "            " +
		       "            btn.disabled = true;" +
		       "            status.textContent = '正在测试所有地区...';" +
		       "            " +
		       "            regions.forEach(region => {" +
		       "                const card = document.getElementById('region-' + region);" +
		       "                card.className = 'region-card testing';" +
		       "                card.querySelector('.progress-bar').style.width = '0%';" +
		       "            });" +
		       "            " +
		       "            try {" +
		       "                const response = await fetch('/api/v1/linktest/pingall');" +
		       "                const data = await response.json();" +
		       "                " +
		       "                if (data.results) {" +
		       "                    Object.keys(data.results).forEach((region, index) => {" +
		       "                        setTimeout(() => {" +
		       "                            updateRegionCard(region, data.results[region]);" +
		       "                        }, index * 200);" +
		       "                    });" +
		       "                }" +
		       "                " +
		       "                setTimeout(() => {" +
		       "                    status.textContent = '测试完成！';" +
		       "                    btn.disabled = false;" +
		       "                }, regions.length * 200 + 500);" +
		       "                " +
		       "            } catch (error) {" +
		       "                status.textContent = '测试失败: ' + error.message;" +
		       "                btn.disabled = false;" +
		       "            }" +
		       "        }" +
		       "        " +
		       "        function updateRegionCard(region, data) {" +
		       "            const card = document.getElementById('region-' + region);" +
		       "            const pingEl = document.getElementById('ping-' + region);" +
		       "            const lossEl = document.getElementById('loss-' + region);" +
		       "            const statusEl = document.getElementById('status-' + region);" +
		       "            const progressBar = card.querySelector('.progress-bar');" +
		       "            " +
		       "            progressBar.style.width = '100%';" +
		       "            " +
		       "            if (data.avgPing) {" +
		       "                pingEl.textContent = data.avgPing + 'ms';" +
		       "                const ping = parseFloat(data.avgPing);" +
		       "                if (ping < 100) {" +
		       "                    pingEl.className = 'metric-value good';" +
		       "                } else if (ping < 200) {" +
		       "                    pingEl.className = 'metric-value warning';" +
		       "                } else {" +
		       "                    pingEl.className = 'metric-value error';" +
		       "                }" +
		       "            }" +
		       "            " +
		       "            if (data.packetLoss) {" +
		       "                lossEl.textContent = data.packetLoss;" +
		       "                const loss = parseFloat(data.packetLoss);" +
		       "                if (loss === 0) {" +
		       "                    lossEl.className = 'metric-value good';" +
		       "                } else if (loss < 50) {" +
		       "                    lossEl.className = 'metric-value warning';" +
		       "                } else {" +
		       "                    lossEl.className = 'metric-value error';" +
		       "                }" +
		       "            }" +
		       "            " +
		       "            if (data.status) {" +
		       "                statusEl.textContent = data.status === 'good' ? '良好' : " +
		       "                                      data.status === 'timeout' ? '超时' : '错误';" +
		       "                statusEl.className = 'metric-value ' + (data.status === 'good' ? 'good' : 'error');" +
		       "            }" +
		       "            " +
		       "            card.className = 'region-card ' + (data.status === 'good' ? 'good' : " +
		       "                                              data.status === 'timeout' ? 'warning' : 'error');" +
		       "        }" +
		       "        " +
		       "        loadIPInfo();" +
		       "        createRegionCards();" +
		       "    </script>" +
		       "</body>" +
		       "</html>";
	}

}
