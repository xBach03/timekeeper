import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../assets/css/LoginPage.css";

export const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [credentials, setCredentials] = useState({ username: "", password: "" });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleLogin = (e: React.FormEvent) => {
        e.preventDefault();

        // Dummy auth check (replace with real one)
        if (credentials.username === "admin" && credentials.password === "1234") {
            localStorage.setItem("isLoggedIn", "true");

            const from = (location.state as any)?.from?.pathname || "/index";
            navigate(from); // Redirect to where the user came from
        } else {
            alert("Invalid credentials");
        }
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            <form onSubmit={handleLogin} className="login-form">
                <input
                    name="username"
                    type="text"
                    placeholder="Username"
                    value={credentials.username}
                    onChange={handleChange}
                    required
                />
                <input
                    name="password"
                    type="password"
                    placeholder="Password"
                    value={credentials.password}
                    onChange={handleChange}
                    required
                />
                <button type="submit">Login</button>
            </form>
        </div>
    );
};
