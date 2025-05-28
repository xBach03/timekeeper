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
        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("userName");
        navigate("/");
    };

    return (
        <div className="layout">
            <aside className="sidebar">
                <h1 className="logo">Workday</h1>
                <nav className="nav">
                    <a href="/index">Dashboard</a>
                    <a href="#">Shifts</a>
                    <a href="/request_leave">Request Leave</a>
                    <a href="#">Settings</a>
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
