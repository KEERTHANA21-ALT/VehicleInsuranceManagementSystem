
import { useEffect, useState } from "react";
import axios from "axios";
import services from "../data/services";
import Navbar from "./Navbar";
import "../css/HomeDashboard.css";
import { useNavigate } from "react-router";

function HomeDashboard() {

    const navigate = useNavigate();

    const [plans, setPlans] = useState([]);

    const [loadingPlans, setLoadingPlans] = useState(true);

    // Pagination
    const [planPage, setPlanPage] = useState(0);

    const planSize = 3;


    // =========================================================
    // REDIRECT AUTHENTICATED USERS
    // =========================================================

    useEffect(() => {

        const token = localStorage.getItem("token");
        const role = localStorage.getItem("role");

        if (token) {

            if (role === "ADMIN") {
                navigate("/admin");
            }
            else if (role === "EMPLOYEE") {
                navigate("/employee");
            }
            else if (role === "POLICY_HOLDER") {
                navigate("/holder");
            }

        }

    }, [navigate]);


    // =========================================================
    // GET INSURANCE PLANS
    // =========================================================

    useEffect(() => {

        const getPlans = async () => {

            try {

                setLoadingPlans(true);

                const response = await axios.get(
                    "http://localhost:8080/api/insurancePlan/get-all"
                );

                console.log(
                    "Insurance Plans:",
                    response.data
                );

                setPlans(response.data || []);

            }
            catch (error) {

                console.log(
                    "Error fetching insurance plans:",
                    error.response?.data || error.message
                );

                setPlans([]);

            }
            finally {

                setLoadingPlans(false);

            }

        };

        getPlans();

    }, []);


    // =========================================================
    // PAGINATION
    // ONLY 3 PLANS PER PAGE
    // =========================================================

    const startIndex =
        planPage * planSize;

    const displayedPlans =
        plans.slice(
            startIndex,
            startIndex + planSize
        );

    const planNext =
        startIndex + planSize < plans.length;


    // =========================================================
    // PREVIOUS PAGE
    // =========================================================

    const handlePrevious = () => {

        if (planPage > 0) {

            setPlanPage(planPage - 1);

        }

    };


    // =========================================================
    // NEXT PAGE
    // =========================================================

    const handleNext = () => {

        if (planNext) {

            setPlanPage(planPage + 1);

        }

    };


    // =========================================================
    // EXPLORE PLANS
    // =========================================================

    const handleExplorePlans = () => {

        document
            .getElementById("plans")
            ?.scrollIntoView({
                behavior: "smooth"
            });

    };


    return (

        <div>

            <Navbar />


            {/* =====================================================
                HOME
            ===================================================== */}

            <section
                id="home"
                className="text-white py-5"
                style={{
                    backgroundColor: "#4A90E2"
                }}
            >

                <div className="container">

                    <div className="row align-items-center">

                        <div className="col-md-7">

                            <h1 className="display-4 fw-bold">
                                Protect Your Vehicle With Trusted Insurance
                            </h1>

                            <p className="lead">
                                Complete vehicle protection with affordable
                                plans, easy claims and secure policy management.
                            </p>


                            <button
                                className="btn btn-warning me-3"
                                onClick={handleExplorePlans}
                            >
                                Explore Plans
                            </button>


                            <button
                                className="btn btn-light"
                                onClick={() =>
                                    navigate("/login")
                                }
                            >
                                Login
                            </button>

                        </div>


                        <div className="col-md-5">

                            <div className="card shadow p-4 text-center">

                                <h3 className="text-primary">
                                    Vehicle Insurance
                                </h3>

                                <p className="text-dark">
                                    Secure your vehicle today
                                </p>

                            </div>

                        </div>

                    </div>

                </div>

            </section>


            {/* =====================================================
                INSURANCE PLANS
            ===================================================== */}

            <section
                id="plans"
                className="py-5 bg-light"
            >

                <div className="container">

                    <h2 className="text-center mb-2">
                        Insurance Plans
                    </h2>

                    <p className="text-center text-muted mb-4">
                        Choose the right protection for your vehicle
                    </p>


                    {/* =================================================
                        LOADING
                    ================================================= */}

                    {loadingPlans ? (

                        <div className="text-center py-5">

                            <div
                                className="spinner-border text-primary"
                            ></div>

                            <p className="text-muted mt-3">
                                Loading insurance plans...
                            </p>

                        </div>

                    ) : plans.length === 0 ? (

                        /* =================================================
                           NO PLANS
                        ================================================= */

                        <div className="text-center py-5">

                            <i
                                className="bi bi-shield-x"
                                style={{
                                    fontSize: "45px",
                                    color: "#6c757d"
                                }}
                            ></i>

                            <h5 className="mt-3">
                                No Insurance Plans Available
                            </h5>

                            <p className="text-muted">
                                Please check again later.
                            </p>

                        </div>

                    ) : (

                        <>

                            {/* =================================================
                                PLAN CARDS
                                ONLY 3 CARDS PER PAGE
                            ================================================= */}

                            <div className="row">

                                {displayedPlans.map((plan) => (

                                    <div
                                        className="col-md-4 mb-4"
                                        key={plan.id}
                                    >

                                        <div
                                            className="card shadow h-100"
                                            style={{
                                                borderRadius: "12px",
                                                border: "none"
                                            }}
                                        >

                                            <div className="card-body p-4">

                                                {/* PLAN NAME */}

                                                <h4 className="fw-bold mb-3">
                                                    {plan.planName ||
                                                        plan.name ||
                                                        "Insurance Plan"}
                                                </h4>


                                                {/* PLAN TYPE */}

                                                {plan.planType && (

                                                    <p className="text-muted">

                                                        <i className="bi bi-shield-check me-2"></i>

                                                        {plan.planType}

                                                    </p>

                                                )}


                                                {/* COVERAGE */}

                                                {plan.coverageAmount !== undefined && (

                                                    <p>

                                                        <i className="bi bi-check-circle text-success me-2"></i>

                                                        Coverage:
                                                        <strong className="ms-1">
                                                            ₹ {plan.coverageAmount}
                                                        </strong>

                                                    </p>

                                                )}


                                                {/* COVER */}

                                                {plan.cover && (

                                                    <p>

                                                        <i className="bi bi-check-circle text-success me-2"></i>

                                                        {plan.cover}

                                                    </p>

                                                )}


                                                {/* PROTECTION */}

                                                {plan.protection && (

                                                    <p>

                                                        <i className="bi bi-check-circle text-success me-2"></i>

                                                        {plan.protection}

                                                    </p>

                                                )}


                                                {/* DESCRIPTION */}

                                                {plan.description && (

                                                    <p className="text-muted">
                                                        {plan.description}
                                                    </p>

                                                )}


                                                {/* PREMIUM */}

                                                {plan.basePremium !== undefined ? (

                                                    <h3
                                                        className="text-success fw-bold mt-3"
                                                    >
                                                        ₹ {plan.basePremium}
                                                    </h3>

                                                ) : plan.premium !== undefined ? (

                                                    <h3
                                                        className="text-success fw-bold mt-3"
                                                    >
                                                        ₹ {plan.premium}
                                                    </h3>

                                                ) : null}


                                                {/* VIEW DETAILS */}

                                                <button
                                                    className="btn btn-primary w-100 mt-3"
                                                    onClick={() =>
                                                        navigate("/login")
                                                    }
                                                >
                                                    View Details
                                                </button>

                                            </div>

                                        </div>

                                    </div>

                                ))}

                            </div>


                            {/* =================================================
                                PAGINATION
                            ================================================= */}

                            {plans.length > planSize && (

                                <div
                                    className="d-flex justify-content-center align-items-center mt-2 pt-3"
                                    style={{
                                        borderTop: "1px solid #dee2e6"
                                    }}
                                >

                                    <button
                                        className="btn btn-outline-secondary px-4"
                                        disabled={planPage === 0}
                                        onClick={handlePrevious}
                                    >
                                        ← Previous
                                    </button>


                                    <span
                                        className="mx-4 fw-semibold"
                                        style={{
                                            color: "#123b6d"
                                        }}
                                    >
                                        Page {planPage + 1}
                                    </span>


                                    <button
                                        className="btn btn-outline-primary px-4"
                                        disabled={!planNext}
                                        onClick={handleNext}
                                    >
                                        Next →
                                    </button>

                                </div>

                            )}

                        </>

                    )}

                </div>

            </section>


            {/* =====================================================
                SERVICES
            ===================================================== */}

            <section
                id="services"
                className="py-5"
            >

                <div className="container">

                    <h2 className="text-center mb-4">
                        Our Services
                    </h2>


                    <div className="row">

                        {services.map((service, index) => (

                            <div
                                className="col-md-3 mb-3"
                                key={index}
                            >

                                <div
                                    className="service-card card shadow h-100"
                                >

                                    <div className="card-body text-center">

                                        <h5>
                                            {service.title}
                                        </h5>

                                        <p>
                                            {service.description}
                                        </p>

                                    </div>

                                </div>

                            </div>

                        ))}

                    </div>

                </div>

            </section>


            {/* =====================================================
                WHY CHOOSE US
            ===================================================== */}

            <section
                id="why"
                className="py-5"
            >

                <div className="container">

                    <h2 className="text-center mb-4">
                        Why Choose Us?
                    </h2>


                    <div className="row text-center">

                        <div className="col-md-4">

                            <h5>
                                Fast Claim Settlement
                            </h5>

                            <p>
                                Simple and quick claim process
                            </p>

                        </div>


                        <div className="col-md-4">

                            <h5>
                                Affordable Premium
                            </h5>

                            <p>
                                Flexible plans for customers
                            </p>

                        </div>


                        <div className="col-md-4">

                            <h5>
                                Secure Management
                            </h5>

                            <p>
                                Safe policy and payment handling
                            </p>

                        </div>

                    </div>

                </div>

            </section>


            {/* =====================================================
                FOOTER
            ===================================================== */}

            <footer
                className="bg-dark text-white py-4 text-center"
            >

                <div className="container">

                    <div className="row">

                        <div className="col-md-4">

                            <h5>
                                EliteDrive Insurance
                            </h5>

                            <p>
                                Vehicle Insurance Management
                            </p>

                            <p>
                                Protecting your vehicle with simple,
                                secure and reliable insurance solutions.
                            </p>

                        </div>


                        <div className="col-md-4">

                            <h5>
                                Contact Us
                            </h5>

                            <p>
                                📧 Email: support@securelife.com
                            </p>

                            <p>
                                ☎ Phone: +91 98765 43210
                            </p>

                            <p>
                                📍 Chennai, Tamil Nadu
                            </p>

                        </div>


                        <div className="col-md-4">

                            <h5>
                                Quick Links
                            </h5>

                            <p>
                                Home
                            </p>

                            <p>
                                Insurance Plans
                            </p>

                            <p>
                                Claims Support
                            </p>

                        </div>

                    </div>


                    <hr />


                    <div className="text-center">

                        <small>
                            © 2026 EliteDrive Insurance.
                            All Rights Reserved.
                        </small>

                    </div>

                </div>

            </footer>

        </div>

    );

}

export default HomeDashboard;

