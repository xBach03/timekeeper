import React, { useEffect, useState } from "react";
import IndexLayout from "./IndexLayout";
import "../assets/css/ShiftsPage.css"
interface Shift {
    date: string;
    time: string;
    location: string;
}

const ShiftsPage: React.FC = () => {
    const [shifts, setShifts] = useState<Shift[]>([]);
    const userName = localStorage.getItem("userName") || "Unknown";
    useEffect(() => {
        const userName = localStorage.getItem("userName");

        fetch(`http://localhost:8080/api/attendance/shifts?name=${userName}`)
            .then(res => {
                if (!res.ok) throw new Error(`Failed to fetch shifts: ${res.status}`);
                return res.json();
            })
            .then(data => {
                console.log("Shifts:", data);
                setShifts(data);
            })
            .catch((err) => {
                console.error("API error:", err);
                const mockShifts: Shift[] = [
                    { date: "2025-06-05", time: "09:00 - 17:00", location: "Main Office" },
                    { date: "2025-06-06", time: "10:00 - 18:00", location: "Remote" }
                ];
                setShifts(mockShifts);
            });
    }, []);


    return (
        <IndexLayout userName={userName}>
            <div className="shifts-container">
                <h2 className="shifts-title">Your Weekly Shifts</h2>
                {shifts === null ? (
                    <p className="loading">Loading shifts...</p>
                ) : shifts.length === 0 ? (
                    <p className="no-shifts">No shifts assigned yet.</p>
                ) : (
                    <ul className="shift-list">
                        {shifts.map((shift, idx) => (
                            <li key={idx} className="shift-item">
                                <span className="shift-date">{shift.date}</span>
                                <span className="shift-time">{shift.time}</span>
                                <span className="shift-location">@ {shift.location}</span>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </IndexLayout>
    );
};

export default ShiftsPage;
