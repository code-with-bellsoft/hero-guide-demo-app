@param String title = "Chat Application - Chat"
@param Boolean authenticated = false
@param String username = ""
@param String sessionId = "default"
@param Boolean isAdmin = false

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
extraStyle = @`
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
        word-wrap: break-word;
    }

    .message-user {
        background-color: #3273dc;
        color: white;
        margin-left: auto;
    }

    .message-other {
        background-color: #f5f5f5;
        margin-right: auto;
    }

    .message-bot {
        background-color: #23d160;
        color: white;
        margin-right: auto;
    }

    .message-join, .message-leave {
        background-color: #f5f5f5;
        color: #4a4a4a;
        text-align: center;
        font-style: italic;
        margin: 0 auto;
    }

    .message-info {
        font-size: 0.8rem;
        color: #7a7a7a;
        margin-bottom: 0.2rem;
    }

    .connecting {
        text-align: center;
        padding: 1rem;
        font-style: italic;
        color: #7a7a7a;
    }
</style>`,
extraScript = @`
<script src="/webjars/sockjs-client/1.5.1/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.4/stomp.min.js"></script>
`
)

<body>
@template.fragments.navbar(
    authenticated = authenticated,
    username = username,
    isAdmin = isAdmin
)
<section class="section">
    <div class="container">
        <div class="columns">
            <div class="column is-8 is-offset-2">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            Chat Room
                        </p>
                        <div class="card-header-icon">
                            <div class="field">
                                <input class="is-checkradio" id="botToggle" type="checkbox" checked>
                                <label for="botToggle">Bot Assistant</label>
                            </div>
                        </div>
                    </div>
                    <div class="card-content">
                        @if(authenticated)
                            <div class="field">
                                <div class="control">
                                    <input class="input" type="text" id="username" value="${username}" readonly>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-info" id="connectButton">Connect</button>
                                </div>
                            </div>
                        @else
                            <div class="field">
                                <div class="control">
                                    <input class="input" type="text" id="username" placeholder="Enter your username">
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-info" id="connectButton">Connect</button>
                                </div>
                            </div>
                            <div class="notification is-warning">
                                <p>You are not logged in. <a href="/login">Login</a> to use your account.</p>
                            </div>
                        @endif
                        <div class="chat-container" id="chatContainer">
                            <div class="connecting">Please enter your username and connect to start chatting.</div>
                        </div>
                        <div class="field">
                            <div class="control">
                                <textarea class="textarea" id="messageInput" placeholder="Type your message here..."
                                          disabled></textarea>
                            </div>
                        </div>
                        <div class="field">
                            <div class="control">
                                <button class="button is-primary" id="sendButton" disabled>
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
        const connectButton = document.getElementById('connectButton');
        const usernameInput = document.getElementById('username');
        const botToggle = document.getElementById('botToggle');

        let stompClient = null;
        let sessionId = '$unsafe{sessionId}'; // Session ID from server
        let username = '${username}';
        let connected = false;

        // Function to connect to WebSocket
        function connect() {
            // Only update username from input if not authenticated
            if (!${authenticated ? "true" : "false"}) {
                username = usernameInput.value.trim();
                if (!username) {
                    alert('Please enter a username');
                    return;
                }
            }

            // Disable connect button and username input
            connectButton.disabled = true;
            usernameInput.disabled = true;

            // Show connecting message
            chatContainer.innerHTML = '<div class="connecting">Connecting to chat server...</div>';

            // Connect to WebSocket
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            // Enable debug for STOMP
            stompClient.debug = function (str) {
                console.log('STOMP Debug: ' + str);
            };

            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                connected = true;

                // Subscribe to the chat topic
                console.log('Subscribing to topic: /topic/chat/' + sessionId);
                stompClient.subscribe('/topic/chat/' + sessionId, function (message) {
                    console.log('Received message:', message);
                    try {
                        const chatMessage = JSON.parse(message.body);
                        console.log('Parsed message:', chatMessage);
                        displayMessage(chatMessage);
                    } catch (e) {
                        console.error('Error parsing message:', e);
                        console.error('Message body:', message.body);
                    }
                });

                // Send join message
                sendJoinMessage();

                // Enable message input and send button
                messageInput.disabled = false;
                sendButton.disabled = false;
                messageInput.focus();

                // Change connect button to disconnect
                connectButton.textContent = 'Disconnect';
                connectButton.classList.remove('is-info');
                connectButton.classList.add('is-danger');
                connectButton.disabled = false;

                // Clear connecting message
                chatContainer.innerHTML = '';

                // Add welcome message
                const welcomeMsg = {
                    type: 'JOIN',
                    content: 'Welcome to the chat! Type a message and press Send.',
                    senderName: 'System',
                    timestamp: new Date()
                };
                displayMessage(welcomeMsg);

            }, function (error) {
                console.error('Error connecting to WebSocket: ', error);
                chatContainer.innerHTML = '<div class="connecting">Error connecting to chat server. Please try again.</div>';

                // Re-enable connect button and username input
                connectButton.disabled = false;
                usernameInput.disabled = false;
            });
        }

        // Function to disconnect from WebSocket
        function disconnect() {
            if (stompClient !== null) {
                // Send leave message
                const leaveMessage = {
                    type: 'LEAVE',
                    content: username + ' left the chat',
                    senderName: username,
                    sessionId: sessionId,
                    timestamp: new Date()
                };
                stompClient.send('/app/chat/' + sessionId, {}, JSON.stringify(leaveMessage));

                // Disconnect from WebSocket
                stompClient.disconnect();
                stompClient = null;
                connected = false;

                // Disable message input and send button
                messageInput.disabled = true;
                sendButton.disabled = true;

                // Change disconnect button to connect
                connectButton.textContent = 'Connect';
                connectButton.classList.remove('is-danger');
                connectButton.classList.add('is-info');

                // Enable username input
                usernameInput.disabled = false;
                connectButton.disabled = false;

                // Clear chat container
                chatContainer.innerHTML = '<div class="connecting">Disconnected from chat server.</div>';
            }
        }

        // Function to send a join message
        function sendJoinMessage() {
            const joinMessage = {
                type: 'JOIN',
                content: username + ' joined the chat',
                senderName: username,
                sessionId: sessionId,
                timestamp: new Date()
            };
            stompClient.send('/app/chat/' + sessionId + '/join', {}, JSON.stringify(joinMessage));
        }

        // Function to send a chat message
        function sendMessage() {
            const content = messageInput.value.trim();
            if (content && stompClient) {
                const chatMessage = {
                    type: 'CHAT',
                    content: content,
                    senderName: username,
                    senderId: username, // Using username as senderId for simplicity
                    sessionId: sessionId,
                    timestamp: new Date()
                };
                stompClient.send('/app/chat/' + sessionId, {}, JSON.stringify(chatMessage));
                messageInput.value = '';
            }
        }

        // Function to display a message in the chat
        function displayMessage(message) {
            console.log('Displaying message:', message);

            try {
                const messageElement = document.createElement('div');
                messageElement.classList.add('message-bubble');

                // Add appropriate class based on message type
                if (message.type === 'CHAT') {
                    console.log('Message type: CHAT');
                    if (message.senderName === username) {
                        messageElement.classList.add('message-user');
                    } else {
                        messageElement.classList.add('message-other');
                    }
                } else if (message.type === 'BOT') {
                    console.log('Message type: BOT');
                    messageElement.classList.add('message-bot');
                } else if (message.type === 'JOIN' || message.type === 'LEAVE') {
                    console.log('Message type: ' + message.type);
                    messageElement.classList.add('message-' + message.type.toLowerCase());
                } else {
                    console.warn('Unknown message type:', message.type);
                }

                // Add sender info for chat messages
                if (message.type === 'CHAT' || message.type === 'BOT') {
                    const infoElement = document.createElement('div');
                    infoElement.classList.add('message-info');
                    infoElement.textContent = message.senderName + ' • ' +
                        (message.timestamp ? new Date(message.timestamp).toLocaleTimeString() : new Date().toLocaleTimeString());
                    messageElement.appendChild(infoElement);
                }

                // Add message content
                const contentElement = document.createElement('div');
                contentElement.textContent = message.content;
                messageElement.appendChild(contentElement);

                // Add message to chat container
                chatContainer.appendChild(messageElement);
                console.log('Message added to chat container');

                // Scroll to bottom
                chatContainer.scrollTop = chatContainer.scrollHeight;
            } catch (e) {
                console.error('Error displaying message:', e);
                console.error('Message object:', message);
            }
        }

        // Event listener for connect/disconnect button
        connectButton.addEventListener('click', function () {
            if (!connected) {
                connect();
            } else {
                disconnect();
            }
        });

        // Event listener for send button
        sendButton.addEventListener('click', function () {
            sendMessage();
        });

        // Event listener for Enter key in message input
        messageInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });

        // Event listener for bot toggle
        botToggle.addEventListener('change', function () {
            if (connected) {
                // Send a message to update the session's bot setting
                const updateMessage = {
                    type: 'CHAT',
                    content: 'Bot assistant ' + (botToggle.checked ? 'enabled' : 'disabled'),
                    senderName: 'System',
                    sessionId: sessionId,
                    botEnabled: botToggle.checked,
                    timestamp: new Date()
                };
                stompClient.send('/app/chat/' + sessionId, {}, JSON.stringify(updateMessage));
            }
        });

        // Focus username input on page load or auto-connect if authenticated
        const isAuthenticated = ${authenticated ? "true" : "false"};
        if (isAuthenticated) {
            // Auto-connect if authenticated
            connect();
        } else {
            usernameInput.focus();
        }
    });
</script>
</body>
</html>
