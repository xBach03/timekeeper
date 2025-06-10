import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import IndexLayout from "./IndexLayout";
import "../assets/css/RequestLeavePage.css";

interface LeaveDay {
    date: Date;
    fullDay: boolean;
    hours: string;
    hoursNum: number;
}

export const RequestLeavePage: React.FC = () => {
    const navigate = useNavigate();
    const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
    const [startHour, setStartHour] = useState<string>("08:30");
    const [endHour, setEndHour] = useState<string>("17:30");
    const [category, setCategory] = useState<string>("Personal");
    const [leaveDates, setLeaveDates] = useState<LeaveDay[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const userName = localStorage.getItem("userName") || "Unknown";

    const normalizeDate = (date: Date) => new Date(date.getFullYear(), date.getMonth(), date.getDate());

    useEffect(() => {
        fetch(`http://localhost:8080/api/leave/upcoming?name=${userName}`)
            .then(res => res.json())
            .then((data: { date: string; fullDay: boolean; hours: string, hoursNum: number }[]) => {
                const parsed = data.map(d => ({
                    date: normalizeDate(new Date(d.date)),
                    fullDay: d.fullDay,
                    hours: d.hours,
                    hoursNum: d.hoursNum
                }));
                setLeaveDates(parsed);
            })
            .catch(() => {
                const fallback = [
                    { date: "2025-06-11", fullDay: true, hours: "8:30 - 17:30", hoursNum: 8.0 },
                    { date: "2025-06-10", fullDay: false, hours: "8:30 - 17:30", hoursNum: 8.0 }
                ];
                const parsed = fallback.map(d => ({
                    date: normalizeDate(new Date(d.date)),
                    fullDay: d.fullDay,
                    hours: d.hours,
                    hoursNum: d.hoursNum
                }));
                setLeaveDates(parsed);
                console.warn("Failed to fetch leave dates");
            })
            .finally(() => setIsLoading(false));
    }, [userName]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (startHour >= endHour) {
            alert("End time must be after start time.");
            return;
        }

        const requestData = {
            date: selectedDate?.toISOString().split("T")[0],
            startHour,
            endHour,
            category
        };

        fetch(`http://localhost:8080/api/leave/request?name=${userName}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestData)
        })
            .then(res => {
                if (res.ok) {
                    alert("Leave request submitted!");
                } else {
                    alert("Failed to submit leave request.");
                }
            })
            .catch(() => alert("Error connecting to server."));
    };

    const selectedWarning = (() => {
        if (!selectedDate) return null;
        const normalized = normalizeDate(selectedDate);
        const match = leaveDates.find(ld => ld.date.getTime() === normalized.getTime());
        if (match && !match.fullDay) {
            return (
                <p className="leave-warning">
                    ⚠️ You already have a partial leave on this day ({match.hours}).
                </p>
            );
        }
        return null;
    })();


    return (
        <IndexLayout userName={userName}>
            <div className="leave_main">
                <h2>Request Leave</h2>
                <div className="leave-container">
                    <form className="leave-form" onSubmit={handleSubmit}>
                        <label>
                            Date:
                            <DatePicker
                                selected={selectedDate}
                                onChange={(date) => setSelectedDate(date)}
                                dateFormat="yyyy-MM-dd"
                                className="datepicker"
                                required
                                minDate={new Date()}
                                filterDate={(date) => {
                                    const normalized = normalizeDate(date);
                                    const isFullDayDisabled = leaveDates.some(ld =>
                                        ld.date.getTime() === normalized.getTime() &&
                                        ld.fullDay &&
                                        startHour === "08:30" &&
                                        endHour === "17:30"
                                    );
                                    return !isFullDayDisabled;
                                }}
                            />
                        </label>

                        {selectedWarning}

                        <label>
                            Start Hour:
                            <input
                                type="time"
                                value={startHour}
                                onChange={(e) => setStartHour(e.target.value)}
                                required
                            />
                        </label>

                        <label>
                            End Hour:
                            <input
                                type="time"
                                value={endHour}
                                onChange={(e) => setEndHour(e.target.value)}
                                required
                            />
                        </label>

                        <label>
                            Category:
                            <select value={category} onChange={(e) => setCategory(e.target.value)}>
                                <option value="Personal">Personal</option>
                                <option value="Sick">Sick Leave</option>
                                <option value="Vacation">Vacation</option>
                                <option value="Other">Other</option>
                            </select>
                        </label>

                        <p className="summary">
                            You are requesting <strong>{category}</strong> leave on <strong>{selectedDate?.toDateString()}</strong> from <strong>{startHour}</strong> to <strong>{endHour}</strong>.
                        </p>

                        <button type="submit">Submit Request</button>
                    </form>

                    <div className="leave-calendar">
                        <h4>This Month's Dayoffs</h4>
                        {isLoading ? (
                            <p>Loading calendar...</p>
                        ) : (
                            <DatePicker
                                inline
                                highlightDates={leaveDates.map(ld => ld.date)}
                                dayClassName={(date) => {
                                    const normalized = normalizeDate(date);
                                    const match = leaveDates.find(ld => ld.date.getTime() === normalized.getTime());

                                    if (!match) return "";

                                    if (match.hoursNum >= 7) return "density-high";
                                    if (match.hoursNum >= 4) return "density-medium";
                                    return "density-low";
                                }}
                            />
                        )}
                    </div>
                </div>
            </div>
        </IndexLayout>
    );
};
