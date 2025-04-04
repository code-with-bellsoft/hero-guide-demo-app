@param String title = "Bot Assistant"

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
@template.fragments.header(title = title)
<body>
@template.fragments.navbar()

<section class="section">
    <div class="container">
        <div class="columns">
            <div class="column is-8 is-offset-2">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            Welcome to the Bot Assistant
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <p>A simple AI bot assistant built with Spring Boot, Spring AI, and Redis.</p>
                            <p>Features:</p>
                            <ul>
                                <li><strong><a href="/assistant">AI mvConversations</a></strong>: Chat with an AI that can answer questions</li>
                                <li><strong>Caching</strong>: Stores common responses for faster replies</li>
                                <li><strong>API</strong>: Can be used by other applications</li>
                                <li><strong><a href="/admin">Admin Dashboard</a></strong>: View stats and test the bot</li>
                            </ul>
                            <p>Helpful for answering questions and providing information.</p>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/assistant" class="card-footer-item">
                                <span class="icon">
                                    <i class="fas fa-robot"></i>
                                </span>
                            <span>Try the Assistant</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

@template.fragments.footer()
</body>
</html>
