import React, { useState, useEffect } from "react";
import IndexLayout from "./IndexLayout";
import "../assets/css/OTLoggingPage.css";

interface OTLog {
    date: string;
    startHour: string;
    endHour: string;
    totalHour: number;
    project: string;
    approval: string;
}

const OTLogging: React.FC = () => {
    const [date, setDate] = useState("");
    const [startHour, setStartHour] = useState("");
    const [endHour, setEndHour] = useState("");
    const [project, setProject] = useState("");
    const [otLogs, setOtLogs] = useState<OTLog[]>([
        {
            date: "2024-06-15",
            startHour: "18:00",
            endHour: "21:00",
            totalHour: 3,
            project: "Migration",
            approval: "Approved"
        }
    ]);

    const fetchLogs = () => {
        const name = localStorage.getItem("userName") || "Unknown";
        fetch(`http://localhost:8080/api/employee/overtime?name=${name}`)
            .then(res => res.json())
            .then(data => {
                if (Array.isArray(data)) setOtLogs(data);
                else console.warn("Unexpected OT log format", data);
            })
            .catch(() => console.error("Error fetching OT logs"));
    };

    useEffect(() => {
        fetchLogs();
    }, []);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const name = localStorage.getItem("userName") || "Unknown";

        // compare time strings as Date objects to validate ordering
        const start = new Date(`1970-01-01T${startHour}`);
        const end = new Date(`1970-01-01T${endHour}`);

        if (end <= start) {
            alert("Invalid hours: end must be after start.");
            return;
        }

        // Send "HH:mm" string values directly
        fetch("http://localhost:8080/api/employee/overtime_log", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name,
                date,
                startHour,  // "19:00"
                endHour,    // "20:00"
                project
            })
        })
            .then(res => {
                if (res.ok) {
                    alert("OT logged!");
                    fetchLogs();
                } else {
                    alert("Failed to log OT");
                }
            })
            .catch(() => alert("Error logging OT"));
    };

    return (
        <IndexLayout userName={localStorage.getItem("userName") || "Unknown"}>
            <h2 className="dashboardTitle">OT Logging</h2>

            <form onSubmit={handleSubmit} className="card" style={{ maxWidth: 400 }}>
                <label>
                    Date:
                    <input type="date" value={date} onChange={e => setDate(e.target.value)} required />
                </label>

                <label style={{marginTop: "1rem"}}>
                    Start Hour:
                    <input
                        type="time"
                        value={startHour}
                        onChange={(e) => setStartHour(e.target.value)}
                        required
                        step="60"
                    />
                </label>

                <label style={{marginTop: "1rem"}}>
                    End Hour:
                    <input
                        type="time"
                        value={endHour}
                        onChange={(e) => setEndHour(e.target.value)}
                        required
                        step="60"
                    />
                </label>


                <label style={{marginTop: "1rem"}}>
                    Project:
                    <input type="text" value={project} onChange={e => setProject(e.target.value)} />
                </label>

                <button type="submit" className="logout-button" style={{ marginTop: "1.5rem" }}>
                    Submit OT
                </button>
            </form>

            <div className="card" style={{ marginTop: "2rem", maxWidth: 600 }}>
                <h3>Logged OT History</h3>
                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                    <thead>
                    <tr style={{ borderBottom: "1px solid #ccc" }}>
                        <th style={{ textAlign: "left", padding: "8px" }}>Date</th>
                        <th style={{ textAlign: "left", padding: "8px" }}>Start</th>
                        <th style={{ textAlign: "left", padding: "8px" }}>End</th>
                        <th style={{ textAlign: "left", padding: "8px" }}>Total</th>
                        <th style={{ textAlign: "left", padding: "8px" }}>Project</th>
                        <th style={{ textAlign: "left", padding: "8px" }}>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {otLogs.map((log, index) => (
                        <tr key={index}>
                            <td style={{ padding: "8px" }}>{log.date}</td>
                            <td style={{ padding: "8px" }}>{log.startHour}</td>
                            <td style={{ padding: "8px" }}>{log.endHour}</td>
                            <td style={{ padding: "8px" }}>{log.totalHour}</td>
                            <td style={{ padding: "8px" }}>{log.project}</td>
                            <td style={{ padding: "8px" }}>{log.approval}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </IndexLayout>
    );
};

export default OTLogging;
