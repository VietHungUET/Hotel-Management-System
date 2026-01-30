import { useState } from "react";
import '@fortawesome/fontawesome-free/css/all.css';

import './Login.css';
import { BsXLg } from "react-icons/bs";
import { Link } from "react-router-dom";
import 'react-phone-number-input/style.css';
import PhoneInput from 'react-phone-number-input';
import userApi from "../../api/userApi";
import { useNavigate } from 'react-router-dom';

const Login = ({ session, setSession }) => {
    const [isActive, setIsActive] = useState(false);
    const [value, setValue] = useState();
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [email, setEmail] = useState();
    const [fullname, setFullname] = useState();
    const navigate = useNavigate();


    const handleRegisterClick = () => {
        setIsActive(true);
    };

    const handleLoginClick = () => {
        setIsActive(false);
    };

    const handleOnLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await userApi.getLogin({ username, password });
            if (response && response.token) {

                localStorage.setItem("token", response.token);

                // Lưu session data như hiện tại
                const sessionData = {
                    Username: response.username,
                    Role: response.role,
                    HotelId: 1,
                };
                setSession(sessionData);

                navigate("/main");
            } else {
                alert("Login failed: No token received");
            }
        } catch (error) {
            console.error("Login failed:", error);
            alert("Login failed");
        }
    };

    const handleOnSignUp = async (e) => {
        e.preventDefault();
        try {
            const response = await userApi.getSignUp({ username, password, email, fullname, value });
            console.log("response:", response);
            const pendingUser = {
                user_name: username,
                user_password: password,
                full_name: fullname,
                email,
                phone: value
            };
            localStorage.setItem("pendingUser", JSON.stringify(pendingUser));
            navigate("/verification");
        } catch (error) {
            alert("Invalid registration information.");
            console.error("Registration failed", error);
        }
    };



    return (
        <>
            <div className="login-body-page">
                <Link to="/"><BsXLg size={45} className="exit-icon" /></Link>
                <div className={`login-container ${isActive ? 'active' : ''}`} id="container">
                    <div className="form-container sign-up">
                        <form>
                            <h1>Create Account</h1>
                            <div className="col-12 social-login">
                                <i className="fa-brands fa-google-plus-g google"></i>
                            </div>
                            <span>or use your email for registration</span>
                            <input type="text" placeholder="Name" value={fullname} onChange={(e) => setFullname(e.target.value)} />
                            <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                            <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
                            <PhoneInput placeholder="Enter phone number." value={value}
                                onChange={setValue} className="PhoneInputInput"
                            />
                            <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                            <button type="submit" onClick={handleOnSignUp}>Sign Up</button>
                        </form>
                    </div>
                    <div className="form-container sign-in">
                        <form>
                            <h1>Sign In</h1>
                            <div className="col-12 social-login">
                                <i className="fa-brands fa-google-plus-g google"></i>
                            </div>
                            <span>or use your email password</span>
                            <input type="username" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                            <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                            <a href="#">Forget Your Password?</a>
                            <button type="submit" onClick={handleOnLogin}>Sign In</button>
                        </form>
                    </div>
                    <div className="toggle-container">
                        <div className="toggle">
                            <div className="toggle-panel toggle-left">
                                <h1>Welcome Back!</h1>
                                <p>Enter your personal details to use all of site features</p>
                                <button className="hidden" id="login" onClick={handleLoginClick}>Sign In</button>
                            </div>
                            <div className="toggle-panel toggle-right">
                                <h1>Hello, Friend!</h1>
                                <p>Register with your personal details to use all of site features</p>
                                <button className="hidden" id="register" onClick={handleRegisterClick}>Sign Up</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}



export default Login;