# Используем официальный образ OpenJDK 21 как базовый
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /kafka-produce

# Копируем файл jar приложения в рабочую директорию
COPY target/kafka-produce-1.0.jar /kafka-produce/kafka-produce-1.0.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "kafka-produce-1.0.jar"]

# Указываем, что приложение будет слушать на порту 8080
EXPOSE 8080