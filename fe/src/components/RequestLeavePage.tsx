import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import IndexLayout from "./IndexLayout";

import "../assets/css/RequestLeavePage.css";

export const RequestLeavePage: React.FC = () => {
    const navigate = useNavigate();
    const [selectedDate, setSelectedDate] = useState<Date | null>(new Date());
    const [startHour, setStartHour] = useState<string>("08:30");
    const [endHour, setEndHour] = useState<string>("17:30");
    const [category, setCategory] = useState<string>("Personal");
    const [leaveDates, setLeaveDates] = useState<Date[]>([]);
    const userName = localStorage.getItem("userName") || "Unknown";
    const normalizeDate = (date: Date) => {
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    };

    useEffect(() => {
        fetch(`http://localhost:8080/api/leave/upcoming?name=${userName}`)
            .then(res => res.json())
            .then((data: string[]) => {
                const parsedDates = data.map(dateStr => normalizeDate(new Date(dateStr)));
                setLeaveDates(parsedDates);
            })
            .catch(() => {
                const fallback = ["2025-06-11", "2025-06-10"];
                const parsedDates = fallback.map(dateStr => normalizeDate(new Date(dateStr)));
                setLeaveDates(parsedDates);
                console.warn("Failed to fetch leave dates");
            });
    }, [userName]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

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
                    navigate("/index");
                } else {
                    alert("Failed to submit leave request.");
                }
            })
            .catch(() => alert("Error connecting to server."));
    };

    return (
        <IndexLayout userName={userName}>
            <div className="leave_main" style={{padding: "30px"}}>
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
                            />
                        </label>

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

                        <button type="submit">Submit Request</button>
                    </form>

                    {/* Calendar on the right */}
                    <div className="leave-calendar">
                        <h4>This month Dayoffs</h4>
                        <DatePicker
                            inline
                            highlightDates={leaveDates}
                            dayClassName={(date) => {
                                const normalized = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                                return leaveDates.some(d =>
                                    d.getTime() === normalized.getTime()
                                ) ? "highlight" : "";
                            }}
                        />
                    </div>
                </div>
            </div>
        </IndexLayout>
    );
};
