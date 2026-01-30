import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Home from "./pages/Home/Home";
import Login from "./pages/Login/Login";
import Main from "./pages/Main/Main";

import Revenue from "./components/MainComponents/Report/Revenue";
import RoomBooking from "./components/MainComponents/Report/RoomBooking";
import Verification from "./pages/Login/Verification";
import { useState, useEffect } from "react";
import axiosClient from "./api/axiosClient";

const App = () => {
  const [session, setSession] = useState();
  const [loading, setLoading] = useState(true); // Thêm trạng thái loading

  // Kiểm tra session từ backend khi ứng dụng khởi động
  useEffect(() => {
    const checkSession = async () => {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          setSession(null);
          setLoading(false);
          return;
        }

        // Gọi API /api/v1/users/me để kiểm tra session
        const response = await axiosClient.get("/api/v1/users/me");
        if (response && response.data) {
          setSession({
            Username: response.data.username,
            Role: response.data.role,
            HotelId: 1,
          });
        } else {
          setSession(null);
        }
      } catch (error) {
        setSession(null);
      } finally {
        setLoading(false); // Kết thúc loading
      }
    };
    checkSession();
  }, []);

  const ProtectedRoute = ({ children }) => {
    if (loading) {
      return <div>Loading...</div>; // Hiển thị loading khi đang kiểm tra session
    }
    if (!session) {
      return <Navigate to="/login" replace />;
    }
    return children;
  };

  return (
    <div className="app">
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login session={session} setSession={setSession} />} />
        <Route path="/main" element={
          <ProtectedRoute>
            <Main session={session} setSession={setSession} />
          </ProtectedRoute>} />
        {/*           <Route path="/room" element={<Main />} /> */}
        <Route path="/revenue" element={
          <ProtectedRoute>
            <Revenue />
          </ProtectedRoute>} />
        <Route path="/roombooking-report" element={
          <ProtectedRoute>
            <RoomBooking />
          </ProtectedRoute>
        } />
        <Route path="/verification" element={<Verification />} />

      </Routes>
    </div>
  );
};

export default App;
