// ===== SOUND EFFECTS =====
const errorSound = new Audio("error.mp3");     // invalid name
const hooraySound = new Audio("hooray.mp3");   // attendance success
const clickSound = new Audio("click.mp3");     // home button click

// ===== SESSION-ONLY ATTENDANCE (CLEARS ON REFRESH) =====
let attendance = [];

// ===== LOGIN FUNCTION =====
function login() {
    const name = document.getElementById("username").value.trim();
    const message = document.getElementById("message");

    // ❌ EMPTY NAME
    if (!name) {
        message.style.color = "red";
        message.textContent = "Please enter your full name.";

        errorSound.currentTime = 0;
        errorSound.play().catch(() => {});
        return;
    }

    // ❌ NAME WITH NUMBERS
    if (/\d/.test(name)) {
        message.style.color = "red";
        message.textContent = "Full name must not contain numbers.";

        errorSound.currentTime = 0;
        errorSound.play().catch(() => {});
        return;
    }

    // ✅ VALID NAME → RECORD ATTENDANCE
    recordAttendance(name);
}

// ===== RECORD ATTENDANCE =====
function recordAttendance(name) {
    const time = new Date().toLocaleString();

    attendance.push({ name, time });

    hooraySound.currentTime = 0;
    hooraySound.play().catch(() => {});

    showSummary();
}

// ===== SHOW SUMMARY VIEW =====
function showSummary() {
    document.getElementById("loginView").style.display = "none";
    document.getElementById("summaryView").style.display = "block";

    const list = document.getElementById("attendanceList");
    list.innerHTML = "";

    attendance.forEach(record => {
        const li = document.createElement("li");
        li.textContent = `${record.name} — ${record.time}`;
        list.appendChild(li);
    });
}

// ===== HOME BUTTON =====
function goHome() {
    clickSound.currentTime = 0;
    clickSound.play().catch(() => {});

    document.getElementById("loginView").style.display = "block";
    document.getElementById("summaryView").style.display = "none";

    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
    document.getElementById("message").textContent = "";
}

// ===== DOWNLOAD ATTENDANCE FILE =====
function downloadAttendance() {
    if (attendance.length === 0) {
        alert("No attendance records to download.");
        return;
    }

    let attendanceData = "ATTENDANCE SUMMARY\n\n";

    attendance.forEach((record, index) => {
        attendanceData +=
            (index + 1) + ". Name: " + record.name + "\n" +
            "Time In: " + record.time + "\n\n";
    });

    const blob = new Blob([attendanceData], { type: "text/plain" });
    const link = document.createElement("a");

    link.href = URL.createObjectURL(blob);
    link.download = "attendance_summary.txt";
    link.click();

    URL.revokeObjectURL(link.href);
}
