import React, { JSX } from 'react';
import './App.css';
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
    useLocation
} from "react-router-dom";

import { NavigationPage } from "./components/NavigationPage";
import { IndexPage } from "./components/IndexPage";
import { RegisterPage } from "./components/RegisterPage";
import { LoginPage } from "./components/LoginPage";
import { CheckInPage } from "./components/CheckInPage";
import { CheckoutPage } from "./components/CheckoutPage";
import { RequestLeavePage } from "./components/RequestLeavePage";
import ShiftsPage from "./components/ShiftsPage";

function RequireAuth({ children }: { children: JSX.Element }) {
    const location = useLocation();
    const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";

    return isLoggedIn ? children : <Navigate to="/login" state={{ from: location }} replace />;
}

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<NavigationPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/checkin" element={<CheckInPage />} />
                <Route path="/checkout" element={<CheckoutPage />} />
                <Route path="/shifts" element={<ShiftsPage/>} />
                <Route
                    path="/index"
                    element={
                        <RequireAuth>
                            <IndexPage />
                        </RequireAuth>
                    }
                />
                <Route path="/request_leave" element={<RequestLeavePage/>} />
            </Routes>
        </Router>
    );
}

export default App;
