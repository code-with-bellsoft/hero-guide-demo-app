@param String title = "Chat Application - Login"
@param String error = null

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
            <div class="column is-6 is-offset-3">
                <div class="card">
                    <div class="card-header">
                        <p class="card-header-title">
                            Login to Chat Application
                        </p>
                    </div>
                    <div class="card-content">
                        @if(error != null)
                            <div class="notification is-danger">
                                ${error}
                            </div>
                        @endif
                        <form action="/login" method="post">
                            <div class="field">
                                <label class="label">Username</label>
                                <div class="control has-icons-left">
                                    <input class="input" type="text" name="username" placeholder="Enter your username"
                                           required>
                                    <span class="icon is-small is-left">
                                            <i class="fas fa-user"></i>
                                        </span>
                                </div>
                            </div>
                            <div class="field">
                                <label class="label">Password</label>
                                <div class="control has-icons-left">
                                    <input class="input" type="password" name="password"
                                           placeholder="Enter your password" required>
                                    <span class="icon is-small is-left">
                                            <i class="fas fa-lock"></i>
                                        </span>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <button class="button is-primary is-fullwidth" type="submit">
                                            <span class="icon">
                                                <i class="fas fa-sign-in-alt"></i>
                                            </span>
                                        <span>Login</span>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer">
                        <p class="card-footer-item">
                            Don't have an account? <a href="/register" class="ml-2">Register</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

@template.fragments.footer()
</body>
</html>
