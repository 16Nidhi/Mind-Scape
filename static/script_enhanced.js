// Enhanced MindCare+ JavaScript
let currentUser = null;
let searchIndex = {};

// Authentication Functions
function showLogin() {
    document.getElementById('auth-modal').style.display = 'block';
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('register-form').style.display = 'none';
}

function showRegister() {
    document.getElementById('auth-modal').style.display = 'block';
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('register-form').style.display = 'block';
}

function switchToRegister() {
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('register-form').style.display = 'block';
}

function switchToLogin() {
    document.getElementById('login-form').style.display = 'block';
    document.getElementById('register-form').style.display = 'none';
}

function closeModal() {
    document.getElementById('auth-modal').style.display = 'none';
}

function login() {
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    if (!username || !password) {
        showNotification('Please fill in all fields', 'error');
        return;
    }
    
    fetch('/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    })
    .then(res => {
        if (res.ok) {
            currentUser = username;
            updateAuthUI();
            closeModal();
            showNotification('Login successful!', 'success');
            loadUserData();
        } else {
            showNotification('Invalid credentials', 'error');
        }
    })
    .catch(() => showNotification('Login failed', 'error'));
}

function register() {
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    
    if (!username || !email || !password) {
        showNotification('Please fill in all fields', 'error');
        return;
    }
    
    fetch('/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `username=${encodeURIComponent(username)}&email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
    .then(res => {
        if (res.ok) {
            showNotification('Registration successful! Please login.', 'success');
            switchToLogin();
        } else {
            showNotification('Registration failed', 'error');
        }
    })
    .catch(() => showNotification('Registration failed', 'error'));
}

function logout() {
    fetch('/logout')
    .then(() => {
        currentUser = null;
        updateAuthUI();
        showNotification('Logged out successfully', 'info');
        clearUserData();
    });
}

function updateAuthUI() {
    const isLoggedIn = currentUser !== null;
    document.getElementById('welcome-msg').style.display = isLoggedIn ? 'inline' : 'none';
    document.getElementById('login-btn').style.display = isLoggedIn ? 'none' : 'inline';
    document.getElementById('register-btn').style.display = isLoggedIn ? 'none' : 'inline';
    document.getElementById('logout-btn').style.display = isLoggedIn ? 'inline' : 'none';
    
    if (isLoggedIn) {
        document.getElementById('welcome-msg').textContent = `Welcome, ${currentUser}!`;
    }
}

// Enhanced Mood Functions
function getMood() {
    const btn = document.getElementById("mood-btn");
    const loading = document.getElementById("mood-loading");
    btn.disabled = true;
    loading.style.display = "inline";
    
    fetch("/mood")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch mood");
            return res.text();
        })
        .then(data => {
            document.getElementById("mood-display").textContent = data;
            showNotification('Mood updated!', 'info');
        })
        .catch(() => {
            document.getElementById("mood-display").textContent = "⚠️ Error fetching mood.";
            showNotification('Error fetching mood', 'error');
        })
        .finally(() => {
            btn.disabled = false;
            loading.style.display = "none";
        });
}

function getMoodInsights() {
    if (!currentUser) {
        showNotification('Please login to view insights', 'warning');
        return;
    }
    
    fetch('/mood-insights')
        .then(res => res.text())
        .then(data => {
            showNotification(data, 'info');
        })
        .catch(() => showNotification('Error fetching insights', 'error'));
}

function getMoodPrediction() {
    if (!currentUser) {
        showNotification('Please login to view predictions', 'warning');
        return;
    }
    
    fetch('/mood-prediction')
        .then(res => res.text())
        .then(data => {
            showNotification(data, 'info');
        })
        .catch(() => showNotification('Error fetching prediction', 'error'));
}

function getMoodAnalytics() {
    if (!currentUser) {
        showNotification('Please login to view analytics', 'warning');
        return;
    }
    
    fetch('/mood-analytics')
        .then(res => res.text())
        .then(data => {
            document.getElementById('analytics-content').innerHTML = `<h4>Mood Analytics</h4><pre>${data}</pre>`;
        })
        .catch(() => showNotification('Error fetching analytics', 'error'));
}

// Enhanced Quote Function
function getQuote() {
    const btn = document.getElementById("quote-btn");
    const loading = document.getElementById("quote-loading");
    btn.disabled = true;
    loading.style.display = "inline";
    
    fetch("/quote")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch quote");
            return res.text();
        })
        .then(data => {
            document.getElementById("quote-display").textContent = data;
        })
        .catch(() => {
            document.getElementById("quote-display").textContent = "⚠️ Error fetching quote.";
            showNotification('Error fetching quote', 'error');
        })
        .finally(() => {
            btn.disabled = false;
            loading.style.display = "none";
        });
}

// Enhanced Journal Functions
function saveJournal() {
    const btn = document.getElementById("journal-btn");
    const loading = document.getElementById("journal-loading");
    const entry = document.getElementById("journal-input").value;
    
    if (!entry.trim()) {
        showNotification('Please write something before saving', 'warning');
        return;
    }
    
    btn.disabled = true;
    loading.style.display = "inline";
    
    fetch("/journal", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: entry
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to save journal");
            return res.text();
        })
        .then(() => {
            showNotification('Journal entry saved!', 'success');
            document.getElementById("journal-input").value = "";
            showJournalEntries(); // Refresh entries
        })
        .catch(() => {
            showNotification('Error saving journal entry', 'error');
        })
        .finally(() => {
            btn.disabled = false;
            loading.style.display = "none";
        });
}

function showJournalEntries() {
    if (!currentUser) {
        document.getElementById("journal-log").textContent = "Please login to view entries";
        return;
    }
    
    fetch('/journal')
        .then(res => res.text())
        .then(data => {
            document.getElementById("journal-log").textContent = data || "No entries yet.";
        })
        .catch(() => {
            document.getElementById("journal-log").textContent = "Error loading entries.";
        });
}

// Enhanced Mood History
function showMoodLog() {
    const btn = document.getElementById("moodlog-btn");
    const loading = document.getElementById("moodlog-loading");
    btn.disabled = true;
    loading.style.display = "inline";
    
    fetch("/mood-log")
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch mood log");
            return res.text();
        })
        .then(data => {
            document.getElementById("mood-log").textContent = data || "No mood data";
        })
        .catch(() => {
            document.getElementById("mood-log").textContent = "⚠️ Error loading mood history.";
            showNotification('Error loading mood history', 'error');
        })
        .finally(() => {
            btn.disabled = false;
            loading.style.display = "none";
        });
}

// Meditation Functions
function getBreathingExercise() {
    fetch('/breathing-exercise')
        .then(res => res.text())
        .then(data => {
            document.getElementById('meditation-content').innerHTML = 
                `<h4>Breathing Exercise</h4><p>${data}</p>`;
        })
        .catch(() => showNotification('Error fetching breathing exercise', 'error'));
}

function getMeditationPrompt() {
    fetch('/meditation')
        .then(res => res.text())
        .then(data => {
            document.getElementById('meditation-content').innerHTML = 
                `<h4>Meditation Guide</h4><p>${data}</p>`;
        })
        .catch(() => showNotification('Error fetching meditation prompt', 'error'));
}

function getCalmingActivity() {
    fetch('/calming-activity')
        .then(res => res.text())
        .then(data => {
            document.getElementById('meditation-content').innerHTML = 
                `<h4>Calming Activity</h4><p>${data}</p>`;
        })
        .catch(() => showNotification('Error fetching calming activity', 'error'));
}

function startMeditationSession() {
    const minutes = document.getElementById('meditation-minutes').value;
    fetch(`/meditation-session?minutes=${minutes}`)
        .then(res => res.text())
        .then(data => {
            document.getElementById('meditation-content').innerHTML = 
                `<h4>Meditation Session</h4><p>${data}</p>`;
            startTimer(parseInt(minutes));
        })
        .catch(() => showNotification('Error starting meditation session', 'error'));
}

function startTimer(minutes) {
    let seconds = minutes * 60;
    const timerDisplay = document.createElement('div');
    timerDisplay.id = 'timer-display';
    timerDisplay.style.cssText = 'font-size: 2rem; text-align: center; margin: 1rem 0; color: var(--primary);';
    document.getElementById('meditation-content').appendChild(timerDisplay);
    
    const timer = setInterval(() => {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        timerDisplay.textContent = `${mins}:${secs.toString().padStart(2, '0')}`;
        
        if (seconds <= 0) {
            clearInterval(timer);
            timerDisplay.textContent = '🧘‍♀️ Session Complete!';
            showNotification('Meditation session complete! 🧘‍♀️', 'success');
        }
        seconds--;
    }, 1000);
}

// Goals Functions
function addGoal() {
    if (!currentUser) {
        showNotification('Please login to add goals', 'warning');
        return;
    }
    
    const title = document.getElementById('goal-title').value;
    const description = document.getElementById('goal-description').value;
    const targetDate = document.getElementById('goal-date').value;
    
    if (!title) {
        showNotification('Please enter a goal title', 'warning');
        return;
    }
    
    fetch('/goals', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `title=${encodeURIComponent(title)}&description=${encodeURIComponent(description)}&targetDate=${targetDate}`
    })
    .then(res => res.text())
    .then(data => {
        showNotification(data, 'success');
        clearGoalInputs();
        loadGoals();
    })
    .catch(() => showNotification('Error adding goal', 'error'));
}

function loadGoals() {
    if (!currentUser) return;
    
    fetch('/goals')
        .then(res => res.text())
        .then(data => {
            document.getElementById('goals-list').innerHTML = data || 'No goals yet.';
        })
        .catch(() => document.getElementById('goals-list').innerHTML = 'Error loading goals.');
}

function clearGoalInputs() {
    document.getElementById('goal-title').value = '';
    document.getElementById('goal-description').value = '';
    document.getElementById('goal-date').value = '';
}

// Reminders Functions
function addReminder() {
    if (!currentUser) {
        showNotification('Please login to add reminders', 'warning');
        return;
    }
    
    const title = document.getElementById('reminder-title').value;
    const description = document.getElementById('reminder-description').value;
    const reminderTime = document.getElementById('reminder-time').value;
    
    if (!title || !reminderTime) {
        showNotification('Please enter title and time', 'warning');
        return;
    }
    
    fetch('/reminders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `title=${encodeURIComponent(title)}&description=${encodeURIComponent(description)}&reminderTime=${reminderTime}`
    })
    .then(res => res.text())
    .then(data => {
        showNotification(data, 'success');
        clearReminderInputs();
        loadReminders();
    })
    .catch(() => showNotification('Error adding reminder', 'error'));
}

function loadReminders() {
    if (!currentUser) return;
    
    fetch('/reminders')
        .then(res => res.text())
        .then(data => {
            document.getElementById('reminders-list').innerHTML = data || 'No reminders set.';
        })
        .catch(() => document.getElementById('reminders-list').innerHTML = 'Error loading reminders.');
}

function clearReminderInputs() {
    document.getElementById('reminder-title').value = '';
    document.getElementById('reminder-description').value = '';
    document.getElementById('reminder-time').value = '';
}

// Habit Tracking Functions
function addHabit() {
    if (!currentUser) {
        showNotification('Please login to add habits', 'warning');
        return;
    }
    
    const name = document.getElementById('habit-name').value;
    const targetValue = document.getElementById('habit-target').value;
    const unit = document.getElementById('habit-unit').value;
    
    if (!name) {
        showNotification('Please enter a habit name', 'warning');
        return;
    }
    
    fetch('/habits', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `name=${encodeURIComponent(name)}&targetValue=${targetValue}&unit=${encodeURIComponent(unit)}`
    })
    .then(res => res.text())
    .then(data => {
        showNotification(data, 'success');
        clearHabitInputs();
        loadHabits();
    })
    .catch(() => showNotification('Error adding habit', 'error'));
}

function loadHabits() {
    if (!currentUser) return;
    
    fetch('/habits')
        .then(res => res.text())
        .then(data => {
            document.getElementById('habits-list').innerHTML = data || 'No habits being tracked.';
        })
        .catch(() => document.getElementById('habits-list').innerHTML = 'Error loading habits.');
}

function clearHabitInputs() {
    document.getElementById('habit-name').value = '';
    document.getElementById('habit-target').value = '1';
    document.getElementById('habit-unit').value = 'times';
}

function getHabitStats() {
    if (!currentUser) {
        showNotification('Please login to view statistics', 'warning');
        return;
    }
    
    fetch('/habit-stats')
        .then(res => res.text())
        .then(data => {
            document.getElementById('analytics-content').innerHTML = 
                `<h4>Habit Statistics</h4><pre>${data}</pre>`;
        })
        .catch(() => showNotification('Error fetching habit statistics', 'error'));
}

// Analytics Functions
function getPersonalizedTips() {
    if (!currentUser) {
        showNotification('Please login to get personalized tips', 'warning');
        return;
    }
    
    fetch('/personalized-tips')
        .then(res => res.text())
        .then(data => {
            document.getElementById('analytics-content').innerHTML = 
                `<h4>Personalized Tips</h4><pre>${data}</pre>`;
        })
        .catch(() => showNotification('Error fetching personalized tips', 'error'));
}

function searchJournal() {
    const query = prompt('Enter search term:');
    if (!query) return;
    
    // Simple client-side search for now
    const journalContent = document.getElementById('journal-log').textContent;
    if (journalContent.toLowerCase().includes(query.toLowerCase())) {
        showNotification(`Found "${query}" in your journal entries`, 'success');
    } else {
        showNotification(`"${query}" not found in journal entries`, 'info');
    }
}

// Notification System
function showNotification(message, type = 'info') {
    const container = document.getElementById('notification-container');
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" style="background: none; border: none; color: inherit; cursor: pointer; padding: 0; margin-left: 10px;">&times;</button>
        </div>
    `;
    
    container.appendChild(notification);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

function showNotifications() {
    showNotification('Check your daily reminders and goals!', 'info');
}

// Enhanced Theme Toggle
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("theme-toggle").onclick = () => {
        document.body.classList.toggle("dark");
        const isDark = document.body.classList.contains("dark");
        document.getElementById("theme-toggle").textContent = isDark ? "☀️" : "🌙";
        localStorage.setItem('darkMode', isDark);
    };
});

// Enhanced Chatbot
function toggleChat() {
    const body = document.getElementById("chat-body");
    body.style.display = body.style.display === "flex" ? "none" : "flex";
}

function sendChat(event) {
    if (event.key === "Enter") {
        const input = document.getElementById("chat-input");
        const msg = input.value.trim();
        const loading = document.getElementById("chat-loading");
        
        if (!msg) return;
        
        // Add user message
        document.getElementById("chat-log").innerHTML += `<div><b>You:</b> ${msg}</div>`;
        input.value = "";
        loading.style.display = "inline";
        
        // Scroll to bottom
        const chatLog = document.getElementById("chat-log");
        chatLog.scrollTop = chatLog.scrollHeight;
        
        fetch("/chat?msg=" + encodeURIComponent(msg))
            .then(res => {
                if (!res.ok) throw new Error("Failed to get bot reply");
                return res.text();
            })
            .then(reply => {
                document.getElementById("chat-log").innerHTML += `<div><b>Assistant:</b> ${reply}</div>`;
                chatLog.scrollTop = chatLog.scrollHeight;
            })
            .catch(() => {
                document.getElementById("chat-log").innerHTML += `<div><b>Assistant:</b> ⚠️ Error getting reply.</div>`;
            })
            .finally(() => {
                loading.style.display = "none";
            });
    }
}

// Data Management
function loadUserData() {
    if (!currentUser) return;
    
    loadGoals();
    loadReminders();
    loadHabits();
    showJournalEntries();
    showMoodLog();
}

function clearUserData() {
    document.getElementById('goals-list').innerHTML = 'No goals yet.';
    document.getElementById('reminders-list').innerHTML = 'No reminders set.';
    document.getElementById('habits-list').innerHTML = 'No habits being tracked.';
    document.getElementById('journal-log').textContent = 'No entries yet.';
    document.getElementById('mood-log').textContent = 'No mood data';
    document.getElementById('analytics-content').innerHTML = 'Your wellness insights will appear here.';
}

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    // Load theme preference
    if (localStorage.getItem('darkMode') === 'true') {
        document.body.classList.add('dark');
        document.getElementById("theme-toggle").textContent = "☀️";
    }
    
    // Check if user is logged in (simplified)
    updateAuthUI();
    
    // Show welcome notification
    showNotification('Welcome to MindCare+! Your mental wellness companion.', 'info');
});
