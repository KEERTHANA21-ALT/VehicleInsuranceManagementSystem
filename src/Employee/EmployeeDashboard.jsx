
import axios from "axios";
import { useEffect } from "react";
import { useNavigate } from "react-router";
import EmployeeNavbar from "./EmployeeNavbar";

function EmployeeDashboard() {

    const username = localStorage.getItem("username")?.split("@")[0];
    const employeeRole = localStorage.getItem("employeeRole");

    const navigate = useNavigate();

    useEffect(() => {

        const verifyAuth = async () => {

            const token = localStorage.getItem("token");

            const config = {
                headers: {
                    Authorization: "Bearer " + token
                }
            };

            try {

                const response = await axios.get(
                    "http://localhost:8080/api/auth/user-details",
                    config
                );

                if (response.data?.role !== "EMPLOYEE") {

                    localStorage.clear();
                    navigate("/page-not-found");

                }

            } catch (error) {

                localStorage.clear();
                navigate("/login");

            }

        };

        verifyAuth();

    }, [navigate]);


    return (

        <div className="bg-light min-vh-100 d-flex flex-column">

            <EmployeeNavbar />


            <div style={{ flex: 1 }}>


                {/* Welcome */}

                <section
                    className="py-5 text-white"
                    style={{
                        backgroundColor: "#4A90E2"
                    }}
                >

                    <div className="container">

                        <h1 className="display-5 fw-bold">
                            Welcome, {username}
                        </h1>

                        <p className="lead mb-0">
                            Manage customer proposals, policies and claims efficiently.
                        </p>

                    </div>

                </section>


                {/* Quick Access */}

                <section className="py-4">

                    <div className="container">

                        <h4 className="mb-4">
                            Quick Access
                        </h4>


                        {/* =================================================
                            INSURANCE MANAGER
                        ================================================= */}

                        {employeeRole === "INSURANCE_MANAGER" && (

                            <div className="row">

                                {/* Proposals */}

                                <div className="col-md-6 mb-4">

                                    <div className="card shadow-sm h-100">

                                        <div className="card-body text-center">

                                            <i
                                                className="bi bi-file-earmark-text"
                                                style={{
                                                    fontSize: "45px",
                                                    color: "#4A90E2"
                                                }}
                                            ></i>

                                            <h5 className="mt-3">
                                                Proposals
                                            </h5>

                                            <p className="text-muted">
                                                Review and approve customer insurance proposals.
                                            </p>

                                            <button
                                                className="btn btn-primary"
                                                onClick={() =>
                                                    navigate("/employee/proposals")
                                                }
                                            >
                                                View Proposals
                                            </button>

                                        </div>

                                    </div>

                                </div>


                                {/* Policies */}

                                <div className="col-md-6 mb-4">

                                    <div className="card shadow-sm h-100">

                                        <div className="card-body text-center">

                                            <i
                                                className="bi bi-file-earmark-check"
                                                style={{
                                                    fontSize: "45px",
                                                    color: "#198754"
                                                }}
                                            ></i>

                                            <h5 className="mt-3">
                                                Policies
                                            </h5>

                                            <p className="text-muted">
                                                View generated customer policies.
                                            </p>

                                            <button
                                                className="btn btn-success"
                                                onClick={() =>
                                                    navigate("/employee/policy")
                                                }
                                            >
                                                View Policies
                                            </button>

                                        </div>

                                    </div>

                                </div>

                            </div>

                        )}



{employeeRole === "CLAIM_MANAGER" && (

    <div className="row">

        {/* CLAIMS */}

        <div className="col-md-6 mb-4">

            <div className="card shadow-sm h-100 border-0">

                <div className="card-body text-center p-4">

                    <i
                        className="bi bi-clipboard-check"
                        style={{
                            fontSize: "45px",
                            color: "#4A90E2"
                        }}
                    ></i>

                    <h5 className="mt-3">
                        Claims
                    </h5>

                    <p className="text-muted">
                        Review inspected claims and approve or reject customer claims.
                    </p>

                    <button
                        className="btn btn-primary"
                        onClick={() =>
                            navigate("/employee/claims")
                        }
                    >
                        Manage Claims
                    </button>

                </div>

            </div>

        </div>


        {/* PENDING PAYMENTS */}

        <div className="col-md-6 mb-4">

            <div className="card shadow-sm h-100 border-0">

                <div className="card-body text-center p-4">

                    <i
                        className="bi bi-credit-card"
                        style={{
                            fontSize: "45px",
                            color: "#198754"
                        }}
                    ></i>

                    <h5 className="mt-3">
                        Pending Payments
                    </h5>

                    <p className="text-muted">
                        Process payments for approved customer claims.
                    </p>

                    <button
                        className="btn btn-success"
                        onClick={() =>
                            navigate("/employee/claim-payments")
                        }
                    >
                        Process Payments
                    </button>

                </div>

            </div>

        </div>

    </div>

)}
```



                        {/* =================================================
                            SURVEYOR
                        ================================================= */}

                        {employeeRole === "SURVEYOR" && (

                            <div className="row">


                                {/* ASSIGNED SURVEYS */}

                                <div className="col-md-4 mb-4">

                                    <div className="card shadow-sm h-100">

                                        <div className="card-body text-center">

                                            <i
                                                className="bi bi-car-front"
                                                style={{
                                                    fontSize: "45px",
                                                    color: "#4A90E2"
                                                }}
                                            ></i>

                                            <h5 className="mt-3">
                                                Assigned Surveys
                                            </h5>

                                            <p className="text-muted">
                                                View claims assigned to you for vehicle inspection.
                                            </p>

                                            <button
                                                className="btn btn-primary"
                                                onClick={() =>
                                                    navigate("/employee/surveys")
                                                }
                                            >
                                                View Surveys
                                            </button>

                                        </div>

                                    </div>

                                </div>


                                {/* PENDING SURVEYS */}

                                <div className="col-md-4 mb-4">

                                    <div className="card shadow-sm h-100">

                                        <div className="card-body text-center">

                                            <i
                                                className="bi bi-hourglass-split"
                                                style={{
                                                    fontSize: "45px",
                                                    color: "#e67e22"
                                                }}
                                            ></i>

                                            <h5 className="mt-3">
                                                Pending Surveys
                                            </h5>

                                            <p className="text-muted">
                                                Complete inspections and submit survey reports.
                                            </p>

                                            <button
                                                className="btn"
                                                style={{
                                                    backgroundColor: "#e67e22",
                                                    color: "white"
                                                }}
                                                onClick={() =>
                                                    navigate("/employee/pending-surveys")
                                                }
                                            >
                                                View Pending
                                            </button>

                                        </div>

                                    </div>

                                </div>


                                {/* COMPLETED SURVEYS */}

                                <div className="col-md-4 mb-4">

                                    <div className="card shadow-sm h-100">

                                        <div className="card-body text-center">

                                            <i
                                                className="bi bi-check-circle"
                                                style={{
                                                    fontSize: "45px",
                                                    color: "#198754"
                                                }}
                                            ></i>

                                            <h5 className="mt-3">
                                                Completed Surveys
                                            </h5>

                                            <p className="text-muted">
                                                View survey reports that you have already submitted.
                                            </p>

                                            <button
                                                className="btn btn-success"
                                                onClick={() =>
                                                    navigate("/employee/completed-surveys")
                                                }
                                            >
                                                View Reports
                                            </button>

                                        </div>

                                    </div>

                                </div>

                            </div>

                        )}

                    </div>

                </section>

            </div>


            {/* Footer */}

            <footer className="bg-dark text-white text-center py-2">

                © 2026 EliteDrive Insurance

            </footer>

        </div>

    );

}

export default EmployeeDashboard;

