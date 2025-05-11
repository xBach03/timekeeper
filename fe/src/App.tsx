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
                <Route
                    path="/index"
                    element={
                        <RequireAuth>
                            <IndexPage />
                        </RequireAuth>
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;
