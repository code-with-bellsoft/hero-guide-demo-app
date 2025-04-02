@param String title = "Bot Assistant - Chat"

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="/webjars/bulma/1.0.3/css/bulma.min.css">
    <script defer src="/webjars/fontawesome/5.6.3/index.js"></script>
    <style>
        .chat-container {
            height: 60vh;
            overflow-y: auto;
            padding: 1rem;
            border: 1px solid #dbdbdb;
            border-radius: 4px;
            margin-bottom: 1rem;
        }
        .message-bubble {
            padding: 0.75rem;
            border-radius: 1rem;
            margin-bottom: 0.5rem;
            max-width: 80%;
        }
        .message-user {
            background-color: #209cee;
            color: white;
            margin-left: auto;
        }
        .message-bot {
            background-color: #f5f5f5;
        }
        .typing-indicator {
            display: none;
            padding: 0.5rem;
            font-style: italic;
            color: #888;
        }
    </style>
</head>
<body>
    <nav class="navbar is-info" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
            <a class="navbar-item" href="/">
                <strong>Bot Assistant</strong>
            </a>
        </div>
        <div class="navbar-menu">
            <div class="navbar-start">
                <a class="navbar-item" href="/">Home</a>
                <a class="navbar-item" href="/assistant">Assistant</a>
            </div>
        </div>
    </nav>

    <section class="section">
        <div class="container">
            <div class="columns">
                <div class="column is-8 is-offset-2">
                    <div class="card">
                        <div class="card-header">
                            <p class="card-header-title">
                                AI Assistant
                            </p>
                        </div>
                        <div class="card-content">
                            <div class="chat-container" id="chatContainer">
                                <!-- Messages will be displayed here -->
                            </div>
                            <div class="typing-indicator" id="typingIndicator">
                                Bot is typing...
                            </div>
                            <div class="field">
                                <div class="control">
                                    <textarea class="textarea" id="messageInput" placeholder="Ask me anything..."></textarea>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-info" id="sendButton">
                                        <span class="icon">
                                            <i class="fas fa-paper-plane"></i>
                                        </span>
                                        <span>Send</span>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="content has-text-centered">
            <p>
                <strong>Bot Assistant</strong> powered by Spring Boot, OpenAI, and jte.
            </p>
        </div>
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const chatContainer = document.getElementById('chatContainer');
            const messageInput = document.getElementById('messageInput');
            const sendButton = document.getElementById('sendButton');
            const typingIndicator = document.getElementById('typingIndicator');

            // Function to add a message to the chat
            function addMessage(message, isUser) {
                const messageElement = document.createElement('div');
                messageElement.classList.add('message-bubble');
                messageElement.classList.add(isUser ? 'message-user' : 'message-bot');
                messageElement.textContent = message;
                chatContainer.appendChild(messageElement);
                chatContainer.scrollTop = chatContainer.scrollHeight;
            }

            // Function to show typing indicator
            function showTypingIndicator() {
                typingIndicator.style.display = 'block';
                chatContainer.scrollTop = chatContainer.scrollHeight;
            }

            // Function to hide typing indicator
            function hideTypingIndicator() {
                typingIndicator.style.display = 'none';
            }

            // Function to send message to the bot
            async function sendMessageToBot(message) {
                try {
                    showTypingIndicator();

                    const response = await fetch('/api/bot/chat', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            message: message,
                            sessionId: 'web-session'
                        })
                    });

                    if (!response.ok) {
                        throw new Error('Failed to get response from bot');
                    }

                    const data = await response.json();
                    hideTypingIndicator();
                    addMessage(data.response, false);
                } catch (error) {
                    console.error('Error:', error);
                    hideTypingIndicator();
                    addMessage('Sorry, I encountered an error. Please try again later.', false);
                }
            }

            // Event listener for send button
            sendButton.addEventListener('click', function() {
                const message = messageInput.value.trim();
                if (message) {
                    addMessage(message, true);
                    messageInput.value = '';
                    sendMessageToBot(message);
                }
            });

            // Event listener for Enter key
            messageInput.addEventListener('keypress', function(e) {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    sendButton.click();
                }
            });

            // Add a welcome message
            addMessage('Hello! I\'m your AI assistant. How can I help you today?', false);
        });
    </script>
</body>
</html>
