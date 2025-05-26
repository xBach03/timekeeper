import React, { useEffect, useState } from "react";
import "../assets/css/IndexPage.css";
import { useNavigate } from "react-router-dom";

interface DashboardData {
    shift: string;
    breakTime: string;
    leaveRequests: string;
    notifications: string[];
}

export const IndexPage: React.FC = () => {
    const navigate = useNavigate();
    const [userName, setUserName] = useState<string>("Loading...");
    const [dashboardData, setDashboardData] = useState<DashboardData>({
        shift: "",
        breakTime: "",
        leaveRequests: "",
        notifications: []
    });

    useEffect(() => {
        const name = localStorage.getItem("userName") || "Unknown";
        setUserName(name);

        fetch(`http://localhost:8080/api/employee/dashboard?name=${name}`)
            .then(res => res.json())
            .then(data => setDashboardData(data))
            .catch(() => setDashboardData({
                shift: "N/A", breakTime: "N/A", leaveRequests: "N/A", notifications: []
            }));
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("userName");
        navigate("/");
    };

    return (
        <div className="layout">
            <aside className="sidebar">
                <h1 className="logo">Workday</h1>
                <nav className="nav">
                    <a href="#">Dashboard</a>
                    <a href="#">Shifts</a>
                    <a href="#">Requests</a>
                    <a href="#">Settings</a>
                </nav>
            </aside>

            <main className="main">
                <header className="header">
                    <h2>Today’s Schedule</h2>
                    <div className="user">
                        <span className="user-icon">👤</span>
                        <span>{userName}</span>
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                    </div>
                </header>

                <section className="cards">
                    <div className="card">
                        <h3>Upcoming Shift</h3>
                        <p>{dashboardData.shift}</p>
                        <p className="sub">Break: {dashboardData.breakTime}</p>
                    </div>

                    <div className="card">
                        <h3>Leave Requests</h3>
                        <p>{dashboardData.leaveRequests}</p>
                    </div>

                    <div className="card">
                        <h3>Notifications</h3>
                        <ul>
                            {dashboardData.notifications.map((n, i) => (
                                <li key={i}>{n}</li>
                            ))}
                        </ul>
                    </div>
                </section>
            </main>
        </div>
    );
};
