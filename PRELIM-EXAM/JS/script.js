/*
 * Programmer: Karina Cassandra M. Dig - STUDENT_ID_HERE
 */

const CSV_DATA = `StudentID,first,last,l1,l2,l3,prelim,att
29-4256-485,Ana,Cruz,80,90,85,88,95
12-1234-567,Juan,Dela Cruz,75,78,82,80,90
98-7654-321,Maria,Santos,90,92,94,91,98`;

let students = [];
let filteredStudents = [];
let selectedStudentId = null;

const tableBody = document.getElementById("tableBody");
const searchInput = document.getElementById("searchInput");

/* ===== PARSE CSV ===== */
function parseCSV() {
    const lines = CSV_DATA.split("\n").slice(1);
    lines.forEach(line => {
        const d = line.split(",");
        students.push({
            id: d[0],
            first: d[1],
            last: d[2],
            l1: d[3],
            l2: d[4],
            l3: d[5],
            prelim: d[6],
            att: d[7]
        });
    });
    filteredStudents = [...students];
}

/* ===== RENDER ===== */
function render(data = filteredStudents) {
    tableBody.innerHTML = "";
    data.forEach(s => {
        tableBody.innerHTML += `
        <tr>
            <td>${s.id}</td>
            <td>${s.first}</td>
            <td>${s.last}</td>
            <td>${s.l1}</td>
            <td>${s.l2}</td>
            <td>${s.l3}</td>
            <td>${s.prelim}</td>
            <td>${s.att}</td>
            <td>
                <button onclick="editStudent('${s.id}')">Edit</button>
                <button onclick="deleteStudent('${s.id}')">Delete</button>
            </td>
        </tr>`;
    });
}

/* ===== ID FORMAT ===== */
studentId.addEventListener("input", () => {
    let digits = studentId.value.replace(/\D/g, "").slice(0, 9);
    let f = "";
    if (digits.length > 0) f += digits.slice(0, 2);
    if (digits.length > 2) f += "-" + digits.slice(2, 6);
    if (digits.length > 6) f += "-" + digits.slice(6, 9);
    studentId.value = f;
});

/* ===== GRADE VALIDATION ===== */
function validateGrades() {
    const grades = [labWork1, labWork2, labWork3, prelimExam, attendance];
    for (let g of grades) {
        if (g.value !== "" && (g.value < 0 || g.value > 100)) {
            alert("Grades must be between 0 and 100 only.");
            g.focus();
            return false;
        }
    }
    return true;
}

/* ===== AUTO-FILL FROM MOCK DATA ===== */
function autoFillGrades() {
    const f = firstName.value.toLowerCase().trim();
    const l = lastName.value.toLowerCase().trim();

    const found = students.find(
        s => s.first.toLowerCase() === f && s.last.toLowerCase() === l
    );

    if (found) {
        labWork1.value = found.l1;
        labWork2.value = found.l2;
        labWork3.value = found.l3;
        prelimExam.value = found.prelim;
        attendance.value = found.att;
    }
}

firstName.addEventListener("blur", autoFillGrades);
lastName.addEventListener("blur", autoFillGrades);

/* ===== ADD ===== */
function addStudent() {
    if (!studentId.value.match(/^\d{2}-\d{4}-\d{3}$/)) {
        alert("Student ID format must be ##-####-###");
        return;
    }

    if (!validateGrades()) return;

    students.push({
        id: studentId.value,
        first: firstName.value,
        last: lastName.value,
        l1: labWork1.value,
        l2: labWork2.value,
        l3: labWork3.value,
        prelim: prelimExam.value,
        att: attendance.value
    });

    filteredStudents = [...students];
    render();
    clearForm();
}

/* ===== EDIT ===== */
function editStudent(id) {
    const s = students.find(st => st.id === id);
    selectedStudentId = id;

    studentId.value = s.id;
    firstName.value = s.first;
    lastName.value = s.last;
    labWork1.value = s.l1;
    labWork2.value = s.l2;
    labWork3.value = s.l3;
    prelimExam.value = s.prelim;
    attendance.value = s.att;
}

/* ===== UPDATE ===== */
btnUpdate.onclick = () => {
    if (!selectedStudentId) return;
    deleteStudent(selectedStudentId);
    addStudent();
};

/* ===== DELETE ===== */
function deleteStudent(id) {
    students = students.filter(s => s.id !== id);
    filteredStudents = [...students];
    render();
}

/* ===== SEARCH ===== */
searchInput.addEventListener("input", () => {
    const key = searchInput.value.toLowerCase();
    filteredStudents = students.filter(s =>
        s.first.toLowerCase().includes(key) ||
        s.last.toLowerCase().includes(key)
    );
    render();
});

/* ===== CLEAR ===== */
function clearForm() {
    studentId.value = "";
    firstName.value = "";
    lastName.value = "";
    labWork1.value = "";
    labWork2.value = "";
    labWork3.value = "";
    prelimExam.value = "";
    attendance.value = "";
    selectedStudentId = null;
}

btnAdd.onclick = addStudent;
btnClear.onclick = clearForm;

/* ===== INIT ===== */
parseCSV();
render();
