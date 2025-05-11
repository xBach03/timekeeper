import React, { useState } from "react";
import "../assets/css/RegisterPage.css";
import { useNavigate } from "react-router-dom";

export const RegisterPage: React.FC = () => {
    const navigate = useNavigate();

    const handleBack = () => {
        navigate("/"); //
    };
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        dob: "",
        phone: "",
        title: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const dto = {
            name: formData.name,
            email: formData.email,
            dateOfBirth: formData.dob, // Make sure dob is in ISO format
            phoneNumber: formData.phone,
            title: formData.title
        };

        try {
            const response = await fetch("http://localhost:8080/api/employee/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dto)
            });

            if (response.ok) {
                alert("Registration successful!");
            } else {
                alert("Failed to register.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Error submitting registration.");
        }
    };

    return (
        <div className="register-container">
            <h2>Employee Registration</h2>
            <form onSubmit={handleSubmit} className="register-form">
                <label>
                    Full Name:
                    <input type="text" name="name" value={formData.name} onChange={handleChange} required/>
                </label>

                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required/>
                </label>

                <label>
                    Date of Birth:
                    <input type="date" name="dob" value={formData.dob} onChange={handleChange} required/>
                </label>

                <label>
                    Phone Number:
                    <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required/>
                </label>

                <label>
                    Job Title:
                    <input type="text" name="title" value={formData.title} onChange={handleChange} required/>
                </label>

                <button type="submit">Register</button>
            </form>
            <div className="back-button-container">
                <button onClick={handleBack} className="back-button">← Back</button>
            </div>
        </div>
    );
};
