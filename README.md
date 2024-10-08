# innotech-ex4-kafka
Проект включает в себя два сервиса: Producer-service и Consumer-service  
Продюсер передаёт метрики в топик KAFKA в формате JSON  
Консьюмер получает эти метрики из KAFKA, сохраняет их базу и предоставляет REST-API для анализа метрик   
Пример сообщения KAFKA:  
```
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "systemName": "System A",
  "timeStamp": "2024-10-08T23:30:00",
  "metrics": {
    "CPU": 75.5,
    "Memory": 65.3,
    "Disk": 40.2
  }
}
```
Чтобы запустить проект необходимо package обоих сервисов с профилем docker  
Далее создаём докер-образы наших сервисов с помощью команд:   
`docker build -t listener-service:latest .` / `docker build -t producer-service:latest .`  
После этого можем запускать docker-compose командой: `docker-compose up -d`  
Необходимые настройки для контейнеров можно заполнить в файле `.env`

# Consumer-service

## Эндпоинты  
`GET /metrics` Получение списка всех метрик.  
`GET /metrics/{id}` Получение конкретной метрики по ее идентификатору.  
`GET /metrics/by-filter` Получение конкретной метрики по определённым идентификаторам. 
   
Пример отправляемого GET-запроса для фильтра в формате JSON
```
[
    {
        "key": "systemName",
        "operator": "eq",
        "value": "kafka-producer-service"
    },
    {
        "key": "id",
        "operator": "uuid",
        "value": "8553d273-d396-46fe-a2da-e10160443e25"
    }
    
]
```
# Producer-service
Не имеет REST-эндпоинтов, метрики собираются с помощью Actuator и при помощи Scheduler отправляются в KAFKA с определённым интервалом  
Интервал отправления метрик меняется настройкой (Указывать в миллисекундах): 
```
scheduler:
  fixed-rate:
```  

