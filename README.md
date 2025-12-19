# Lightweight Relational Database Engine (LRDE)

A lightweight, in-memory relational database engine implemented in Java. This project demonstrates core database concepts including query parsing, execution planning, and data storage.

## Features
- **SQL Support**: `CREATE TABLE`, `INSERT INTO`, `SELECT` (with `WHERE` clause).
- **In-Memory Storage**: Fast, non-persistent data storage.
- **Type Safety**: Supports `INT`, `STRING`, and `BOOLEAN` data types.
- **Interfaces**:
    - **Console REPL**: Interactive command-line interface.
    - **GUI**: Simple Swing-based graphical user interface.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Compilation
Navigate to the project root and compile the source code:
```bash
javac -d out src/main/java/com/lrde/*.java src/main/java/com/lrde/command/*.java src/main/java/com/lrde/ui/*.java
```

### Running the Application

#### Console Mode (REPL)
To start the interactive console:
```bash
java -cp out com.lrde.Main
```

#### GUI Mode
To launch the graphical user interface:
```bash
java -cp out com.lrde.Main --gui
```

## Usage Examples

### Create a Table
```sql
CREATE TABLE users (id INT, name STRING, active BOOLEAN)
```

### Insert Data
```sql
INSERT INTO users VALUES (1, "Alice", true)
INSERT INTO users VALUES (2, "Bob", false)
```

### Query Data
Select all records:
```sql
SELECT * FROM users
```

Filter records:
```sql
SELECT * FROM users WHERE id = 1
SELECT * FROM users WHERE active = true
```
