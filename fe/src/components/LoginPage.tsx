import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/LoginPage.css";

export const LoginPage: React.FC = () => {
    const navigate = useNavigate();
    const [dateTime, setDateTime] = useState(new Date().toLocaleString());
    const [status, setStatus] = useState("");

    useEffect(() => {
        const timer = setInterval(() => {
            setDateTime(new Date().toLocaleString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);

    const handleRecognize = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/recognizer/recognize");
            const text = await response.text();
            if (response.ok && text && text.toLowerCase().includes("recognized")) {
                localStorage.setItem("isLoggedIn", "true");
                navigate("/index");
            } else {
                setStatus(`Not recognized: ${text}`);
            }
        } catch (error) {
            console.error(error);
            setStatus("Error calling recognition API");
        }
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            <p className="datetime">{dateTime}</p>
            <button className="recognize-btn" onClick={handleRecognize}>Recognize Face</button>
            {status && <p className="status-message">{status}</p>}
        </div>
    );
};
