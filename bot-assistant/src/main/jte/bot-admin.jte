@param String title = "Bot Assistant - Admin"
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

<!DOCTYPE html>
<html lang="en">
@template.fragments.header(title = title)
<body>
@template.fragments.navbar()

<section class="section">
    <div class="container">
        <h1 class="title">Bot Assistant Admin</h1>

        <div class="columns">
            <!-- Bot Statistics -->
            <div class="column is-half">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            <span class="icon">
                                <i class="fas fa-chart-bar"></i>
                            </span>
                            <span>Bot Statistics</span>
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <pre id="statsContent">Loading statistics...</pre>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a class="card-footer-item" id="refreshStats">
                            <span class="icon">
                                <i class="fas fa-sync-alt"></i>
                            </span>
                            <span>Refresh</span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Cache Management -->
            <div class="column is-half">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            <span class="icon">
                                <i class="fas fa-database"></i>
                            </span>
                            <span>Cache Management</span>
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <p>Current cache hit ratio: <span id="cacheHitRatio">Loading...</span></p>
                            <button class="button is-danger" id="clearCacheBtn">
                                <span class="icon">
                                    <i class="fas fa-trash"></i>
                                </span>
                                <span>Clear Cache</span>
                            </button>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a class="card-footer-item" id="refreshCacheStats">
                            <span class="icon">
                                <i class="fas fa-sync-alt"></i>
                            </span>
                            <span>Refresh</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Health Check -->
        <div class="columns mt-4">
            <div class="column is-full">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            <span class="icon">
                                <i class="fas fa-heartbeat"></i>
                            </span>
                            <span>Health Check</span>
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <p>Bot Assistant Status: <span id="healthStatus">Checking...</span></p>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a class="card-footer-item" id="checkHealth">
                            <span class="icon">
                                <i class="fas fa-sync-alt"></i>
                            </span>
                            <span>Check Health</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Message Processing Test -->
        <div class="columns mt-4">
            <div class="column is-full">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            <span class="icon">
                                <i class="fas fa-comment"></i>
                            </span>
                            <span>Message Processing Test</span>
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content">
                            <div class="field">
                                <label class="label">Test Message</label>
                                <div class="control">
                                    <textarea class="textarea" id="testMessage" placeholder="Enter a test message"></textarea>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-primary" id="processMessageBtn">
                                        <span class="icon">
                                            <i class="fas fa-paper-plane"></i>
                                        </span>
                                        <span>Process Message</span>
                                    </button>
                                </div>
                            </div>
                            <div class="mt-4">
                                <label class="label">Response:</label>
                                <pre id="messageResponse">No response yet</pre>
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
    document.addEventListener('DOMContentLoaded', function() {
        // Get elements
        const statsContent = document.getElementById('statsContent');
        const cacheHitRatio = document.getElementById('cacheHitRatio');
        const healthStatus = document.getElementById('healthStatus');
        const testMessage = document.getElementById('testMessage');
        const messageResponse = document.getElementById('messageResponse');

        // Refresh stats
        document.getElementById('refreshStats').addEventListener('click', function() {
            fetchStats();
        });

        // Clear cache
        document.getElementById('clearCacheBtn').addEventListener('click', function() {
            clearCache();
        });

        // Refresh cache stats
        document.getElementById('refreshCacheStats').addEventListener('click', function() {
            fetchCacheHitRatio();
        });

        // Check health
        document.getElementById('checkHealth').addEventListener('click', function() {
            checkHealth();
        });

        // Process message
        document.getElementById('processMessageBtn').addEventListener('click', function() {
            processMessage();
        });

        // Fetch statistics
        async function fetchStats() {
            try {
                statsContent.textContent = 'Loading statistics...';
                const response = await fetch('/api/bot/stats');
                if (!response.ok) {
                    throw new Error('Failed to fetch statistics');
                }
                const data = await response.text();
                statsContent.textContent = data;
            } catch (error) {
                console.error('Error:', error);
                statsContent.textContent = 'Error fetching statistics';
            }
        }

        // Fetch cache hit ratio
        async function fetchCacheHitRatio() {
            try {
                cacheHitRatio.textContent = 'Loading...';
                const response = await fetch('/api/bot/cache/hit-ratio');
                if (!response.ok) {
                    throw new Error('Failed to fetch cache hit ratio');
                }
                const data = await response.json();
                cacheHitRatio.textContent = (data * 100).toFixed(2) + '%';
            } catch (error) {
                console.error('Error:', error);
                cacheHitRatio.textContent = 'Error fetching cache hit ratio';
            }
        }

        // Clear cache
        async function clearCache() {
            try {
                const response = await fetch('/api/bot/cache/clear', {
                    method: 'POST'
                });
                if (!response.ok) {
                    throw new Error('Failed to clear cache');
                }
                const data = await response.text();
                alert(data);
                fetchCacheHitRatio(); // Refresh cache stats after clearing
            } catch (error) {
                console.error('Error:', error);
                alert('Error clearing cache');
            }
        }

        // Check health
        async function checkHealth() {
            try {
                healthStatus.textContent = 'Checking...';
                const response = await fetch('/api/bot/health');
                if (!response.ok) {
                    throw new Error('Health check failed');
                }
                const data = await response.text();
                healthStatus.textContent = data;
                healthStatus.style.color = 'green';
            } catch (error) {
                console.error('Error:', error);
                healthStatus.textContent = 'Service Unavailable';
                healthStatus.style.color = 'red';
            }
        }

        // Process message
        async function processMessage() {
            try {
                const message = testMessage.value.trim();
                if (!message) {
                    alert('Please enter a message');
                    return;
                }

                messageResponse.textContent = 'Processing...';

                const response = await fetch('/api/bot/process', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        content: message,
                        sessionId: 'admin-session',
                        type: 'CHAT',
                        senderId: 'admin',
                        senderName: 'Admin'
                    })
                });

                if (!response.ok) {
                    throw new Error('Failed to process message');
                }

                const data = await response.json();
                messageResponse.textContent = data.content;
            } catch (error) {
                console.error('Error:', error);
                messageResponse.textContent = 'Error processing message';
            }
        }

        // Initial data load
        fetchStats();
        fetchCacheHitRatio();
        checkHealth();
    });
</script>
</body>
</html>
