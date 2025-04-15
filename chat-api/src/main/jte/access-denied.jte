@param String title = "Access Denied"
@param Boolean authenticated = false
@param String username = ""
@param Boolean isAdmin = false
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
@template.fragments.navbar(
    authenticated = authenticated,
    username = username,
    isAdmin = isAdmin
)

<section class="section">
    <div class="container">
        <div class="columns">
            <div class="column is-6 is-offset-3">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title has-background-danger-light has-text-danger">
                            <span class="icon mr-2">
                                <i class="fas fa-exclamation-triangle"></i>
                            </span>
                            Access Denied
                        </p>
                    </div>
                    <div class="card-content">
                        <div class="content has-text-centered">
                            <p class="is-size-4 mb-5">Sorry, you don't have permission to access this page.</p>
                            <p class="mb-5">This area is restricted to administrators only.</p>
                            <div class="buttons is-centered">
                                <a href="/" class="button is-primary">
                                    <span class="icon">
                                        <i class="fas fa-home"></i>
                                    </span>
                                    <span>Go to Home</span>
                                </a>
                                @if(!authenticated)
                                    <a href="/login" class="button is-info">
                                        <span class="icon">
                                            <i class="fas fa-sign-in-alt"></i>
                                        </span>
                                        <span>Login</span>
                                    </a>
                                @endif
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

@template.fragments.footer()
</body>
</html>