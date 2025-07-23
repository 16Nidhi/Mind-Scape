// Enhanced MindScape JavaScript - GitHub Pages Compatible
let currentUser = null;
let localData = {
    journal: [],
    gratitude: [],
    goals: [],
    reminders: [],
    moods: []
};
let currentPageIndex = 1;
let totalPages = 3;

// Load data from localStorage
function loadLocalData() {
    const stored = localStorage.getItem('mindscape-data');
    if (stored) {
        localData = JSON.parse(stored);
    }
}

// Save data to localStorage
function saveLocalData() {
    localStorage.setItem('mindscape-data', JSON.stringify(localData));
}

// Pagination Functions
function changePage(direction) {
    const newPage = currentPageIndex + direction;
    if (newPage >= 1 && newPage <= totalPages) {
        document.getElementById(`page-${currentPageIndex}`).classList.remove('active');
        currentPageIndex = newPage;
        document.getElementById(`page-${currentPageIndex}`).classList.add('active');
        updatePageNavigation();
    }
}

function goToPage(pageNumber) {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
        document.getElementById(`page-${currentPageIndex}`).classList.remove('active');
        currentPageIndex = pageNumber;
        document.getElementById(`page-${currentPageIndex}`).classList.add('active');
        updatePageNavigation();
    }
}

function updatePageNavigation() {
    document.getElementById('current-page').textContent = currentPageIndex;
    document.getElementById('total-pages').textContent = totalPages;
    document.getElementById('prev-page').disabled = currentPageIndex === 1;
    document.getElementById('next-page').disabled = currentPageIndex === totalPages;
    
    const activePage = document.querySelector('.page-content.active');
    if (activePage) {
        activePage.style.opacity = '0';
        activePage.style.transform = 'translateY(20px)';
        setTimeout(() => {
            activePage.style.opacity = '1';
            activePage.style.transform = 'translateY(0)';
        }, 100);
    }
}

// Authentication Functions (Demo Mode)
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
    
    // Demo login - just accept any credentials
    currentUser = username;
    updateAuthUI();
    closeModal();
    showNotification('Demo login successful!', 'success');
    loadUserData();
}

function register() {
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    
    if (!username || !email || !password) {
        showNotification('Please fill in all fields', 'error');
        return;
    }
    
    // Demo registration
    showNotification('Demo registration successful! Please login.', 'success');
    switchToLogin();
}

function logout() {
    currentUser = null;
    updateAuthUI();
    showNotification('Logged out successfully', 'info');
    clearUserData();
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

// Mood Functions
function getMood() {
    const moods = ["😄 Happy", "😔 Sad", "😡 Angry", "😌 Calm", "😐 Neutral", "😊 Joyful", "😴 Tired", "😎 Confident"];
    const mood = moods[Math.floor(Math.random() * moods.length)];
    
    document.getElementById("mood-display").textContent = mood;
    
    if (currentUser) {
        localData.moods.push({ mood, date: new Date().toISOString() });
        saveLocalData();
    }
    
    showNotification('Mood updated!', 'info');
}

function getMoodInsights() {
    if (!currentUser) {
        showNotification('Please login to view insights', 'warning');
        return;
    }
    
    const insights = [
        "Your mood patterns show positive trends this week!",
        "Consider meditation when feeling stressed.",
        "Regular journaling helps improve mood stability.",
        "You've been consistently happy lately - great job!"
    ];
    
    showNotification(insights[Math.floor(Math.random() * insights.length)], 'info');
}

function getMoodPrediction() {
    if (!currentUser) {
        showNotification('Please login to view predictions', 'warning');
        return;
    }
    
    const predictions = [
        "Tomorrow looks bright! 🌞",
        "You might feel energetic this afternoon! ⚡",
        "Consider some self-care this evening. 🛀",
        "A good day for social activities! 👥"
    ];
    
    showNotification(predictions[Math.floor(Math.random() * predictions.length)], 'info');
}

function getMoodAnalytics() {
    if (!currentUser) {
        showNotification('Please login to view analytics', 'warning');
        return;
    }
    
    const analytics = `
📊 Mood Analytics (Demo)
━━━━━━━━━━━━━━━━━━━━
😄 Happy: 45%
😌 Calm: 25%
😐 Neutral: 20%
😔 Sad: 10%
━━━━━━━━━━━━━━━━━━━━
📈 Trend: Improving
🎯 Recommendation: Keep up the good work!
    `;
    
    document.getElementById('analytics-content').innerHTML = `<h4>Mood Analytics</h4><pre>${analytics}</pre>`;
}

// Quote Function
function getQuote() {
    const quotes = [
        "🌟 You are stronger than you think.",
        "🌱 Every day is a new beginning.",
        "💪 Believe in yourself and all that you are.",
        "🌈 After every storm comes a rainbow.",
        "✨ Your potential is endless.",
        "🎯 Focus on progress, not perfection.",
        "💙 Be kind to yourself.",
        "🌸 Bloom where you are planted.",
        "🔥 You have the power to change your story.",
        "⭐ Small steps lead to big changes."
    ];
    
    const quote = quotes[Math.floor(Math.random() * quotes.length)];
    document.getElementById("quote-display").textContent = quote;
}

// Journal Functions
function saveJournal() {
    const entry = document.getElementById("journal-input").value;
    
    if (!entry.trim()) {
        showNotification('Please write something before saving', 'warning');
        return;
    }
    
    if (!currentUser) {
        showNotification('Please login to save journal entries', 'warning');
        return;
    }
    
    localData.journal.push({
        entry: entry,
        date: new Date().toLocaleDateString()
    });
    
    saveLocalData();
    showNotification('Journal entry saved!', 'success');
    document.getElementById("journal-input").value = "";
    showJournalEntries();
}

function showJournalEntries() {
    if (!currentUser) {
        document.getElementById("journal-log").textContent = "Please login to view entries";
        return;
    }
    
    if (localData.journal.length === 0) {
        document.getElementById("journal-log").textContent = "No entries yet.";
        return;
    }
    
    const entries = localData.journal.slice(-5).reverse().map(entry => 
        `[${entry.date}] ${entry.entry}`
    ).join('\n\n');
    
    document.getElementById("journal-log").textContent = entries;
}

function showMoodLog() {
    if (!currentUser) {
        document.getElementById("mood-log").textContent = "Please login to view mood history";
        return;
    }
    
    if (localData.moods.length === 0) {
        document.getElementById("mood-log").textContent = "No mood data";
        return;
    }
    
    const moodHistory = localData.moods.slice(-10).reverse().map(mood => 
        `${new Date(mood.date).toLocaleDateString()}: ${mood.mood}`
    ).join('\n');
    
    document.getElementById("mood-log").textContent = moodHistory;
}

// Meditation Functions
function getBreathingExercise() {
    const exercises = [
        "🫁 4-7-8 Breathing: Inhale for 4, hold for 7, exhale for 8",
        "🌬️ Box Breathing: Inhale 4, hold 4, exhale 4, hold 4",
        "💨 Equal Breathing: Inhale for 4 counts, exhale for 4 counts",
        "🌊 Ocean Breathing: Deep inhales and long, slow exhales"
    ];
    
    const exercise = exercises[Math.floor(Math.random() * exercises.length)];
    document.getElementById('meditation-content').innerHTML = 
        `<h4>Breathing Exercise</h4><p>${exercise}</p>`;
}

function getMeditationPrompt() {
    const prompts = [
        "🧘‍♀️ Body Scan: Focus on each part of your body from toes to head",
        "🌅 Mindful Observation: Notice 5 things you can see, 4 you can hear, 3 you can touch",
        "💭 Thought Watching: Observe your thoughts without judgment, like clouds passing",
        "❤️ Loving Kindness: Send positive thoughts to yourself and others"
    ];
    
    const prompt = prompts[Math.floor(Math.random() * prompts.length)];
    document.getElementById('meditation-content').innerHTML = 
        `<h4>Meditation Guide</h4><p>${prompt}</p>`;
}

function getCalmingActivity() {
    const activities = [
        "🌿 Take 5 deep breaths and focus on the present moment",
        "🎵 Listen to calming music for 10 minutes",
        "🌸 Step outside and notice nature around you",
        "💙 Practice gratitude by listing 3 things you're thankful for"
    ];
    
    const activity = activities[Math.floor(Math.random() * activities.length)];
    document.getElementById('meditation-content').innerHTML = 
        `<h4>Calming Activity</h4><p>${activity}</p>`;
}

function startMeditationSession() {
    const minutes = document.getElementById('meditation-minutes').value;
    document.getElementById('meditation-content').innerHTML = 
        `<h4>Meditation Session</h4><p>Starting ${minutes}-minute meditation session...</p>`;
    
    startTimer(parseInt(minutes));
}

function startTimer(minutes) {
    let seconds = minutes * 60;
    const timerDisplay = document.createElement('div');
    timerDisplay.id = 'timer-display';
    timerDisplay.style.cssText = 'font-size: 2rem; text-align: center; margin: 1rem 0; color: var(--primary);';
    
    const content = document.getElementById('meditation-content');
    content.appendChild(timerDisplay);
    
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
    
    localData.goals.push({
        title,
        description,
        targetDate,
        completed: false,
        date: new Date().toLocaleDateString()
    });
    
    saveLocalData();
    showNotification('Goal added!', 'success');
    clearGoalInputs();
    loadGoals();
}

function loadGoals() {
    if (!currentUser) return;
    
    if (localData.goals.length === 0) {
        document.getElementById('goals-list').innerHTML = 'No goals yet.';
        return;
    }
    
    const goalsHtml = localData.goals.map((goal, index) => 
        `<div class="goal-item ${goal.completed ? 'completed' : ''}">
            <strong>${goal.title}</strong><br>
            ${goal.description}<br>
            <small>Target: ${goal.targetDate || 'No date set'}</small>
        </div>`
    ).join('');
    
    document.getElementById('goals-list').innerHTML = goalsHtml;
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
    
    localData.reminders.push({
        title,
        description,
        reminderTime,
        date: new Date().toLocaleDateString()
    });
    
    saveLocalData();
    showNotification('Reminder added!', 'success');
    clearReminderInputs();
    loadReminders();
}

function loadReminders() {
    if (!currentUser) return;
    
    if (localData.reminders.length === 0) {
        document.getElementById('reminders-list').innerHTML = 'No reminders set.';
        return;
    }
    
    const remindersHtml = localData.reminders.map(reminder => 
        `<div class="reminder-item">
            <strong>${reminder.title}</strong><br>
            ${reminder.description}<br>
            <small>Time: ${reminder.reminderTime}</small>
        </div>`
    ).join('');
    
    document.getElementById('reminders-list').innerHTML = remindersHtml;
}

function clearReminderInputs() {
    document.getElementById('reminder-title').value = '';
    document.getElementById('reminder-description').value = '';
    document.getElementById('reminder-time').value = '';
}

// Gratitude Functions
function addGratitude() {
    if (!currentUser) {
        showNotification('Please login to add gratitude entries', 'warning');
        return;
    }
    
    const entry = document.getElementById('gratitude-entry').value;
    if (!entry.trim()) {
        showNotification('Please write something you\'re grateful for', 'warning');
        return;
    }
    
    localData.gratitude.push({
        entry,
        date: new Date().toLocaleDateString()
    });
    
    saveLocalData();
    showNotification('Gratitude added! 🙏', 'success');
    document.getElementById('gratitude-entry').value = '';
    loadGratitude();
}

function loadGratitude() {
    if (!currentUser) return;
    
    if (localData.gratitude.length === 0) {
        document.getElementById('gratitude-list').innerHTML = 'No gratitude entries yet.';
        updateGratitudeCount();
        return;
    }
    
    const gratitudeHtml = localData.gratitude.slice(-10).reverse().map(item => 
        `<div class='gratitude-item'>🙏 ${item.entry}</div>`
    ).join('');
    
    document.getElementById('gratitude-list').innerHTML = gratitudeHtml;
    updateGratitudeCount();
}

function getGratitudePrompt() {
    const prompts = [
        "Someone who made you smile today",
        "A small moment of joy you experienced",
        "Something beautiful you noticed in nature",
        "A skill or talent you appreciate about yourself",
        "A memory that brings you happiness"
    ];
    const prompt = prompts[Math.floor(Math.random() * prompts.length)];
    document.getElementById('gratitude-entry').placeholder = prompt;
    showNotification(`Prompt: ${prompt}`, 'info');
}

function updateGratitudeCount() {
    const count = localData.gratitude.length;
    document.getElementById('gratitude-count').textContent = `Entries: ${count}`;
}

// Analytics Functions
function getPersonalizedTips() {
    if (!currentUser) {
        showNotification('Please login to get personalized tips', 'warning');
        return;
    }
    
    const tips = `
🎯 Personalized Tips (Demo)
━━━━━━━━━━━━━━━━━━━━
• Try meditation for 10 minutes daily
• Write in your journal before bed
• Practice gratitude every morning
• Take breaks every hour
• Connect with friends regularly
━━━━━━━━━━━━━━━━━━━━
💡 Based on your recent activity
    `;
    
    document.getElementById('analytics-content').innerHTML = 
        `<h4>Personalized Tips</h4><pre>${tips}</pre>`;
}

function searchJournal() {
    const query = prompt('Enter search term:');
    if (!query) return;
    
    const found = localData.journal.some(entry => 
        entry.entry.toLowerCase().includes(query.toLowerCase())
    );
    
    if (found) {
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
    
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

function showNotifications() {
    showNotification('Check your daily reminders and goals!', 'info');
}

// Enhanced Chatbot
function toggleChat() {
    const body = document.getElementById("chat-body");
    body.style.display = body.style.display === "flex" ? "none" : "flex";
}

function sendChat(event) {
    if (event.key === "Enter") {
        const input = document.getElementById("chat-input");
        const msg = input.value.trim();
        
        if (!msg) return;
        
        document.getElementById("chat-log").innerHTML += `<div><b>You:</b> ${msg}</div>`;
        input.value = "";
        
        // Simple chatbot responses
        const responses = [
            "That's interesting! How does that make you feel?",
            "I understand. Would you like to try a breathing exercise?",
            "Have you considered writing about this in your journal?",
            "Remember to be kind to yourself. 💙",
            "Perhaps some meditation could help with that.",
            "You're doing great by reaching out. Keep going! 🌟",
            "Would you like me to suggest a calming activity?",
            "That sounds challenging. What's one small step you could take?"
        ];
        
        setTimeout(() => {
            const response = responses[Math.floor(Math.random() * responses.length)];
            document.getElementById("chat-log").innerHTML += `<div><b>MindBot:</b> ${response}</div>`;
            
            const chatLog = document.getElementById("chat-log");
            chatLog.scrollTop = chatLog.scrollHeight;
        }, 1000);
        
        const chatLog = document.getElementById("chat-log");
        chatLog.scrollTop = chatLog.scrollHeight;
    }
}

// Data Management
function loadUserData() {
    if (!currentUser) return;
    
    loadGoals();
    loadReminders();
    loadGratitude();
    showJournalEntries();
    showMoodLog();
}

function clearUserData() {
    document.getElementById('goals-list').innerHTML = 'No goals yet.';
    document.getElementById('reminders-list').innerHTML = 'No reminders set.';
    document.getElementById('gratitude-list').innerHTML = 'No gratitude entries yet.';
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
    
    // Theme toggle
    document.getElementById("theme-toggle").onclick = () => {
        document.body.classList.toggle("dark");
        const isDark = document.body.classList.contains("dark");
        document.getElementById("theme-toggle").textContent = isDark ? "☀️" : "🌙";
        localStorage.setItem('darkMode', isDark);
    };
    
    // Initialize pagination
    updatePageNavigation();
    
    // Load local data
    loadLocalData();
    
    // Check if user is logged in (simplified)
    updateAuthUI();
    
    // Show welcome notification
    showNotification('Welcome to MindScape! Your mental wellness companion. This is a demo version.', 'info');
});
