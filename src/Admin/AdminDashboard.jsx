import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import AdminNavbar from "../Admin/AdminNavbar";

function AdminDashboard() {
    const navigate = useNavigate();

    const [summary, setSummary] = useState({

        employees: 0,
        customers: 0,
        deletedCustomers: 0,
        plans: 0,
        pendingProposals: 0

    })

    const token = localStorage.getItem("token");
    const config = {
        headers: {
            Authorization: "Bearer " + token
        }

    }

    useEffect(() => {
        const verifyAuth = async () => {
            const token = localStorage.getItem("token");
            const config = {
                headers: { Authorization: "Bearer " + token }
            };

            try {
                const response = await axios.get("http://localhost:8080/api/auth/user-details", config);
                const backendUsername = response.data?.username;
                const backendRole = response.data?.role;
                const localUsername = localStorage.getItem("username");
                const localRole = localStorage.getItem("role");

                if (
                    backendUsername !== localUsername ||
                    backendRole !== localRole ||
                    backendRole !== "ADMIN"
                ) {
                    localStorage.clear();
                    navigate("/page-not-found");
                }
            } catch (err) {
                localStorage.clear();
                navigate("/login");
            }
        };
        verifyAuth();
    }, [])

    const fetchSummary = async () => {

        

        try {


            const employeeResponse = await axios.get(
                "http://localhost:8080/api/employee/get-all",
                config
            );


            const customerResponse = await axios.get(
                "http://localhost:8080/api/policyHolder/get-all",
                config
                
                
            )
            console.log(customerResponse.data);


            const planResponse = await axios.get(
                "http://localhost:8080/api/insurancePlan/get-all",
                config
            );


            const proposalResponse = await axios.get(
                "http://localhost:8080/api/proposal/get-all",
                config
            );

            


            setSummary({

                employees:

                    employeeResponse.data.filter(
                        emp => emp.isActive
                    ).length,


                customers:

                    customerResponse.data.filter(
                        customer => customer.isActive
                    ).length,


                deletedCustomers:

                    customerResponse.data.filter(
                        customer => !customer.isActive
                    ).length,


                plans:

                    planResponse.data.filter(
                        plan => plan.isActive
                    ).length,


                pendingProposals:

                    proposalResponse.data.filter(

                        proposal =>
                            proposal.proposalStatus !== "APPROVED"

                    ).length

            });


        }
        catch (error) {

            console.log(error);

        }

    };
    useEffect(() => {

        fetchSummary();

    }, []);

    return (
        <div className="bg-light min-vh-100">
            <AdminNavbar />

            {/* Welcome Content */}
            <section className="py-5 text-white" style={{ backgroundColor: "#4A90E2" }}>
                <div className="container">
                    <h1 className="display-5 fw-bold">Welcome, Admin</h1>
                    <p className="lead mb-0">Manage employees, customers and insurance operations.</p>
                </div>
            </section>

            {/* Overview */}
            <section className="py-4">
                <div className="container">
                    <h3 className="mb-3">Overview</h3>
                    <div className="row">
                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Employees</h5>
                                    <p>Create and manage employees</p>
                                    <button className="btn btn-primary" onClick={() => navigate("/admin/employees")}>Manage</button>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Insurance Plans</h5>
                                    <p>Add and update plans</p>
                                    <button className="btn btn-primary" onClick={() => navigate("/admin/insurance-plan")}>Manage</button>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Customers</h5>
                                    <p>View policy holders</p>
                                    <button className="btn btn-primary" >View</button>
                                </div>
                            </div>
                        </div>


                        <div className="col-md-3 mb-3">
                            <div className="card shadow h-100">
                                <div className="card-body text-center">
                                    <h5>Proposals</h5>
                                    <p>Assign proposals to employees</p>
                                    <button className="btn btn-primary" onClick={() => navigate("/admin/proposals")}>View</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Summary */}
            <section className="py-2">
                <div className="container">
                    <h3 className="mb-3">Summary</h3>
                    <div className="row">
                        <div className="col mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>{summary.employees}</h4>
                                    <p className="mb-0">Employees</p>
                                </div>
                            </div>
                        </div>

                        <div className="col mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>{summary.customers}</h4>
                                    <p className="mb-0">Customers</p>
                                </div>
                            </div>
                        </div>

                        <div className="col mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>{summary.deletedCustomers}</h4>
                                    <p className="mb-0">Deactivated Customers</p>
                                </div>
                            </div>
                        </div>

                        <div className="col mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>{summary.plans}</h4>
                                    <p className="mb-0">Insurance Plans</p>
                                </div>
                            </div>
                        </div>

                        <div className="col mb-3">
                            <div className="card shadow text-center">
                                <div className="card-body">
                                    <h4>{summary.pendingProposals}</h4>
                                    <p className="mb-0">Pending Proposals</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>



            <footer className="bg-dark text-white text-center py-2">
                <p className="mb-0">© 2026 EliteDrive Insurance</p>
            </footer>
        </div>
    );
}

export default AdminDashboard