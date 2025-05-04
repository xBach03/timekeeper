import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { NavigationPage } from "./components/NavigationPage";
import { IndexPage } from "./components/IndexPage";
import { RegisterPage } from "./components/RegisterPage"

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={<NavigationPage />} />
              <Route path="/index" element={<IndexPage />} />
              <Route path="/register" element={<RegisterPage />} />
          </Routes>
      </Router>
  );
}

export default App;
