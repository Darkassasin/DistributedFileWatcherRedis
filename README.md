# 📂 Distributed File Watcher Service

A scalable, fault-tolerant file watcher service built with **Spring Boot** that supports **horizontal scalability** using **Redis** for coordination between multiple instances. It ensures that the same file is not processed multiple times across instances in a distributed environment.

---

## 🚀 Key Features

- 📡 **Distributed File Watching**  
  Watches directories in real time and detects newly placed files across multiple service instances.

- 🔁 **Horizontal Scalability**  
  Seamless scaling of the service with Docker, Kubernetes, or cloud-native platforms.

- 🧠 **Smart File Deduplication**  
  Prevents duplicate processing using **content-based hashing** (MD5/SHA) stored in **Redis**.

- 🧰 **Spring Boot & Spring Integration**  
  Leverages Spring frameworks for a production-ready and easily configurable architecture.


## 🏗️ Architecture Overview

```text
┌────────────┐      ┌────────────┐      ┌────────────┐
│ Instance 1 │<────>│   Redis    │<────>│ Instance 2 │
└────┬───────┘      └────────────┘      └────┬───────┘
     │                                      │
     ▼                                      ▼
 [ Watches File System ]          [ Watches File System ]
     │                                      │
     ▼                                      ▼
 [ Compute File Hash based on content]           [ Compute File Hash based on content]
     │                                      │
     ▼                                      ▼
[ Check Redis for Duplicates ]        [ Check Redis for Duplicates ]
     │                                      │
     ▼                                      ▼
[ Process if New ]               [ Skip if Duplicate ]

🧑‍💻 Tech Stack

Java 17
Spring Boot
Spring Integration / DevTools
Redis (as central coordination store)
Docker & Docker Compose
🛠️ Setup & Run

Prerequisites
Java 17+
Redis running locally or via Docker
Maven

🧪 How It Works

Each instance watches a folder for new files.
On file creation:
It calculates the hash of the file based on Content 
Checks Redis to see if this hash has already been processed.
If not, it processes the file and stores the hash in Redis.
Otherwise, it skips the file to avoid duplication.
This mechanism allows safe, distributed file processing even with multiple instances running.

📈 Scalability

This service is built with horizontal scalability in mind.Redis acts as a central coordination point to keep state consistent across replicas.

🧹 Future Enhancements

 - File processing retries and dead-letter handling
 - Hash expiration policies in Redis
 - File archiving/post-processing hooks 
 - Easily deployable in containers and orchestrated environments.
