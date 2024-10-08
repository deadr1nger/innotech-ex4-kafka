# Используем официальный образ OpenJDK 21 как базовый
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /kafka-listener

# Копируем файл jar приложения в рабочую директорию
COPY target/kafka-listener-1.0.jar /kafka-listener/kafka-listener-1.0.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "kafka-listener-1.0.jar"]

# Указываем, что приложение будет слушать на порту 8080
EXPOSE 8080