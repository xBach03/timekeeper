import React, { useState } from "react";
import "../assets/css/RegisterPage.css";

export const RegisterPage: React.FC = () => {
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        dob: "",
        phone: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Submitted Data:", formData);
        // You can send this to an API here
        alert("Registration submitted!");
    };

    return (
        <div className="register-container">
            <h2>Employee Registration</h2>
            <form onSubmit={handleSubmit} className="register-form">
                <label>
                    Full Name:
                    <input type="text" name="name" value={formData.name} onChange={handleChange} required />
                </label>

                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </label>

                <label>
                    Date of Birth:
                    <input type="date" name="dob" value={formData.dob} onChange={handleChange} required />
                </label>

                <label>
                    Phone Number:
                    <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required />
                </label>

                <button type="submit">Register</button>
            </form>
        </div>
    );
};
