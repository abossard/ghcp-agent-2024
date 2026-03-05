---
name: diagramming
description: >
  Architecture diagramming and JPA ERD generation for Spring Boot projects.
  Uses Python's diagrams library and Graphviz to produce PNG documentation.
  USE FOR: architecture diagrams, ERD, entity relationship, visual docs, system diagram.
---

# Diagramming Skill — Spring Boot Edition

Generate professional PNG diagrams from a Spring Boot codebase using Python.

## Prerequisites

```bash
pip install diagrams graphviz matplotlib pillow
brew install graphviz  # macOS — required for PNG rendering
```

## 1. Architecture Diagram Patterns

### Spring Boot 3-Tier (Controller → Service → Repository)

```python
from diagrams import Diagram, Cluster, Edge
from diagrams.onprem.client import Users
from diagrams.onprem.compute import Server
from diagrams.onprem.database import PostgreSQL
from diagrams.programming.framework import Spring

with Diagram("Spring Boot Architecture", show=False, filename="architecture", direction="LR",
             graph_attr={"fontsize": "20", "bgcolor": "white", "pad": "0.5"}):
    users = Users("Clients")

    with Cluster("Spring Boot Application"):
        with Cluster("Controllers"):
            ctrl = Spring("REST API")

        with Cluster("Services"):
            svc = Spring("Business Logic")

        with Cluster("Repositories"):
            repo = Spring("Data Access")

    db = PostgreSQL("Database")

    users >> Edge(label="HTTP") >> ctrl >> svc >> repo >> Edge(label="JPA") >> db
```

### With Additional Infrastructure

```python
from diagrams import Diagram, Cluster, Edge
from diagrams.onprem.client import Users
from diagrams.onprem.database import PostgreSQL
from diagrams.onprem.inmemory import Redis
from diagrams.onprem.monitoring import Grafana, Prometheus
from diagrams.programming.framework import Spring
from diagrams.onprem.network import Nginx

with Diagram("Full Stack Architecture", show=False, filename="architecture", direction="TB",
             graph_attr={"fontsize": "20", "bgcolor": "white", "pad": "0.5"}):
    users = Users("Clients")

    with Cluster("Reverse Proxy"):
        nginx = Nginx("Load Balancer")

    with Cluster("Application Tier"):
        app = Spring("Spring Boot 3.4")

    with Cluster("Data Tier"):
        db = PostgreSQL("PostgreSQL")
        cache = Redis("Redis Cache")

    with Cluster("Observability"):
        prom = Prometheus("Prometheus")
        graf = Grafana("Grafana")

    users >> nginx >> app
    app >> db
    app >> cache
    app >> Edge(style="dotted") >> prom >> graf
```

### Azure Deployment Architecture

```python
from diagrams import Diagram, Cluster, Edge
from diagrams.azure.compute import AppServices, ContainerApps
from diagrams.azure.database import DatabaseForPostgresqlServers, CacheForRedis
from diagrams.azure.network import ApplicationGateway
from diagrams.azure.security import KeyVaults
from diagrams.azure.monitor import ApplicationInsights
from diagrams.azure.identity import ManagedIdentities
from diagrams.onprem.client import Users

with Diagram("Azure Deployment", show=False, filename="architecture", direction="TB",
             graph_attr={"fontsize": "20", "bgcolor": "white", "pad": "0.5"}):
    users = Users("Clients")

    with Cluster("Azure"):
        agw = ApplicationGateway("App Gateway")

        with Cluster("App Service"):
            app = AppServices("Spring Boot")

        with Cluster("Data"):
            pg = DatabaseForPostgresqlServers("PostgreSQL Flex")
            redis = CacheForRedis("Redis")

        kv = KeyVaults("Key Vault")
        ai = ApplicationInsights("App Insights")
        mi = ManagedIdentities("Managed Identity")

    users >> agw >> app
    app >> pg
    app >> redis
    app >> Edge(style="dashed") >> kv
    app >> Edge(style="dotted") >> ai
    mi >> [app, kv]
```

## 2. JPA ERD Generation

### Full ERD with Graphviz (Recommended)

```python
import graphviz

def create_erd(title, filename, entities, relationships):
    """
    Generate ERD from parsed JPA entities.

    entities: list of dicts with 'name' and 'columns' keys
        columns: list of tuples (col_name, data_type, key_type)
        key_type: 'PK', 'FK', or None

    relationships: list of tuples (from_entity, to_entity, label, cardinality)
        cardinality: '1:1', '1:N', 'N:1', 'N:M'
    """
    dot = graphviz.Digraph(title, filename=filename, format='png')
    dot.attr(rankdir='LR', splines='spline', nodesep='0.8', ranksep='1.2')
    dot.attr('node', shape='none', margin='0')

    for entity in entities:
        name = entity['name']
        rows = ""
        for col_name, data_type, key_type in entity['columns']:
            icon = ""
            if key_type == 'PK':
                icon = "🔑 "
            elif key_type == 'FK':
                icon = "🔗 "
            rows += f'<TR><TD ALIGN="LEFT">{icon}{col_name}</TD><TD ALIGN="LEFT"><FONT COLOR="gray">{data_type}</FONT></TD></TR>'

        html = f'''<<TABLE BORDER="1" CELLBORDER="0" CELLSPACING="0" CELLPADDING="4">
        <TR><TD BGCOLOR="#4472C4" COLSPAN="2"><FONT COLOR="white"><B>{name}</B></FONT></TD></TR>
        {rows}
        </TABLE>>'''
        dot.node(name, html)

    for from_e, to_e, label, card in relationships:
        arrowhead = 'crow' if card.endswith('N') or card.endswith('M') else 'tee'
        arrowtail = 'crow' if card.startswith('N') or card.startswith('M') else 'tee'
        dot.edge(from_e, to_e, label=label, arrowhead=arrowhead, arrowtail=arrowtail)

    dot.render(cleanup=True)
    return f"{filename}.png"
```

### JPA Annotation Mapping

When scanning Java `@Entity` classes, map these annotations to ERD elements:

| JPA Annotation            | ERD Element                     |
| ------------------------- | ------------------------------- |
| `@Id`                     | Column with PK icon 🔑          |
| `@GeneratedValue`         | Auto-increment note             |
| `@Column(nullable=false)` | NOT NULL                        |
| `@Column(length=N)`       | VARCHAR(N)                      |
| `@ManyToOne`              | FK 🔗 + N:1 relationship arrow  |
| `@OneToMany`              | 1:N relationship arrow          |
| `@ManyToMany`             | N:M relationship (via junction) |
| `@OneToOne`               | 1:1 relationship arrow          |
| `String` field            | VARCHAR                         |
| `Long` / `Integer`        | BIGINT / INT                    |
| `LocalDateTime`           | TIMESTAMP                       |
| `BigDecimal`              | DECIMAL                         |
| `Boolean`                 | BOOLEAN                         |
| `@Enumerated`             | ENUM                            |

### Example: Scanning the Greeting Entity

Given `model/Greeting.java`:

```java
@Entity
public class Greeting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String message;
}
```

Produces:

```python
entities = [{
    'name': 'Greeting',
    'columns': [
        ('id', 'BIGINT', 'PK'),
        ('name', 'VARCHAR (NOT NULL)', None),
        ('message', 'VARCHAR', None),
    ]
}]
relationships = []  # No relationships for this entity
create_erd("Database Schema", "erd", entities, relationships)
```

## 3. Connection & Edge Styling

```python
# Data flow
a >> Edge(label="HTTPS") >> b

# Async / messaging
a >> Edge(label="async", style="dashed") >> b

# Monitoring / observability
a >> Edge(style="dotted", color="gray") >> b

# One to many
a >> [b, c, d]

# Many to one
[a, b, c] >> d
```

## 4. Common Imports Quick Reference

```python
# Spring Boot / Java ecosystem
from diagrams.programming.framework import Spring
from diagrams.programming.language import Java

# On-prem / local dev
from diagrams.onprem.database import PostgreSQL, MySQL
from diagrams.onprem.inmemory import Redis, Memcached
from diagrams.onprem.queue import Kafka, RabbitMQ
from diagrams.onprem.client import Users, Client
from diagrams.onprem.network import Nginx
from diagrams.onprem.monitoring import Grafana, Prometheus
from diagrams.onprem.ci import GithubActions, Jenkins

# Azure services
from diagrams.azure.compute import AppServices, ContainerApps, FunctionApps, AKS
from diagrams.azure.database import DatabaseForPostgresqlServers, CosmosDb, CacheForRedis, SQLDatabases
from diagrams.azure.network import ApplicationGateway, LoadBalancers
from diagrams.azure.security import KeyVaults
from diagrams.azure.monitor import ApplicationInsights, LogAnalyticsWorkspaces
from diagrams.azure.identity import ManagedIdentities, ActiveDirectory
from diagrams.azure.storage import BlobStorage

# Generic (for custom components)
from diagrams.generic.compute import Rack
from diagrams.generic.database import SQL
from diagrams.custom import Custom  # for custom icons
```

## 5. File Output Convention

All diagram files go to `docs/diagrams/`:

| File                             | Purpose                     |
| -------------------------------- | --------------------------- |
| `docs/diagrams/architecture.py`  | Architecture diagram source |
| `docs/diagrams/architecture.png` | Rendered architecture       |
| `docs/diagrams/erd.py`           | ERD source                  |
| `docs/diagrams/erd.png`          | Rendered ERD                |

Always set `show=False` in `Diagram()` so headless execution works.
