# Fabric Latency Viewer

A client-side Fabric mod that displays ping numbers in the player list (Tab menu) instead of signal bars.

## Features

- Shows actual ping values (ms) instead of signal bars
- Color-coded based on latency:
  - Green: < 50ms
  - Yellow-Green: 50-100ms
  - Yellow: 100-150ms
  - Orange: 150-300ms
  - Red: > 300ms

## Supported Versions

| Minecraft | Java |
|-----------|------|
| 1.20.1    | 17+  |
| 1.20.2    | 17+  |
| 1.20.4    | 17+  |
| 1.20.6    | 21+  |
| 1.21      | 21+  |
| 1.21.1    | 21+  |
| 1.21.2    | 21+  |
| 1.21.3    | 21+  |
| 1.21.4    | 21+  |
| 1.21.5    | 21+  |
| 1.21.6    | 21+  |
| 1.21.7    | 21+  |
| 1.21.8    | 21+  |
| 1.21.9    | 21+  |
| 1.21.10   | 21+  |
| 1.21.11   | 21+  |

## Requirements

- Java 21 (for building)
- Fabric Loader 0.15.0+

## Build

```bash
# Build all versions
./gradlew buildAll

# Build specific version
./gradlew :1.20.1:build
./gradlew :1.21.4:build
./gradlew :1.21.11:build

# Clean build files
./gradlew clean
```

## Output

Built JAR files are located in each version's build directory:

```
versions/<mc-version>/build/libs/fabric-latency-viewer-v1.0.0-mc<mc-version>.jar
```

Example:
```
versions/1.21.11/build/libs/fabric-latency-viewer-v1.0.0-mc1.21.11.jar
```

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download the JAR file for your Minecraft version
3. Place the JAR in your `.minecraft/mods` folder
4. Launch the game

## License

MIT
