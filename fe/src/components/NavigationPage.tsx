import React from "react";
import { Link } from "react-router-dom";
import "../assets/css/NavigationPage.css";

export const NavigationPage: React.FC = () => {
    return (
        <div className="nav-page">
            <h1 className="nav-title">Employee Portal</h1>
            <div className="nav-options">
                <Link to="/register" className="nav-card">
                    <span className="nav-icon">✅</span>
                    <h2>Register</h2>
                </Link>
                <Link to="/check-in" className="nav-card">
                    <span className="nav-icon">🕒</span>
                    <h2>Check-In</h2>
                </Link>
                <Link to="/leave-request" className="nav-card">
                    <span className="nav-icon">🗓️</span>
                    <h2>Request Leave</h2>
                </Link>
                <Link to="/index" className="nav-card">
                    <span className="nav-icon">📋</span>
                    <h2>Index Page</h2>
                </Link>
            </div>
        </div>
    );
};
