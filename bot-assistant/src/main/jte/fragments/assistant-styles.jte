<!--

    Copyright © 2025 BellSoft (info@bell-sw.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
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
        color: black;
    }

    .typing-indicator {
        display: none;
        padding: 0.5rem;
        font-style: italic;
        color: #888;
    }
</style>
