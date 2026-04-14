---
title: 链路测速
emoji: 🌐
colorFrom: indigo
colorTo: blue
sdk: docker
pinned: false
short_description: 全球网络连接质量测试工具
---

# 🌐 链路测速 - 全球网络连接质量测试工具

一个基于 Spring Boot 的全球网络链路测试工具，支持多地区延迟测试、丢包率检测和 IP 地理位置识别。

## ✨ 功能特性

### 🔗 全球链路测试
- **多地区测试**: 支持北美、欧洲、东南亚、拉美、中东等地区的网络连接测试
- **延迟测试**: 实时测量到各地区的网络延迟
- **丢包率检测**: 准确计算网络丢包率
- **连接状态**: 直观显示网络连接质量（良好/超时/错误）

### 🌍 IP 地理定位
- **自动识别**: 自动检测用户公网 IP 地址
- **地区判断**: 智能 IP 地理位置识别
- **多源获取**: 支持多种 IP 获取方式，确保准确性

### 🛡️ 混沌工程测试
- **故障注入**: 模拟服务故障，测试系统容错能力
- **延迟注入**: 模拟网络延迟，验证超时处理机制
- **错误模拟**: 测试系统在异常情况下的表现

### 🎨 美观的用户界面
- **响应式设计**: 支持桌面和移动设备
- **实时更新**: 测试进度实时显示
- **颜色编码**: 绿色（良好）、黄色（警告）、红色（错误）
- **动画效果**: 流畅的测试动画和交互反馈

## 🚀 快速开始

### 在线访问

- **Hugging Face Spaces**: [https://huggingface.co/spaces/chanfasf/Resilience-Gateway](https://huggingface.co/spaces/chanfasf/Resilience-Gateway)
- **GitHub**: [https://github.com/43984927/LinkTest](https://github.com/43984927/LinkTest)

### 本地运行

#### 前置要求
- Java 17 或更高版本
- Maven 3.6+
- Docker（可选）

#### 方式一：使用 Maven

```bash
# 克隆项目
git clone https://github.com/43984927/LinkTest.git
cd LinkTest

# 编译项目
mvn clean package

# 运行应用
java -jar target/resilience-gateway-0.0.1-SNAPSHOT.jar
```

#### 方式二：使用 Docker

```bash
# 构建镜像
docker build -t link-test .

# 运行容器
docker run -p 7860:7860 link-test
```

访问地址：http://localhost:7860

## 📖 使用说明

### 链路测速

1. 访问主页，系统自动检测您的 IP 地址和地区
2. 点击"开始测试"按钮
3. 等待测试完成（约 5-10 秒）
4. 查看各地区的延迟、丢包率和连接状态

### 混沌工程测试

访问 `/api/v1/chaos` 页面，可以测试：

- **正常请求**: `/api/v1/proxy` - 测试网关基本功能
- **延迟测试**: `/api/v1/proxy?delayMs=3000` - 模拟 3 秒延迟
- **故障注入**: `/api/v1/proxy?forceError=true` - 模拟服务故障

## 🔌 API 接口文档

### 链路测试接口

#### 获取测试地区列表
```
GET /api/v1/linktest/regions
```

**响应示例**:
```json
{
  "regions": ["北美", "欧洲", "东南亚", "拉美", "中东"],
  "total": 5
}
```

#### 测试指定地区
```
GET /api/v1/linktest/ping/{region}
```

**响应示例**:
```json
{
  "region": "北美",
  "host": "https://www.google.com",
  "packetsSent": 4,
  "packetsReceived": 4,
  "packetLoss": "0%",
  "avgPing": "120ms",
  "minPing": "115ms",
  "maxPing": "125ms",
  "status": "reachable"
}
```

#### 测试所有地区
```
GET /api/v1/linktest/pingall
```

**响应示例**:
```json
{
  "results": {
    "北美": {
      "host": "https://www.google.com",
      "packetLoss": "0%",
      "avgPing": "120",
      "status": "good"
    }
  },
  "timestamp": 1234567890
}
```

#### 获取当前 IP 信息
```
GET /api/v1/linktest/myip
```

**响应示例**:
```json
{
  "ip": "123.45.67.89",
  "location": "亚太",
  "timestamp": 1234567890
}
```

### 混沌工程接口

#### 正常请求
```
GET /api/v1/proxy
```

#### 延迟注入
```
GET /api/v1/proxy?delayMs=3000
```

#### 故障注入
```
GET /api/v1/proxy?forceError=true
```

## 🏗️ 项目结构

```
Resilience-Gateway/
├── .github/
│   └── workflows/          # GitHub Actions 工作流
├── src/
│   └── main/
│       ├── java/
│       │   └── com/gateway/
│       │       ├── ResilienceGatewayApplication.java
│       │       └── controller/
│       │           ├── GatewayController.java      # 主控制器
│       │           └── LinkTestController.java     # 链路测试控制器
│       └── resources/
│           └── application.properties              # 应用配置
├── Dockerfile              # Docker 构建文件
├── pom.xml                 # Maven 配置
└── README.md              # 项目文档
```

## 🛠️ 技术栈

- **后端框架**: Spring Boot 3.x
- **构建工具**: Maven
- **容器化**: Docker
- **部署平台**: Hugging Face Spaces
- **测试方法**: HTTP 请求测试（适配容器环境）

## 🌐 测试节点

| 地区 | 测试目标 | 说明 |
|------|---------|------|
| 北美 🇺🇸 | Google | 测试到北美的连接质量 |
| 欧洲 🇪🇺 | Cloudflare | 测试到欧洲的连接质量 |
| 东南亚 🇸🇬 | Yahoo | 测试到东南亚的连接质量 |
| 拉美 🇧🇷 | Wikipedia | 测试到拉美的连接质量 |
| 中东 🇦🇪 | GitHub | 测试到中东的连接质量 |

## 📊 性能指标

- **延迟评估**:
  - 🟢 < 100ms: 良好
  - 🟡 100-200ms: 一般
  - 🔴 > 200ms: 较慢

- **丢包率评估**:
  - 🟢 0%: 优秀
  - 🟡 < 50%: 一般
  - 🔴 ≥ 50%: 较差

## 🔒 安全说明

- 本工具仅用于网络连接质量测试
- 不收集或存储任何用户数据
- 所有测试均为公开可访问的服务
- IP 信息仅用于地理位置识别

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 更新日志

### v1.0.0 (2026-04-14)
- ✨ 初始版本发布
- 🌐 支持全球 5 个地区的链路测试
- 🎨 美观的用户界面
- 🛡️ 混沌工程测试功能
- 🌍 IP 地理位置识别

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 后端框架
- [Hugging Face Spaces](https://huggingface.co/spaces) - 免费部署平台
- [GitHub](https://github.com) - 代码托管平台

## 📮 联系方式

- GitHub: [@43984927](https://github.com/43984927)
- 项目地址: [https://github.com/43984927/LinkTest](https://github.com/43984927/LinkTest)

---

⭐ 如果这个项目对您有帮助，请给一个 Star！
