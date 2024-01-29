import React, { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import axios from 'axios';

function Signup() {
    const navigate = useNavigate();
    // Define state for each input field
    const [email, setEmail] = useState('');
    const [fullName, setFullName] = useState('');
    const [password, setPassword] = useState('');
    const [address, setAddress] = useState('');
    const [postcode, setPostcode] = useState('');
    const [birthday, setBirthday] = useState('');
    const [ic, setIc] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('YOUR_SIGNUP_API_ENDPOINT', {
                email,
                fullName,
                password,
                address,
                postcode,
                birthday,
                ic,
                phoneNumber
                // ... include other fields as necessary
            });

            if (response.status === 200) {
                // Handle signup success (e.g., showing success message, navigating to login)
                navigate('/login');
            } else {
                // Handle errors (e.g., showing an error message)
                console.error('Signup failed');
            }
        } catch (error) {
            console.error('There was an error during signup', error);
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="px-8 py-6 mt-4 text-left bg-white shadow-lg">
                <h3 className="text-2xl font-bold text-center">Signup</h3>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label className="block" htmlFor="email">Email</label>
                        <input type="email" placeholder="Email"
                               id="email"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="fullName">Full Name</label>
                        <input type="text" placeholder="Full Name"
                               id="fullName"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setFullName(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="password">Password</label>
                        <input type="password" placeholder="Password"
                               id="password"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="address">Address</label>
                        <input type="text" placeholder="Address"
                               id="address"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setAddress(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="postcode">Postcode</label>
                        <input type="text" placeholder="Postcode"
                               id="postcode"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setPostcode(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="birthday">Birthday</label>
                        <input type="date"
                               id="birthday"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setBirthday(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="ic">IC</label>
                        <input type="text" placeholder="IC Number"
                               id="ic"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setIc(e.target.value)}
                        />
                    </div>
                    <div className="mt-4">
                        <label className="block" htmlFor="phonenumber">Phone Number</label>
                        <input type="tel" placeholder="Phone Number"
                               id="phonenumber"
                               className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                               onChange={(e) => setPhoneNumber(e.target.value)}
                        />
                    </div>
                    <div className="flex items-baseline justify-between">
                        <button className="px-6 py-2 mt-4 text-white bg-blue-600 rounded-lg hover:bg-blue-900">Sign Up</button>
                    </div>
                </form>
                <div className="flex items-center justify-center">
                    <p className="mt-4 text-center">Already have an account? <Link to="/login" className="text-blue-600 hover:text-blue-800">Login</Link></p>
                </div>
            </div>
        </div>
    );
}

export default Signup;
