@param String title = "Chat Application - Chat"

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
            background-color: #3273dc;
            color: white;
            margin-left: auto;
        }
        .message-bot {
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>
    <nav class="navbar is-primary" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
            <a class="navbar-item" href="/">
                <strong>Chat Application</strong>
            </a>
        </div>
        <div class="navbar-menu">
            <div class="navbar-start">
                <a class="navbar-item" href="/">Home</a>
                <a class="navbar-item" href="/chat">Chat</a>
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
                                Chat Room
                            </p>
                        </div>
                        <div class="card-content">
                            <div class="chat-container" id="chatContainer">
                                <!-- Messages will be displayed here -->
                            </div>
                            <div class="field">
                                <div class="control">
                                    <textarea class="textarea" id="messageInput" placeholder="Type your message here..."></textarea>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-primary" id="sendButton">
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
                <strong>Chat Application</strong> powered by Spring Boot and jte.
            </p>
        </div>
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const chatContainer = document.getElementById('chatContainer');
            const messageInput = document.getElementById('messageInput');
            const sendButton = document.getElementById('sendButton');

            // Function to add a message to the chat
            function addMessage(message, isUser) {
                const messageElement = document.createElement('div');
                messageElement.classList.add('message-bubble');
                messageElement.classList.add(isUser ? 'message-user' : 'message-bot');
                messageElement.textContent = message;
                chatContainer.appendChild(messageElement);
                chatContainer.scrollTop = chatContainer.scrollHeight;
            }

            // Event listener for send button
            sendButton.addEventListener('click', function() {
                const message = messageInput.value.trim();
                if (message) {
                    addMessage(message, true);
                    messageInput.value = '';

                    // Simulate a response (in a real app, this would be from the server)
                    setTimeout(function() {
                        addMessage('This is a simulated response. In a real application, this would come from the server.', false);
                    }, 1000);
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
            addMessage('Welcome to the chat! Type a message and press Send.', false);
        });
    </script>
</body>
</html>
