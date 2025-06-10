import React, { useEffect, useState } from "react";
import IndexLayout from "./IndexLayout";
import "../assets/css/TodayProgressPage.css";

interface AttendanceData {
    date: string;
    checkIn: string | null;
    checkOut: string | null;
    streak: number;
}

export const TodayProgressPage: React.FC = () => {
    const userName = localStorage.getItem("userName") || "Unknown";
    const [data, setData] = useState<AttendanceData | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        fetch(`http://localhost:8080/api/attendance/today?name=${userName}`)
            .then(res => res.json())
            .then(res => setData(res))
            .catch(() => console.warn("Failed to fetch today's attendance"))
            .finally(() => setIsLoading(false));
    }, [userName]);

    const handleCheck = async (type: "checkin" | "checkout") => {
        try {
            // 1. Step 1: Recognize user
            const response = await fetch("http://localhost:8080/api/recognizer/recognize");
            const name = await response.text();

            if (!response.ok || !name || name.trim().length === 0) {
                alert("😕 Face not recognized. Please try again.");
                return;
            }

            const trimmedName = name.trim();
            const now = new Date();
            const isoWithOffset = now.toISOString();

            // 2. Step 2: Send attendance record
            const payload = {
                name: trimmedName,
                time: isoWithOffset,
                checkType: type // optional: backend can use this if needed
            };

            const res = await fetch(`http://localhost:8080/api/attendance/${type}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                const data = await res.json();
                alert(`✅ ${type === "checkin" ? "Check-in" : "Check-out"} recorded for ${data.name} at ${data.time}`);
                window.location.reload();
            } else {
                const error = await res.text();
                alert(`❌ ${type} failed for ${trimmedName}: ${error}`);
            }
        } catch (err) {
            console.error(err);
            alert("⚠️ Network or recognition error.");
        }
    };




    const calculateDuration = (checkIn: string, checkOut: string) => {
        const start = new Date(`1970-01-01T${checkIn}:00`);
        const end = new Date(`1970-01-01T${checkOut}:00`);
        const diffMs = end.getTime() - start.getTime();
        const hours = Math.floor(diffMs / 1000 / 60 / 60);
        const minutes = Math.floor((diffMs / 1000 / 60) % 60);
        return `${hours}h ${minutes}m`;
    };

    const progressPercent = (() => {
        if (!data?.checkIn || !data?.checkOut) return 0;
        const start = new Date(`1970-01-01T${data.checkIn}:00`).getTime();
        const end = new Date(`1970-01-01T${data.checkOut}:00`).getTime();
        const fullDay = 8 * 60 * 60 * 1000;
        return Math.min(100, Math.floor(((end - start) / fullDay) * 100));
    })();

    return (
        <IndexLayout userName={userName}>
            <div className="progress_main">
                <h2>📈 Today’s Progress</h2>

                {isLoading ? (
                    <p>Loading...</p>
                ) : data ? (
                    <div className="progress-card">
                        <p><strong>📅 Date:</strong> {data.date}</p>
                        <p><strong>🟢 Check-in:</strong> {data.checkIn || "Not yet checked in"}</p>
                        <p><strong>🔴 Check-out:</strong> {data.checkOut || "Not yet checked out"}</p>

                        {data.checkIn && data.checkOut && (
                            <p><strong>⏳ Duration:</strong> {calculateDuration(data.checkIn, data.checkOut)}</p>
                        )}

                        <div className="progress-bar">
                            <div className="progress-fill" style={{ width: `${progressPercent}%` }} />
                        </div>

                        <p><strong>🔥 Streak:</strong> {data.streak} day{data.streak !== 1 ? "s" : ""}</p>

                        <div className="check-buttons">
                            <button
                                onClick={() => handleCheck("checkin")}
                                disabled={!!data.checkIn}
                            >
                                {data.checkIn ? "Checked In" : "Check In"}
                            </button>

                            <button
                                onClick={() => handleCheck("checkout")}
                                disabled={!data.checkIn || !!data.checkOut}
                            >
                                {data.checkOut ? "Checked Out" : "Check Out"}
                            </button>
                        </div>

                        <p className="tip">💡 Tip: Don’t forget to check out before 5:30 PM!</p>
                    </div>
                ) : (
                    <p>No attendance record found for today.</p>
                )}
            </div>
        </IndexLayout>
    );
};
