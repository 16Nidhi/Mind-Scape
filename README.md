# MindScape - Mental Health Wellness App 🧠💙

A beautiful, comprehensive mental health application designed to support your wellness journey with modern web technologies and an intuitive user interface.

## 🚀 **Live Demo**

- **Local Server:** `http://localhost:8080` (after running manually)

## 🛠️ **How to Run the App**

### 🖥️ **Run Locally with Java Server**

**Prerequisites:**
- Java JDK 8 or higher
- Any text editor or IDE

**Steps:**
1. **Download/Clone the project**
   ```bash
   git clone https://github.com/yourusername/mindscape.git
   cd mindscape
   ```

2. **Compile the Java files**
   ```bash
   # Windows (PowerShell/Command Prompt)
   javac -cp "sqlite-jdbc.jar" *.java controllers/*.java
   
   # Linux/Mac
   javac -cp "sqlite-jdbc.jar" *.java controllers/*.java
   ```

3. **Run the server**
   ```bash
   # Windows
   java -cp ".;sqlite-jdbc.jar" Main
   
   # Linux/Mac
   java -cp ".:sqlite-jdbc.jar" Main
   ```

4. **Open your browser**
   - Go to: `http://localhost:8080`
   - Enjoy full database functionality!

**Features:**
- ✅ Complete database storage (SQLite)
- ✅ Persistent data across sessions
- ✅ Full MindBot AI responses
- ✅ Advanced analytics and insights
- ✅ All wellness features

## 🌟 **Features**

### 🎯 **Core Functionality**
- **Mood Tracking** - Daily mood logging with insights and analytics
- **Journal System** - Personal diary with local/database storage
- **Goal Setting** - Mental health goal tracking and management
- **Daily Reminders** - Time-based wellness reminders
- **Gratitude Journal** - Daily gratitude entries with prompts

### 🧘‍♀️ **Wellness Tools**
- **Meditation Timer** - Guided meditation sessions (5-20 minutes)
- **Breathing Exercises** - Various breathing techniques for stress relief
- **Calming Activities** - Mindfulness practices and relaxation techniques
- **Daily Quotes** - Motivational and inspirational content

### 🤖 **AI Assistant**
- **MindBot** - Interactive wellness companion chatbot
- **Personalized Tips** - Customized recommendations
- **Mood Analytics** - Data-driven insights and patterns

### 🎨 **Beautiful Design**
- **3-Page Layout** with smooth pagination navigation
- **Animated Gradients** - Dynamic background effects
- **Themed Cards** - Color-coordinated sections for different features
- **Dark/Light Mode** - User preference toggle
- **Responsive Design** - Works perfectly on desktop and mobile
- **Glassmorphism Effects** - Modern, translucent design elements

## � **How to Use**

### **Getting Started**
1. **Login/Register**: Click Login or Register (demo mode - any credentials work)
2. **Navigation**: Use pagination controls to browse between pages
3. **Explore Features**: Start using the wellness tools!

### **Navigation Guide**
- **Page 1: Mood & Wellness** - Track mood, read quotes, journal, view mood history
- **Page 2: Goals & Habits** - Set goals, create reminders, practice gratitude, view analytics  
- **Page 3: Meditation & Mindfulness** - Breathing exercises, meditation guides, timed sessions

### **Key Features**
- **MindBot Chat**: Click the floating chatbot icon (bottom-right corner)
- **Theme Toggle**: Use the moon/sun icon in header to switch themes
- **Data Persistence**: Your data is saved automatically

## �️ **Technical Stack**

### **Frontend**
- **HTML5, CSS3, JavaScript (ES6+)**
- **Custom CSS** with Grid, Flexbox, Animations
- **Responsive Design** for all devices

### **Backend**
- **Java HTTP Server** with custom routing
- **SQLite Database** for data persistence
- **Session Management** and user authentication

### **Storage**
- **SQLite database** for persistent data storage

## �️ **Technical Stack**

### **Frontend**
- **HTML5, CSS3, JavaScript (ES6+)**
- **Custom CSS** with Grid, Flexbox, Animations
- **Responsive Design** for all devices

### **Backend (Java Server Version)**
- **Java HTTP Server** with custom routing
- **SQLite Database** for data persistence
- **Session Management** and user authentication

### **Storage Options**
- **Server Version**: SQLite database
- **Static Version**: Browser localStorage
- **GitHub Pages**: localStorage only

## �📂 **Project Structure**

```
mindscape/
├── templates/
│   └── index.html          # Main application template
├── static/
│   ├── style.css           # Complete styling with themes
│   └── script.js           # Application JavaScript
├── controllers/            # Java backend files
│   ├── HomeController.java # Main request handler
│   ├── DatabaseManager.java # Database operations
│   ├── MoodTracker.java    # Mood tracking logic
│   ├── Assistant.java      # MindBot AI responses
│   └── ...                 # Other controllers
├── Main.java              # Server entry point
├── SQLiteHelper.java      # Database helper
├── sqlite-jdbc.jar        # Database driver
├── package.json           # Project metadata
└── README.md              # This documentation
```

## 🚀 **Troubleshooting**

### **Common Issues & Solutions**

**Java Server Won't Start:**
```bash
# Check Java version
java -version

# Ensure SQLite jar is present
ls sqlite-jdbc.jar

# Try alternative compilation
javac -cp "*" *.java controllers/*.java
```

**Port Already in Use:**
- Change port in `Main.java` (line with `HttpServer.create`)
- Or kill existing process: `netstat -ano | findstr :8080`

**Database Issues:**
- Delete existing database file and restart server
- Check file permissions in project directory

**Static Version Not Loading:**
- Use local web server instead of opening HTML directly
- Check browser console for JavaScript errors
- Ensure all files are in correct directories

## 🎨 **Color Themes**

Each section has its own beautiful color theme:
- **Mood**: Golden yellow gradient
- **Quotes**: Ocean blue gradient  
- **Journal**: Fresh green gradient
- **Mood Log**: Warm red gradient
- **Goals**: Royal purple gradient
- **Reminders**: Pink gradient
- **Gratitude**: Rose pink gradient
- **Analytics**: Orange gradient
- **Meditation**: Deep purple gradient

## 🌙 **Dark Mode**

Toggle between light and dark themes using the theme button in the header. Your preference is automatically saved and restored on next visit.

## 📱 **Responsive Design**

The application works seamlessly across all devices:
- **Desktop**: Full grid layout with hover effects
- **Tablet**: Responsive card arrangements
- **Mobile**: Single-column layout with touch-friendly controls

## 🔒 **Privacy & Data**

### **Data Storage:**
- **Java Server Version**: Uses local SQLite database file
- **No External Servers**: All data stays on your local device

### **Privacy Features:**
- No data transmission to external services
- Local storage only (your data, your control)
- No tracking or analytics
- Open source and transparent

## 💡 **Development**

### **Adding New Features:**
1. **Server Version**: Modify Java controllers and templates
2. **Styling**: Edit `static/style.css` for visual changes

### **Database Schema:**
```sql
-- Main tables in SQLite database
users (id, username, password, created_at)
moods (id, user_id, mood_value, date, notes)
journal_entries (id, user_id, title, content, date)
goals (id, user_id, title, description, target_date, completed)
gratitude_entries (id, user_id, entry_text, date)
```

### **Contributing:**
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📋 **Requirements**

### **For Java Server Version:**
- Java JDK 8+
- SQLite JDBC driver (included: `sqlite-jdbc.jar`)
- Any operating system (Windows, Linux, macOS)

### **For Development:**
- Text editor or IDE (VS Code, IntelliJ, Eclipse)
- Git for version control
- Basic knowledge of HTML/CSS/JavaScript

## 🆘 **Support**

### **Getting Help:**
- Check the **Troubleshooting** section above
- Review the project structure and file organization
- Test both server and static versions locally before deployment

### **Common Use Cases:**
- **Personal Use**: Run locally with Java server for full features
- **Development**: Local Java server for complete functionality with database
- **Production**: Java server version for full functionality

## 📄 **License**

This project is open source and available under the [MIT License](LICENSE).

## 🙏 **Acknowledgments**

- Designed with mental health and user experience in mind
- Inspired by modern wellness applications  
- Built with love for the mental health community
- Special thanks to the open-source community

---

**Made with 💙 for mental wellness**

*⚠️ **Important Notice**: This app is for wellness support and is not a replacement for professional mental health care. If you're experiencing serious mental health issues, please consult with a qualified healthcare provider.*

## 📞 **Emergency Resources**

- **Crisis Text Line**: Text HOME to 741741
- **National Suicide Prevention Lifeline**: 988
- **International Association for Suicide Prevention**: https://www.iasp.info/resources/Crisis_Centres/
