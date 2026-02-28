### 3.4 Serviço de Temperature Monitoring

**Descrição:** Responsável pela análise dos dados e geração de relatórios, este serviço faz parte da capacidade de negócio de **Monitoramento de Temperaturas**.

- **Funcionalidades Principais:**
    - Armazenamento dos dados dos sensores
    - Permitir consulta ao histórico de temperatura
    - Configuração e disparo de alertas de temperatura

**Endpoints:**

- `GET /api/sensors/{sensorId}/temperatures`: Lista de registro de temperaturas
- `PUT /api/sensors/{sensorId}/alert`: Atualiza configuração de alerta de temperatura
- `GET /api/sensors/{sensorId}/alert`: Consulta configuração de alerta de temperatura
- `DELETE /api/sensors/{sensorId}/alert`: Deleta configuração de alerta de temperatura
- `GET /api/sensors/{sensorId}/monitoring`: Detalhes do monitoramento
- `PUT /api/sensors/{sensorId}/monitoring/enable`: Ativa monitoramento
- `DELETE /api/sensors/{sensorId}/monitorig/enable`: Inativa monitoramento

**Tecnologias Sugeridas:**

- Linguagem de Programação: Java
- Banco de Dados: Postgres

**Objetos de negócio:**

- Temperature Log
    - Propriedades
        - Id: UUID
        - SensorId: TSID
        - Registred At: OffsetDateTime
        - Value: Double
- Sensor Monitoring
    - Id: TSID
    - Last Temperature: Double
    - Updated At:  OffsetDateTime
    - Enabled: Boolean
- Sensor Alert
    - Id: TSID
    - MaxTemperature: Double
    - MinTemperature: Double