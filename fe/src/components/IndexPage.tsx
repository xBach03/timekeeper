import React, { useEffect, useState } from "react";
import "../assets/css/IndexPage.css";
import { useNavigate } from "react-router-dom";
import IndexLayout from "./IndexLayout";
import {
    PieChart, Pie, Cell, Tooltip,
    BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer
} from 'recharts';


interface DashboardData {
    shift: string;
    breakTime: string;
    leaveRequests: string;
    notifications: string[];
}

interface LeaveStatusData {
    name: string;
    value: number;
}

interface WeeklyAttendanceData {
    date: string;   // e.g., "Mon", "Tue"
    hours: number;
    key: string;
}

export const IndexPage: React.FC = () => {
    const navigate = useNavigate();
    const [userName, setUserName] = useState<string>("Loading...");
    const [dashboardData, setDashboardData] = useState<DashboardData>({
        shift: "",
        breakTime: "",
        leaveRequests: "",
        notifications: []
    });

    const getCurrentWeekLabels = (): WeeklyAttendanceData[] => {
        const today = new Date();
        const day = today.getDay();
        const monday = new Date(today);
        monday.setDate(today.getDate() - ((day + 6) % 7));

        const weekdays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'];

        return weekdays.map((dayLabel, i) => {
            const d = new Date(monday);
            d.setDate(monday.getDate() + i);
            return {
                date: `${dayLabel} ${d.getDate()}`,  // used for label
                key: dayLabel,                       // used for lookup
                hours: 0
            } as any;
        });
    };

    const WEEK_DAYS: WeeklyAttendanceData[] = getCurrentWeekLabels();

    const COLORS = ['#0088FE', '#00C49F', '#FF8042'];

    const [leaveStatusData, setLeaveStatusData] = useState<LeaveStatusData[]>([]);
    const [attendanceData, setAttendanceData] = useState<WeeklyAttendanceData[]>([]);


    useEffect(() => {
        const name = localStorage.getItem("userName") || "Unknown";
        setUserName(name);
        // Dashboard
        fetch(`http://localhost:8080/api/index/dashboard?name=${name}`)
            .then(res => res.json())
            .then(data => setDashboardData(data))
            .catch(() => setDashboardData({
                shift: "N/A", breakTime: "N/A", leaveRequests: "N/A", notifications: []
            }));
        // Leave status
        // Leave status
        fetch(`http://localhost:8080/api/index/leave-status?name=${name}`)
            .then(res => res.json())
            .then(data => {
                if (Array.isArray(data)) {
                    setLeaveStatusData(data);
                } else {
                    // fallback in case of bad API shape
                    setLeaveStatusData([
                        { name: "Approved", value: 2 },
                        { name: "Pending", value: 1 },
                        { name: "Rejected", value: 1 }
                    ]);
                }
            })
            .catch(() => setLeaveStatusData([
                { name: "Approved", value: 2 },
                { name: "Pending", value: 1 },
                { name: "Rejected", value: 1 }
            ]));

        // Weekly attendance
        fetch(`http://localhost:8080/api/index/weekly-attendance?name=${name}`)
            .then(res => res.json())
            .then(data => {
                if (Array.isArray(data)) {
                    const parseDay = (label: string): string => {
                        const match = label.match(/^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)/i);
                        return match ? match[0].charAt(0).toUpperCase() + match[0].slice(1).toLowerCase() : '';
                    };

                    const rawMap = new Map(
                        data.map((d: WeeklyAttendanceData) => {
                            const day = parseDay(d.date);
                            return [day, { hours: d.hours, label: d.date }];
                        })
                    );

                    const normalized = WEEK_DAYS.map(day => {
                        const entry = rawMap.get(day.key); // use 'Mon', 'Tue', etc.
                        return {
                            key: day.key,
                            date: day.date,                 // keep label like 'Mon 17'
                            hours: entry?.hours || 0
                        };
                    });


                    setAttendanceData(normalized);
                } else {
                    setAttendanceData(WEEK_DAYS.map(d => ({
                        key: d.key,
                        date: `${d.date} (N/A)`,
                        hours: 0
                    })));
                }
            })
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("isLoggedIn");
        localStorage.removeItem("userName");
        navigate("/");
    };

    return (
        <IndexLayout userName={userName}>
            <h2 className="dashboardTitle">Today’s Schedule</h2>
            <section className="cards">
                <div className="card">
                    <h3>Upcoming Shift</h3>
                    <p>{dashboardData.shift}</p>
                    <p className="sub">Break: {dashboardData.breakTime}</p>
                </div>

                <div className="card">
                    <h3>Leave Requests</h3>
                    <p>{dashboardData.leaveRequests}</p>
                </div>

                <div className="card">
                    <h3>Notifications</h3>
                    <ul>
                        {dashboardData.notifications.map((n, i) => (
                            <li key={i}>{n}</li>
                        ))}
                    </ul>
                </div>
            </section>
            <section className="charts">
                <div className="chart-container">
                    <h3>Weekly Attendance</h3>
                    <ResponsiveContainer width="100%" height={250}>
                        <BarChart data={Array.isArray(attendanceData) ? attendanceData : []}>
                            <CartesianGrid strokeDasharray="3 3"/>
                            <XAxis dataKey="date" tick={{fontSize: 14}}/>
                            <YAxis tick={{fontSize: 14}}/>
                            <Tooltip contentStyle={{fontSize: '14px'}}/>
                            <Bar dataKey="hours" fill="#8884d8">
                                {/* Optional: Add bar labels if needed */}
                                {/* <LabelList dataKey="hours" position="top" style={{ fontSize: 12 }} /> */}
                            </Bar>
                        </BarChart>
                    </ResponsiveContainer>
                </div>

                <div className="chart-container pie">
                    <div className="pie-chart-wrapper">
                        <h3>Leave Request Status</h3>
                        <ResponsiveContainer width="100%" height={250}>
                            <PieChart>
                                <Pie
                                    data={leaveStatusData}
                                    dataKey="value"
                                    nameKey="name"
                                    cx="50%"
                                    cy="50%"
                                    outerRadius={80}
                                    label
                                >
                                    {leaveStatusData.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]}/>
                                    ))}
                                </Pie>
                                <Tooltip/>
                            </PieChart>
                        </ResponsiveContainer>
                    </div>

                    <div className="pie-legend">
                        {leaveStatusData.map((entry, index) => (
                            <div key={entry.name} className="pie-legend-item">
                                <div
                                    className="pie-legend-color"
                                    style={{backgroundColor: COLORS[index % COLORS.length]}}
                                />
                                <span>{entry.name}</span>
                            </div>
                        ))}
                    </div>
                </div>
            </section>
        </IndexLayout>
        // <div className="layout">
        //     <aside className="sidebar">
        //         <h1 className="logo">Workday</h1>
        //         <nav className="nav">
        //             <a href="/index">Dashboard</a>
        //             <a href="#">Shifts</a>
        //             <a href="/request_leave">Request Leave</a>
        //             <a href="#">Settings</a>
        //         </nav>
        //     </aside>
        //
        //     <main className="main">
        //         <header className="header">
        //             <h2>Today’s Schedule</h2>
        //             <div className="user">
        //                 <span className="user-icon">👤</span>
        //                 <span>{userName}</span>
        //                 <button className="logout-button" onClick={handleLogout}>Logout</button>
        //             </div>
        //         </header>
    // <section className="cards">
    //     <div className="card">
    //         <h3>Upcoming Shift</h3>
    //         <p>{dashboardData.shift}</p>
    //         <p className="sub">Break: {dashboardData.breakTime}</p>
    //     </div>
    //
    //     <div className="card">
    //         <h3>Leave Requests</h3>
    //         <p>{dashboardData.leaveRequests}</p>
    //     </div>
    //
    //     <div className="card">
    //         <h3>Notifications</h3>
    //         <ul>
    //             {dashboardData.notifications.map((n, i) => (
    //                 <li key={i}>{n}</li>
    //             ))}
    //         </ul>
    //     </div>
    // </section>
    // <section className="charts">
    //     <div className="chart-container">
    //         <h3>Weekly Attendance</h3>
    //         <ResponsiveContainer width="100%" height={250}>
    //             <BarChart data={Array.isArray(attendanceData) ? attendanceData : []}>
    //                 <CartesianGrid strokeDasharray="3 3" />
    //                 <XAxis dataKey="date" tick={{ fontSize: 14 }} />
    //                 <YAxis tick={{ fontSize: 14 }} />
    //                 <Tooltip contentStyle={{ fontSize: '14px' }} />
    //                 <Bar dataKey="hours" fill="#8884d8">
    //                     {/* Optional: Add bar labels if needed */}
    //                     {/* <LabelList dataKey="hours" position="top" style={{ fontSize: 12 }} /> */}
    //                 </Bar>
    //             </BarChart>
    //         </ResponsiveContainer>
    //     </div>
    //
    //     <div className="chart-container pie">
    //         <div className="pie-chart-wrapper">
    //             <h3>Leave Request Status</h3>
    //             <ResponsiveContainer width="100%" height={250}>
    //                 <PieChart>
    //                     <Pie
    //                         data={leaveStatusData}
    //                         dataKey="value"
    //                         nameKey="name"
    //                         cx="50%"
    //                         cy="50%"
    //                         outerRadius={80}
    //                         label
    //                     >
    //                         {leaveStatusData.map((entry, index) => (
    //                             <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]}/>
    //                         ))}
    //                     </Pie>
    //                     <Tooltip/>
    //                 </PieChart>
    //             </ResponsiveContainer>
    //         </div>
    //
    //         <div className="pie-legend">
    //             {leaveStatusData.map((entry, index) => (
    //                 <div key={entry.name} className="pie-legend-item">
    //                     <div
    //                         className="pie-legend-color"
    //                         style={{backgroundColor: COLORS[index % COLORS.length]}}
    //                     />
    //                     <span>{entry.name}</span>
    //                 </div>
    //             ))}
    //         </div>
    //     </div>
    // </section>

        //     </main>
        // </div>
    );
};
