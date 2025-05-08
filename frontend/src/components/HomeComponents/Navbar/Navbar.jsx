import React, { useState, useEffect } from 'react'
import './Navbar.css'
import { Link } from 'react-router-dom'
import { assets } from '../../../assets/assets'

const Navbar = () => {
  const [navbar, setNavbar] = useState(false);

  useEffect(() => {
    window.addEventListener('scroll', () => {
      window.scrollY > 50 ? setNavbar(true) : setNavbar(false);
    });
  }, []);

  return (
    <div className={`navbar ${navbar ? 'scrolled' : 'transparent'}`}>
      <Link to="/" className="logo-container">
        <img src={assets.logo_img} alt="" className="logo" />
        <span className="logo-text">Lunora</span>
      </Link>
      <div className="navbar-right">
        <ul className="navbar-menu">
          <Link to="/" className="nav">Home</Link>
          <a href="/#about" className="nav">About</a>
          <a href="/#footer" className="nav">Contact</a>
          <Link to="/login"><button>Sign In</button></Link>
        </ul>
      </div>
    </div>
  )
}

export default Navbar