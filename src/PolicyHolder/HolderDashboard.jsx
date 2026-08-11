import { Link, useNavigate } from "react-router";
import HolderNavbar from "../PolicyHolder/HolderNavbar";

function HolderDashboard() {

    const username = localStorage.getItem("username")?.split("@")[0];
    const navigate = useNavigate()

    return (
        <div className="bg-light min-vh-100">

            <HolderNavbar />

            {/* Welcome Banner */}
            <section className="py-5 text-white" style={{ backgroundColor: "#4A90E2" }}>
                <div className="container">
                    <h1 className="display-5 fw-bold">Welcome, {username}</h1>
                    <p className="lead mb-3">Manage your vehicle insurance, policies and claims easily.</p>
                    <button className="btn btn-light" onClick={()=>navigate("/holder/create-proposal")}>Buy New Insurance</button>
                </div>
            </section>

            {/* Quick Actions */}
            <section className="py-4">
                <div className="container">
                    <h3 className="mb-3">Overview</h3>
                    <div className="row">
                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Add Vehicle</h5>
                                    <p>Register your vehicle details</p>
                                    <button className="btn btn-primary" 
                                    onClick={()=>navigate("/holder/vehicles")}>Add</button>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>My Proposals</h5>
                                    <p>Track insurance applications</p>
                                    <button className="btn btn-primary" onClick={()=>navigate("/holder/proposals")}>View</button>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>My Policies</h5>
                                    <p>View active policies</p>
                                    <button className="btn btn-primary" onClick={()=>navigate("/holder/policies")}>View</button>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Raise Claim</h5>
                                    <p>Submit claim request</p>
                                    <button className="btn btn-primary" onClick={()=>navigate("/holder/claims")}>Claim</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Dashboard Summary */}
            <section className="py-2">
                <div className="container">
                    <h3 className="mb-3">My Insurance Details</h3>
                    <div className="row">
                        <div className="col-md-4 mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>1</h4>
                                    <p className="mb-0">Registered Vehicles</p>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4 mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>1</h4>
                                    <p className="mb-0">Active Policies</p>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4 mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>0</h4>
                                    <p className="mb-0">Pending Claims</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Footer */}
            <footer className="bg-dark text-white text-center py-2">
                <p className="mb-0">© 2026 EliteDrive Insurance</p>
            </footer>
        </div>
    );
}

export default HolderDashboard;