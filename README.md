1、正式环境添加host解析

vim /etc/hosts

172.26.188.1 rszx-ka-001
172.26.188.2 rszx-ka-002
172.26.188.3 rszx-ka-003

2、将resource目录下的platform-redirect-uri.properties复制到
/opt/kafka/properties 目录下

3、SSO示例 客户端配置
(1) 添加oauth2依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
(2) 启动类添加 @EnableOAuth2Sso 注解
(3) 配置oauth2 client
security:
  oauth2:
    client:
      client-id: sso_one
      client-secret: sso_one_secret
      user-authorization-uri: http://127.0.0.1:8088/platform-kafka/oauth/authorize
      access-token-uri: http://127.0.0.1:8088/platform-kafka/oauth/token
    resource:
      jwt:
        key-uri: http://127.0.0.1:8088/platform-kafka/oauth/token_key