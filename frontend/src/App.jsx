import {BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import Home from "./pages/Home/Home";
import Login from "./pages/Login/Login";
import Main from "./pages/Main/Main";

import Revenue from "./components/MainComponents/Report/Revenue";
import RoomBooking from "./components/MainComponents/Report/RoomBooking";
import Verification from "./pages/Login/Verification";
import { useState, useEffect } from "react";
import userApi from "./api/userApi";

const App = () => {
  const [session, setSession] = useState();
  const [loading, setLoading] = useState(true); // Thêm trạng thái loading

    // Kiểm tra session từ backend khi ứng dụng khởi động
    useEffect(() => {
      const checkSession = async () => {
        try {
          const response = await userApi.getHome(); // Gọi API /home
          if (response.status === 200) {
            setSession({
              Username: response.data.username,
              Role: response.data.role,
              HotelId: response.data.hotelId,
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
          <Route path="/login" element={<Login session={session} setSession={setSession}/>} />
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
