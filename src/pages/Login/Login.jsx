import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const validate = (email, password) => {
        let isValid = true;
        setEmailError('');
        setPasswordError('');

        // Email validation
        if (!email || !/\S+@\S+\.\S+/.test(email)) {
            setEmailError('Please enter a valid email address.');
            isValid = false;
        }

        // Password validation
        if (!password || password.length < 6) {
            setPasswordError('Password must be at least 6 characters long.');
            isValid = false;
        }
        return isValid;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (validate(email, password)) {
            try {
                const response = await axios.post('http://localhost:8005/auth/login', { email, password });
                console.log('Server response:', response.data);

                if (response.data && response.data.token) {
                    localStorage.setItem('token', response.data.token);
                    navigate('/dashboard'); // Navigate to the dashboard
                } else {
                    console.error('No token in response:', response.data);
                }
            } catch (error) {
                console.error('Login error:', error.response ? error.response.data : error);
            }
        } else {
            console.error('Form is invalid, do not submit.');
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="px-8 py-6 mt-4 text-left bg-white shadow-lg">
                <h3 className="text-2xl font-bold text-center">Login to your account</h3>
                <form onSubmit={handleSubmit}>
                    <div className="mt-4">
                        <div>
                            <label className="block" htmlFor="email">Email</label>
                            <input type="text" placeholder="Email"
                                   id="email"
                                   className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                                   onChange={(e) => setEmail(e.target.value)}
                            />
                            {emailError && <p className="text-red-500">{emailError}</p>}
                        </div>
                        <div className="mt-4">
                            <label className="block" htmlFor="password">Password</label>
                            <input type="password" placeholder="Password"
                                   id="password"
                                   className="w-full px-4 py-2 mt-2 border rounded-md focus:outline-none focus:ring-1 focus:ring-blue-600"
                                   onChange={(e) => setPassword(e.target.value)}
                            />
                            {passwordError && <p className="text-red-500">{passwordError}</p>}
                        </div>
                        <div className="flex items-baseline justify-between">
                            <button className="px-6 py-2 mt-4 text-white bg-blue-600 rounded-lg hover:bg-blue-900">Login</button>
                        </div>
                    </div>
                </form>
                <div className="flex items-center justify-center">
                    <p className="mt-4 text-center">Don't have an account? <Link to="/signup" className="text-blue-600 hover:text-blue-800">Sign Up</Link></p>
                </div>
            </div>
        </div>
    );
}

export default Login;
