@param String title = "Bot Assistant - Chat"

<!DOCTYPE html>
<!--
  #%L
  hero-guide-demo-app
  %%
  Copyright (C) 2025 BellSoft
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  -->

<html lang="en">
@template.fragments.header(
title = title,
extraStyle = @`@template.fragments.assistant-styles()`
)
<body>
@template.fragments.navbar()

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
                                <textarea class="textarea" id="messageInput"
                                          placeholder="Ask me anything..."></textarea>
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

@template.fragments.footer()

<script>
    document.addEventListener('DOMContentLoaded', function () {
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

                const response = await fetch('/api/bot/process', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        content: message,
                        sessionId: 'web-session',
                        type: 'CHAT',
                        senderId: 'user',
                        senderName: 'User'
                    })
                });

                if (!response.ok) {
                    throw new Error('Failed to get response from bot');
                }

                const data = await response.json();
                hideTypingIndicator();
                addMessage(data.content, false);
            } catch (error) {
                console.error('Error:', error);
                hideTypingIndicator();
                addMessage('Sorry, I encountered an error. Please try again later.', false);
            }
        }

        // Event listener for send button
        sendButton.addEventListener('click', function () {
            const message = messageInput.value.trim();
            if (message) {
                addMessage(message, true);
                messageInput.value = '';
                sendMessageToBot(message);
            }
        });

        // Event listener for Enter key
        messageInput.addEventListener('keypress', function (e) {
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
