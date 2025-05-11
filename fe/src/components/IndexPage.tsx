import React from "react";
import "../assets/css/IndexPage.css";
import { useNavigate } from "react-router-dom";

export const IndexPage: React.FC = () => {
    const navigate = useNavigate();
    const handleLogout = () => {
        localStorage.removeItem("isLoggedIn");
        navigate("/"); // Go back to navigation page
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
                        <span>Admin</span>
                    </div>
                    <div className="user">
                        <span className="user-icon">👤</span>
                        <span>Admin</span>
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                    </div>
                </header>

                <section className="cards">
                    <div className="card">
                        <h3>Upcoming Shift</h3>
                        <p>9:00 AM – 5:00 PM</p>
                        <p className="sub">Break: 1 hour</p>
                    </div>

                    <div className="card">
                        <h3>Leave Requests</h3>
                        <p>2 pending approvals</p>
                    </div>

                    <div className="card">
                        <h3>Notifications</h3>
                        <ul>
                            <li>New policy update</li>
                            <li>Shift change approved</li>
                        </ul>
                    </div>
                </section>
            </main>
        </div>
    );
};
