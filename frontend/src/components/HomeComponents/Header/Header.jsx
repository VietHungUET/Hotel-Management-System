import React from 'react'
import './Header.css'
import { assets } from '../../../assets/assets'
import { Link } from 'react-router-dom'


const Header = () => {
  return (
    <div className="header">
        <img src={assets.header_img2} alt="" />
         <div className="overlay"></div>
          <div className="header-contents">
            <h2>Hotel Management System</h2>
            <p>Light Up Your Hotel Management with Lunora - Like the Luna and Aurora.</p>
            <Link to="/login"><button>Get started</button></Link>
          </div>
    </div>
  )
}

export default Header
