# 💬 WsChat - Chat Grupal en Java

<div align="center">

![Chat Animation](https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/tip.svg)

</div>

## 🌟 Características

- 🖼️ Interfaz gráfica moderna y amigable
- 👥 Soporte para múltiples usuarios simultáneos
- 📝 Mensajería en tiempo real
- 🔔 Notificaciones de conexión/desconexión
- ⌨️ Envío de mensajes con Enter o botón
- 🎨 Diseño responsive y agradable a la vista
- 🗑️ Botón para limpiar el historial del chat
- 👤 Distinción clara de mensajes propios
- 🎯 Auto-scroll a nuevos mensajes
- 🎨 Tipografía clara en color negro

## 🚀 Cómo ejecutar

### Requisitos previos
- ☕ Java JDK 8 o superior
- 🔧 Un IDE Java (recomendado: Eclipse o IntelliJ IDEA)

### Pasos para ejecutar

1. 📂 Inicia el servidor:
   ```bash
   cd src/Servidor
   javac SocketServidor.java
   java SocketServidor
   ```

2. 🖥️ Inicia el cliente:
   ```bash
   cd src/Cliente
   javac Cliente.java
   java Cliente
   ```

## 🏗️ Estructura del proyecto

```
WsChat/
├── 📂 src/
│   ├── 📂 Cliente/
│   │   ├── 📄 Cliente.java      # Interfaz gráfica y lógica del cliente
│   │   └── 📄 HiloCliente.java  # Manejo de conexiones de cliente
│   └── 📂 Servidor/
│       └── 📄 SocketServidor.java # Servidor principal
└── 📄 README.md
```

## 🔧 Componentes principales

### 🖥️ Cliente
- Interfaz gráfica moderna con Swing y JTextPane
- Sistema de eventos para mensajería
- Manejo de conexión/desconexión elegante
- Soporte para emojis 😊
- Control de formato y estilo de mensajes
- Botón de limpieza de chat
- Distinción visual de mensajes propios

### ⚙️ Servidor
- Gestión de múltiples conexiones
- Sistema de broadcast de mensajes
- Control de nombres de usuarios
- Manejo robusto de errores

## 🎨 Interfaz de Usuario

- 📝 Campo de texto para escribir mensajes
- 📤 Botón "Enviar" con ícono
- 🗑️ Botón "Limpiar chat" en la parte superior
- 📜 Área de mensajes con scroll automático
- 🎯 Formato consistente con fuente Arial 14px
- 👤 Prefijo "Tú:" para mensajes propios
- 🖼️ Diseño limpio y moderno

## 🤝 Protocolo de comunicación

Los mensajes siguen el siguiente formato:
- 📝 Mensaje normal: `texto`
- 👤 Registro de nombre: `NOMBRE:usuario`
- 🚪 Desconexión: `FIN`

## 💡 Tips de uso

- 🎯 Presiona Enter o el botón para enviar mensajes
- 🔄 Los mensajes se actualizan en tiempo real
- 🚪 Cierra la ventana para desconectarte
- 👥 Verás notificaciones cuando otros usuarios se unan o salgan
- 🗑️ Usa el botón de limpiar para resetear la vista del chat
- 📜 El chat hace scroll automático a nuevos mensajes

## ⚠️ Consideraciones

- 🔒 Asegúrate de que el puerto 2018 esté disponible
- 🌐 Configura la IP del servidor si es necesario
- 🔍 Revisa la consola del servidor para monitoreo

<div align="center">

### 🎉 ¡Disfruta chateando! 🎊

</div>
