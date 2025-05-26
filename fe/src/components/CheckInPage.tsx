import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../assets/css/LoginPage.css";

export const CheckInPage: React.FC = () => {
    const url = "http://localhost:8080"
    const navigate = useNavigate();
    const [dateTime, setDateTime] = useState(new Date().toLocaleString());
    const [status, setStatus] = useState("");

    const handleBack = () => {
        navigate("/"); //
    };

    useEffect(() => {
        const timer = setInterval(() => {
            setDateTime(new Date().toLocaleString());
        }, 1000);
        return () => clearInterval(timer);
    }, []);

    const handleRecognize = async () => {
        try {
            const response = await fetch(url + "/api/recognizer/recognize");
            const name = await response.text();

            if (response.ok && name && name.trim().length > 0) {
                const currentTime = new Date().toISOString();

                const checkin = await fetch(url + "/api/attendance/checkin", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        name: name.trim(),
                        time: currentTime
                    })
                });

                if (checkin.ok) {
                    setStatus("Check in success: " + name + "\nTime: " + new Date(currentTime).toLocaleString())
                } else {
                    const errorText = await checkin.text();
                    setStatus("Check-in failed: " + errorText);
                }
            } else {
                setStatus("Not recognized: " + name);
            }
        } catch (error) {
            console.error(error);
            setStatus("Error calling recognition API");
        }
    };

    return (
        <div className="login-container">
            <h2>Check-in</h2>
            <p className="datetime">{dateTime}</p>
            <button className="recognize-btn" onClick={handleRecognize}>Open Recognizer</button>
            {status && (
                <p className="status-message" style={{whiteSpace: "pre-line"}}>
                    {status}
                </p>
            )}
            <div className="back-button-container">
                <button onClick={handleBack} className="back-button">← Back</button>
            </div>
        </div>
    );
};
