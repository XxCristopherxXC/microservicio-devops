[README.md](https://github.com/user-attachments/files/29493779/README.md)
# Evidencia Gráfica de Observabilidad y Entornos Reales en DevOps

Evidencia gráfica del proyecto **microservicio-devops**, que documenta la configuración de infraestructura, observabilidad, CI/CD y políticas de cumplimiento implementadas sobre AWS ECS/Fargate, GitHub Actions y SonarCloud.

---

## Contexto general: configuración del clúster

Configuré un clúster balanceado en Amazon ECS bajo la modalidad Serverless con AWS Fargate para aislar lógicamente los recursos de nuestro microservicio, delegando la administración de la infraestructura a AWS y garantizando alta disponibilidad, seguridad en la red interna y escalabilidad automática sin sobrecarga operativa.

![Vista general del clúster ECS](images/01-ecs-cluster-overview.png)

---

## Credenciales y secretos del pipeline

Configuración de los secretos del repositorio en GitHub (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_SESSION_TOKEN` y `SONAR_TOKEN`), necesarios para que GitHub Actions despliegue en AWS y ejecute el análisis de SonarCloud.

![Secretos del repositorio en GitHub](images/02-github-secrets.png)

---

## Planificación de tareas del equipo

Aquí estoy definiendo las tareas con Jason para nuestro proyecto.

![Definición de tareas en Amazon ECS](images/03-ecs-task-definition.png)

---

## IE1 — Herramientas de monitoreo (CloudWatch)

**Amazon CloudWatch** se utilizó en el proyecto para implementar la observabilidad, centralizando los logs de la consola de Spring Boot mediante el grupo de registros `/ecs/tarea-devops` y recopilando las métricas nativas de infraestructura (como `CPUUtilization` y `MemoryUtilization`). Su rol clave para cumplir con el indicador IE1 de la rúbrica es permitir el monitoreo continuo, el rastreo de excepciones en tiempo real y la supervisión del rendimiento de las tareas serverless en AWS Fargate.

![Grupo de registros de CloudWatch](images/04-cloudwatch-loggroup.png)

Sobre monitoreo e infraestructura, habilitamos el panel de Estado nativo de Amazon ECS en el clúster `devops-cluster`. Este integra gráficos de Utilización de CPU y Utilización de Memoria para medir continuamente el procesamiento y consumo de RAM de las tareas serverless en AWS Fargate, previniendo fallas y saturación. El mensaje "No hay datos disponibles" es normal en esta etapa, ya que las tareas de la última revisión (`tarea-devops:2`) están inicializando su red perimetral en la VPC sin recibir tráfico constante aún; no obstante, la arquitectura de observabilidad con CloudWatch ya está 100% acoplada y lista para desplegar telemetría en tiempo real apenas el servicio procese peticiones.

![Métricas de CPU y memoria en ECS](images/05-ecs-cpu-memory-status.png)

---

## IE2 — Despliegue en entorno orquestado (ECS / Fargate)

El microservicio fue desplegado eficazmente dentro de la plataforma cloud de Amazon Web Services (AWS), utilizando el orquestador nativo Amazon ECS bajo la modalidad serverless de AWS Fargate. El despliegue se consolidó en el clúster denominado `devops-cluster`, administrado automáticamente mediante un pipeline de CI/CD en GitHub Actions.

![Estado del servicio en Amazon ECS](images/06-ecs-service-status.png)

**El repositorio Amazon ECR (Elastic Container Registry)** se utilizó en la arquitectura como el almacenamiento centralizado y seguro de las imágenes Docker del microservicio. Su rol en el pipeline de CI/CD es clave para cumplir con el indicador IE2 de la rúbrica, ya que funciona como el puente automatizado donde GitHub Actions empaqueta y sube (push) la imagen construida con el nombre `microservicio-devops`, permitiendo que posteriormente el orquestador Amazon ECS la descargue (pull) de forma directa para desplegar y actualizar las tareas en AWS Fargate.

![Repositorio privado en Amazon ECR](images/07-ecr-repository.png)

---

## IE3 — Dashboard con métricas clave

El dashboard de calidad está completamente integrado en el flujo CI/CD. Actualmente la métrica de Cobertura se encuentra en estado base debido a que el microservicio está en fase inicial de despliegue de endpoints, teniendo planificada la incorporación de pruebas unitarias automatizadas con JUnit y Mockito en el siguiente sprint para activar la recolección de porcentaje de cobertura dentro del panel.

![Dashboard de calidad en SonarQube](images/08-sonarqube-dashboard.png)

---

## IE5 — Políticas de cumplimiento (Branch Protection / SonarQube)

Importación y configuración del proyecto `microservicio-devops` en SonarCloud para habilitar el análisis estático de código como parte de la política de calidad.

![Importación del proyecto en SonarCloud](images/09-sonarcloud-import.png)

En la imagen se observa que la rama principal `main` tiene activados los iconos de protección. Esto significa que configuramos reglas de Branch Protection:

![Reglas de Branch Protection en GitHub](images/10-github-branch-protection.png)

Se implementaron reglas de protección sobre la rama `main` en GitHub para asegurar que ningún cambio de código se fusione de forma directa o sin control. Esto actúa como una política de cumplimiento normativo automatizada (IE5), obligando a que todo código pase primero por la validación de estado del pipeline (Check status en verde) y por el análisis estático de SonarCloud antes de habilitar su empaquetado final y posterior despliegue en la infraestructura cloud.

---

## IE6 — Validación que interrumpe el pipeline ante fallas críticas

Esta captura de GitHub Actions demuestra visualmente cómo funciona el pipeline automático (CI/CD) y sirve para respaldar con fuerza el indicador IE6 (interrupción ante fallas) y el IE3 (tiempos de despliegue).

![Pull request bloqueado por validaciones](images/11-pr-merge-blocked.png)

Esta evidencia de GitHub Actions demuestra el funcionamiento real y automatizado del pipeline CI/CD bajo el flujo AWS DevOps Pipeline - Spring Boot. La captura refleja de forma empírica la aplicación del indicador IE6, donde un error de configuración en el análisis estático interrumpió automáticamente el flujo (Commit #6 en rojo), bloqueando el empaquetado para proteger el entorno; posterior a las correcciones técnicas, los siguientes flujos se ejecutaron de manera exitosa en la rama `main` (Commits #7 y #8 en verde), registrando tiempos de ejecución e integración óptimos de entre 1 y 2 minutos (cumpliendo con el IE3).

![Historial de ejecuciones de GitHub Actions](images/12-github-actions-workflows.png)

![Historial de ejecuciones de GitHub Actions (continuación)](images/13-github-actions-workflows-cont.png)

---

## Resumen de indicadores cubiertos

| Indicador | Descripción | Evidencia |
|---|---|---|
| IE1 | Herramientas de monitoreo (CloudWatch) | Logs y métricas de CPU/memoria |
| IE2 | Despliegue en entorno orquestado (ECS/Fargate) | Servicio ECS y repositorio ECR |
| IE3 | Dashboard con métricas clave | SonarQube y tiempos de pipeline |
| IE5 | Políticas de cumplimiento | Branch Protection y SonarCloud |
| IE6 | Validación ante fallas críticas | Bloqueo y recuperación del pipeline en GitHub Actions |
