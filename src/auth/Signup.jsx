import AuthNavbar from "./AuthNavbar";
import { useState } from "react";
import { Link, useNavigate } from "react-router";
import axios from "axios";

function Signup() {
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [dob, setDob] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");

    const [errorMsg, setErrorMessage] = useState("");
    const [successMsg, setSuccessMessage] = useState("");

    const navigate = useNavigate();

    const onSignup = async (event) => {
        event.preventDefault();
        const data = { 
            name, 
            username, 
            password, 
            dob, 
            phoneNumber, 
            address 
        }

        try {
            await axios.post("http://localhost:8080/api/auth/signup", data);
            setSuccessMessage("Registration Successful! Redirecting to Login...");

            setTimeout(() => {
                navigate("/login");
            }, 1500);
        } catch (err) {
            setErrorMessage("Username already exists or invalid details");
        }
    };

    const clearError = () => setErrorMessage("");

    return (
        <div className="bg-light min-vh-100">
            <AuthNavbar />

            <div className="container">
                <div className="row justify-content-center mt-5">
                    <div className="col-md-6">
                        <div className="card shadow border-0 rounded-4">
                            <div className="card-body p-4">
                                <h2 className="text-center text-primary fw-bold mb-4">Sign up</h2>

                                {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
                                {successMsg && <div className="alert alert-success">{successMsg}</div>}

                                <form onSubmit={onSignup}>
                                    <div className="mb-3">
                                        <label className="form-label">Name</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            placeholder="Enter full name"
                                            value={name}
                                            onChange={(e) => { setName(e.target.value); clearError(); }}
                                            required
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Email</label>
                                        <input
                                            type="email"
                                            className="form-control"
                                            placeholder="Enter email"
                                            value={username}
                                            onChange={(e) => { setUsername(e.target.value); clearError(); }}
                                            required
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Password</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            placeholder="Enter password"
                                            value={password}
                                            onChange={(e) => { setPassword(e.target.value); clearError(); }}
                                            required
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Date of Birth</label>
                                        <input
                                            type="date"
                                            className="form-control"
                                            value={dob}
                                            onChange={(e) => { setDob(e.target.value); clearError(); }}
                                            required
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Phone Number</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            placeholder="Enter phone number"
                                            value={phoneNumber}
                                            onChange={(e) => { setPhoneNumber(e.target.value); clearError(); }}
                                            required
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Address</label>
                                        <textarea
                                            className="form-control"
                                            rows="3"
                                            placeholder="Enter address"
                                            value={address}
                                            onChange={(e) => { setAddress(e.target.value); clearError(); }}
                                            required
                                        ></textarea>
                                    </div>

                                    <button type="submit" className="btn btn-primary w-100">Sign Up</button>
                                </form>

                                <div className="text-center mt-3">
                                    Already have an account?
                                    <Link to="/login" className="text-decoration-none ms-2">Login</Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Signup