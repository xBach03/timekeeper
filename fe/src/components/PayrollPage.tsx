import React, { useEffect, useState } from "react";
import IndexLayout from "./IndexLayout";
import "../assets/css/PayrollPage.css";


interface PayrollRecord {
    date: string;
    hoursWorked: number;
    hourlyRate: number;
    totalPay: number;
    bonus: number;
    tax: number;
}


const Payroll: React.FC = () => {
    const [payrollData, setPayrollData] = useState<PayrollRecord[]>([
        { date: "2024-06-10", hoursWorked: 8, hourlyRate: 15, totalPay: 125, bonus: 10, tax: 5 },
        { date: "2024-06-11", hoursWorked: 7, hourlyRate: 15, totalPay: 112, bonus: 15, tax: 8 },
        { date: "2024-06-12", hoursWorked: 5, hourlyRate: 15, totalPay: 77, bonus: 5, tax: 3 },
        { date: "2024-06-13", hoursWorked: 2, hourlyRate: 15, totalPay: 29, bonus: 0, tax: 1 },
        { date: "2024-06-14", hoursWorked: 3, hourlyRate: 15, totalPay: 45, bonus: 2, tax: 2 }
    ]);



    useEffect(() => {
        const name = localStorage.getItem("userName") || "Unknown";
        fetch(`http://localhost:8080/api/employee/payroll?name=${name}`)
            .then(res => res.json())
            .then(data => {
                if (Array.isArray(data)) {
                    setPayrollData(data);
                } else {
                    console.warn("Unexpected payroll data format", data);
                }
            })
            .catch(err => {
                console.error("Failed to fetch payroll data:", err);
            });
    }, []);


    return (
        <IndexLayout userName={localStorage.getItem("userName") || "Unknown"}>
            <h2 className="dashboardTitle">Payroll</h2>
            <div className="card">
                <table style={{width: "100%", borderCollapse: "collapse"}}>
                    <thead>
                    <tr style={{textAlign: "left", borderBottom: "1px solid #ccc"}}>
                        <th>Date</th>
                        <th>Hours Worked</th>
                        <th>Hourly Rate</th>
                        <th>Bonus</th>
                        <th>Tax</th>
                        <th>Total Pay</th>
                    </tr>
                    </thead>
                    <tbody>
                    {payrollData.map((record, index) => (
                        <tr key={index}>
                            <td>{record.date}</td>
                            <td>{record.hoursWorked}</td>
                            <td>${record.hourlyRate.toFixed(2)}</td>
                            <td>${record.bonus.toFixed(2)}</td>
                            <td>${record.tax.toFixed(2)}</td>
                            <td>${record.totalPay.toFixed(2)}</td>
                        </tr>
                    ))}

                    {/* ✅ Totals Row */}
                    <tr style={{fontWeight: "bold", borderTop: "2px solid #ccc"}}>
                        <td>Total</td>
                        <td>{payrollData.reduce((sum, r) => sum + r.hoursWorked, 0)}</td>
                        <td></td>
                        <td>${payrollData.reduce((sum, r) => sum + r.bonus, 0).toFixed(2)}</td>
                        <td>${payrollData.reduce((sum, r) => sum + r.tax, 0).toFixed(2)}</td>
                        <td>${payrollData.reduce((sum, r) => sum + r.totalPay, 0).toFixed(2)}</td>
                    </tr>
                    </tbody>

                </table>
            </div>
        </IndexLayout>
    );
};

export default Payroll;
