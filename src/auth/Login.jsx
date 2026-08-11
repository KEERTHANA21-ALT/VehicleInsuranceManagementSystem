import AuthNavbar from "./AuthNavbar"
import { useState } from "react"
import { Link, useNavigate } from "react-router"
import axios from "axios"

function Login() {

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [errorMsg, setErrorMessage] = useState('')

    const navigate = useNavigate()

    const onLogin = async (event) => {

        event.preventDefault()

        let authToken = window.btoa(username + ":" + password)

        let config = {
            headers: {
                'Authorization': 'Basic ' + authToken
            }
        }

        try {

            const response = await axios.get(
                "http://localhost:8080/api/auth/login",
                config
            )


            localStorage.setItem("username", username)
            localStorage.setItem("token", response.data?.token)
            localStorage.setItem("role", response.data?.role)
            localStorage.setItem("employeeRole", response.data.employeeRole);


            switch (response.data?.role) {

                case "ADMIN":
                    navigate("/admin")
                    break;

                case "POLICY_HOLDER":
                    navigate("/holder")
                    break;

                case "EMPLOYEE":
                    navigate("/employee")
                    break;

            }

        }
        catch (err) {

            setErrorMessage("Invalid username or password")

        }

    }

    const clearError = () => {
    setErrorMessage("");
}


    return (
        <div className="bg-light min-vh-100">
            <AuthNavbar />

            <div className="container">
                <div className="row justify-content-center mt-5">
                    <div className="col-md-5">
                        <div className="card shadow border-0 rounded-4">
                            <div className="card-body p-4">
                                <h2 className="text-center text-primary fw-bold mb-4">Login</h2>

                                {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

                                <form onSubmit={onLogin}>
                                    <div className="mb-3">
                                        <label className="form-label">Username</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            placeholder="Enter username"
                                            value={username}
                                            onChange={(e) => { setUsername(e.target.value)
                                             clearError()
                                         }}
                                        />
                                    </div>

                                    <div className="mb-3">
                                        <label className="form-label">Password</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            placeholder="Enter password"
                                            value={password}
                                            onChange={(e) => { setPassword(e.target.value)
                                            clearError()
                                         }}
                                        />
                                    </div>

                                    <button type="submit" className="btn btn-primary w-100">Login</button>
                                </form>

                                <div className="text-center mt-3">
                                    Don't have an account?
                                    <Link to="/sign-up" className="text-decoration-none ms-2">Sign Up</Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

}


export default Login