http://localhost:8081/template1?user_name=Иван&area_name=Москве
![img1.png](img1.png)

http://localhost:8081/template2?user_name=%D0%90%D0%BD%D0%BD%D0%B0&area_name=%D0%A1%D0%B0%D0%BD%D0%BA%D1%82-%D0%9F%D0%B5%D1%82%D0%B5%D1%80%D0%B1%D1%83%D1%80%D0%B3%D0%B5&registration_date=15.05.2023
![img2.png](img2.png)

http://localhost:8081/template3?user_name=%D0%9F%D0%B5%D1%82%D1%80&area_name=%D0%9D%D0%BE%D0%B2%D0%BE%D1%81%D0%B8%D0%B1%D0%B8%D1%80%D1%81%D0%BA%D0%B5&expiry_date=31.12.2023
![img3.png](img3.png)

## Алгоритм

#### 1. При запуске загружаем шаблоны в память и помещаем в конкурентную мапу для быстрого доступа

```...
   private final Map<String, String> templates = new ConcurrentHashMap<>();
   ...
   templates.put("template1", Files.readString(Path.of("src/main/resources/template1.txt")));
   templates.put("template2", Files.readString(Path.of("src/main/resources/template2.txt")));
   ...
```

### 2. Сервер принимает подключение и передает обработчику запросов

```
    @Override
    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(serverTemplateConfig.getPort())) {

            System.out.println("Server is listening on port " + serverTemplateConfig.getPort());
            acceptConnections(serverSocket);

        } catch (IOException e) {
            throw new RuntimeException("Server startup failed", e);
        }
    }

    private void acceptConnections(ServerSocket serverSocket) {
        try {

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                new ClientConnection(clientSocket, requestProcessor).handle();
            }

        } catch (IOException e) {
            System.err.println("Error accepting connection: " + e.getMessage());
        }
    }
```

### 3. Извлечение из запроса (GET /template1?user_name=John&age=30 например)

#### 3.1 Если пустая строка - null

#### 3.2 Разделяем строку на метод, шаблон, параметры

#### 3.3 Проверка, если массив короче 2 эл - возвращаем пустой запрос

#### 3.4 парсятся параметры и складирываются в мапу ключ - параметр, значение - наше значение из строки

```
public final class QueryParserUtils {

    public static Map<String, String> parseQueryParameter(String query) {

        Map<String, String> params = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return params;
        }

        String[] pairs = query.split("&");

        for (String pair : pairs) {

            int idx = pair.indexOf("=");
            if (idx == -1) continue;

            String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);

            params.put(key, value);
        }

        return params;
    }

}
```

### 4. Получение шаблона из хранилища

```
public class HttpRequestHandlerImpl {

    private HttpTemplateResponse processRequest(HttpTemplateRequest request) {

        if (request.getMethod() == null || request.getMethod().isEmpty()) {
            return new HttpTemplateResponse(400, "Bad Request", null);
        }

        String templateContent = templateStorage.getTemplate(request.getPath());

        if (templateContent == null || templateContent.isEmpty()) {
            return new HttpTemplateResponse(404, "Not Found", null);
        }

        try {

            String processedContent = templateProcessor.processTemplate(
                    templateContent,
                    request.getParams()
            );

            return new HttpTemplateResponse(200, "OK", processedContent);

        } catch (RuntimeException e) {
            return new HttpTemplateResponse(500, "Internal Server Error", "Template processing error");
        }
    }
    
}
```

### 5. Замена в шаблоне

#### 5.1 Регуляркой <(\\w+)> ищем все вхождения при помощи Matcher

#### 5.2 Проходимся по всем мэтчам

#### 5.3 Для каждого совпадения извлекаем значения из Map

#### 5.3 StringBuffer собирает результат