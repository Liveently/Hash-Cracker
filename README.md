# Hash-Cracker 

## Описание  
**HashCracker** — распределенная система для подбора паролей по MD5-хэшу.  
Состоит из менеджера (распределяет задачи), воркеров (выполняют перебор) и веб-интерфейса.  


## Сервисы  
- **Менеджер**  распределяет задачи, отправляет результаты  
- **Воркеры**  выполняют перебор  
- **UI**  интерфейс для отправки и отслеживания задач  

## Запуск  
```sh
docker-compose up --build
```

## Доступ
Перейдите в браузере: [`http://localhost:8081`](http://localhost:8081).  

## Переменные окружения
Можно задать в `.env` или `docker-compose.yml`:  
```env
MANAGER_PORT=8080  # Порт менеджера  
WORKER_PORT=8081   # Порт воркеров  
TASK_TIMEOUT=PT1M  # Таймаут задачи
HASH_PARTS: "4"           # Количество частей, на которые делится хеш
LETTERS_AND_DIGITS: "abcdefghijklmnopqrstuvwxyz0123456789"  # Алфавит для перебора
PROGRESS_UPDATE_INTERVAL: "10000"  #Каждые каждые x итераций обновляется прогресс
```

## Масштабируемость
Можно запускать несколько воркеров для ускорения работы
Задаётся в `docker-compose.yml`: 
```
worker:
    deploy:
      replicas: 3
```
