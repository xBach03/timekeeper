import npReact, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/LoginPage.css";

export const LoginPage: React.FC = () => {
    const handleBack = () => {
        navigate("/"); //
    };
    const navigate = useNavigate();
    const [dateTime, setDateTime] = useState(new Date().toLocaleString());
    const [status, setStatus] = useState("");
    const [countdown, setCountdown] = useState<number | null>(null);
    const url = "http://localhost:8080";

    useEffect(() => {
        const timer = setInterval(() => {
            setDateTime(new Date().toLocaleString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);

    useEffect(() => {
        if (countdown === null) return;

        if (countdown === 0) {
            localStorage.setItem("isLoggedIn", "true");
            navigate("/index");
        }

        const timer = setTimeout(() => {
            setCountdown(prev => (prev !== null ? prev - 1 : null));
        }, 1000);

        return () => clearTimeout(timer);
    }, [countdown, navigate]);

    const handleRecognize = async () => {
        try {
            const response = await fetch(url + "/api/recognizer/recognize");
            const name = await response.text();
            if (response.ok && name) {
                const login = await fetch(url + "/api/employee/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        name: name.trim(),
                    })
                });
                const loginData = await login.json();
                if (login.ok && loginData.name) {
                    localStorage.setItem("isLoggedIn", "true");
                    localStorage.setItem("userName", loginData.name); // Save name as token
                    setStatus(`Recognized: ${loginData.name}`);
                    setCountdown(5);
                } else {
                    setStatus(`Not recognized: ${name}`);
                }
            } else {
                setStatus(`Not recognized: ${name}`);
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
            <button className="recognize-btn" onClick={handleRecognize}>Open Recognizer</button>
            {status && (
                <p className="status-message" style={{whiteSpace: "pre-line"}}>
                    {status}
                    {countdown !== null && countdown > 0 && `\nRedirecting in ${countdown}...`}
                </p>
            )}
            <div className="back-button-container">
                <button onClick={handleBack} className="back-button">← Back</button>
            </div>
        </div>
    );
};
