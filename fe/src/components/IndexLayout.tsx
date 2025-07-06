import React from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/IndexPage.css";

interface Props {
    userName: string;
    children: React.ReactNode;
}

const IndexLayout: React.FC<Props> = ({ userName, children }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        const name = localStorage.getItem("userName") || "Unknown"
        fetch(`http://localhost:8080/api/employee/logout?name=${name}`)
            .then(res => {
                if (res.ok) {
                    localStorage.removeItem("userName");
                } else {
                    alert("Logout failed");
                }
            })
            .catch(() => alert("Network error during logout"));

        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("userName");
        navigate("/");
    };

    return (
        <div className="layout">
            <aside className="sidebar">
                <h1 className="logo">Workday 💼</h1>
                <nav className="nav">
                    <a href="/index">Dashboard 📊</a>
                    <a href="/shifts">Shifts 🗓️</a>
                    <a href="/request_leave">Request Leave 📃</a>
                    <a href="/today_progress">Today Progress 📈</a>
                    <a href="/payroll">Payroll 📋</a>
                    <a href="/ot_logging">OT Logging ⏳</a>
                    <a href="#">Settings ⚙️</a>
                </nav>
            </aside>

            <main className="main">
                <header className="header">
                    <div className="user">
                        <span className="user-icon">👤</span>
                        <span>{userName}</span>
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                    </div>
                </header>
                {children}
            </main>
        </div>
    );
};




export default IndexLayout;
