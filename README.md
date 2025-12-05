
---

# **Beauty Match – Agendamentos para Salões de Beleza**

O **Beauty Match** é uma plataforma completa desenvolvida para otimizar a gestão de salões de beleza, solucionando conflitos de agendamento e centralizando a comunicação entre **proprietários**, **colaboradores** e **clientes** em um único sistema.

Este projeto foi criado como solução real para uma empresa do ramo, com foco em organização, eficiência e experiência do usuário.

---

## 🚀 **Principais Funcionalidades**

 ✔️ **Agendamento de serviços** de forma simples e intuitiva
 ✔️ **Cadastro de usuários**, funcionários, clientes e proprietários
 ✔️ **Gestão de serviços**, horários e disponibilidade
 ✔️ **Processamento e validação de dados**
 ✔️ **Controle seguro de login e autorização (Security)**
 ✔️ **Upload e gerenciamento de imagens**
 ✔️ **Painel administrativo** para proprietários
 ✔️ **Interface organizada**, estética e funcional

---

## 🛠️ **Tecnologias Utilizadas**

### **Backend**

* Java 17+
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* Maven

### **Banco de Dados**

* MySQL

### **Outros**

* JWT para autenticação
* Padrão DTO e services bem estruturados
* Configurações externas via `.properties`/`.yml`

---

## 📁 Estrutura do Projeto

```
Beauty-Match-main/
 ├── docs/               # Documentação completa do sistema
 ├── src/
 │   ├── main/java/...   # Código-fonte organizado por camadas
 │   ├── resources/      # Configurações e assets
 ├── uploads/            # Armazenamento de imagens (dev)
 ├── pom.xml             # Dependências Maven
 ├── .mvn/               # Configurações Maven wrapper
```

---

## 📄 **Documentação do Sistema**

Na pasta `/docs`, você encontrará:

* **documentacao-config.md** – Configurações gerais
* **documentacao-controller.md** – Mapeamentos de APIs
* **documentacao-service.md** – Regras de negócio
* **documentacao-dto.md** – Estrutura de dados compartilhados
* **documentacao-entity.md** – Modelo de entidades
* **documentacao-exception.md** – Tratamento de exceções
* **documentacao-security.md** – Autenticação, filtros e tokens
* **processo-agendamento.md** – Fluxo completo do agendamento
* **resumo-geral-sistema.md** – Visão geral do projeto
* **diagramas/** – Diagramas UML e fluxos do sistema

---

## ⚙️ **Como Rodar o Projeto Localmente**

### **Pré-requisitos**

* Java 17+
* Maven 3.8+
* MySQL instalado

---

### **Instalação**

# ✅ **1. Instalar o Java (JDK 17 ou superior)**

### **Windows**

1. Acesse: [https://adoptium.net](https://adoptium.net)
2. Baixe o **Temurin 17 (JDK)**.
3. Instale avançando todas as etapas.
4. Verifique a instalação:

   ```bash
   java -version
   ```

### **Linux (Ubuntu/Debian)**

```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

---

# ✅ **2. Instalar o Maven**

### **Windows**

1. Baixe o Maven: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Baixe o arquivo **Binary zip archive**.
3. Extraia em `C:\Program Files\Apache\maven`.
4. Adicione ao PATH:

   * Painel de Controle → Sistema → Avançado → Variáveis de Ambiente
   * Em "Path", adicione:

     ```
     C:\Program Files\Apache\maven\bin
     ```
5. Verifique a instalação:

   ```bash
   mvn -version
   ```

### **Linux**

```bash
sudo apt update
sudo apt install maven
mvn -version
```

---

# ✅ **3. Instalar o MySQL**

### **Windows**

1. Baixe o instalador oficial: [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)
2. Instale usando a opção **Developer Default**.
3. Defina uma senha para o usuário **root**.
4. Instale o MySQL Workbench (opcional, mas recomendado).

### **Linux (Ubuntu/Debian)**

```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

---

# 🎯 **Verificar se tudo está funcionando**

### Java:

```bash
java -version
```

### Maven:

```bash
mvn -version
```

### MySQL:

```bash
mysql -u root -p
```

---

### **Passo a passo**

```bash
# Clonar o repositório
git clone <seu-repositorio>

# Entrar na pasta
cd Beauty-Match-main

# Instalar dependências e rodar
mvn spring-boot:run
```

### **Configuração do banco**

No arquivo `application.properties` ou `application.yml`, configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/beautymatch
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔐 **Autenticação**

O login utiliza **JWT**.
O fluxo de segurança inclui:

* Filtro de autenticação
* Filtro de autorização
* Proteção de endpoints
* Perfis de usuários com permissões diferentes

---

## 🧩 **Endpoints Principais**

O sistema possui endpoints para:

* Usuários
* Funcionários
* Agendamentos
* Serviços
* Uploads
* Autenticação

(Consulte `/docs/documentacao-controller.md` para a lista completa.)

---

## 📌 **Objetivo do Projeto**

Criar um sistema real capaz de:

* Centralizar informações de agendamentos
* Evitar sobreposições de horários
* Facilitar a comunicação entre profissionais e clientes
* Aumentar a eficiência operacional do salão

---
